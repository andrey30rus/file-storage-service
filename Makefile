# ============================================================================
# Cloud Storage — Makefile
# ============================================================================

.DEFAULT_GOAL := help
SHELL := /bin/bash

COMPOSE_FILE := docker-compose.yml
ENV_FILE := .env
ENV_TEMPLATE := .env.example
DC := docker compose -f $(COMPOSE_FILE) --env-file $(ENV_FILE) --project-name cloud-storage

GREEN  := \033[0;32m
YELLOW := \033[0;33m
RED    := \033[0;31m
BLUE   := \033[0;34m
NC     := \033[0m

ifneq (,$(wildcard $(ENV_FILE)))
	include $(ENV_FILE)
	export
endif

.PHONY: help init up up-infra up-app down clean ps logs \
        backend-run backend-build backend-test \
        frontend-install frontend-dev frontend-build

# ----------------------------------------------------------------------------
# Help
# ----------------------------------------------------------------------------

help: ## Show this help
	@echo -e "$(GREEN)Cloud Storage — доступные команды:$(NC)"
	@grep -E '^[a-zA-Z_-]+:.*##' $(MAKEFILE_LIST) | sort | \
		awk -F'##' '{cmd=$$1; sub(/:.*/, "", cmd); gsub(/^ +| +$$/, "", $$2); printf "  $(BLUE)%-20s$(NC) %s\n", cmd, $$2}'

init: ## Create .env from .env.example
	@if [ -f $(ENV_FILE) ]; then \
		echo -e "$(YELLOW)$(ENV_FILE) already exists$(NC)"; \
	else \
		cp $(ENV_TEMPLATE) $(ENV_FILE); \
		echo -e "$(GREEN)Created $(ENV_FILE)$(NC)"; \
	fi

.env-check:
	@if [ ! -f $(ENV_FILE) ]; then \
		echo -e "$(RED)$(ENV_FILE) not found, run 'make init' first$(NC)"; \
		exit 1; \
	fi

# ----------------------------------------------------------------------------
# Docker Compose
# ----------------------------------------------------------------------------

up: .env-check ## Start infra + app (docker compose, build images)
	@$(DC) --profile infra --profile app up -d --build
	@echo -e "$(GREEN)backend: http://localhost:$(BACKEND_PORT)  frontend: http://localhost:$(FRONTEND_PORT)  minio console: http://localhost:$(MINIO_CONSOLE_PORT)$(NC)"

up-infra: .env-check ## Start only infra (postgres, redis, minio)
	@$(DC) --profile infra up -d

up-app: .env-check ## Start app services (backend, frontend; infra starts too if not running)
	@$(DC) --profile infra --profile app up -d --build backend frontend

up-prod: .env-check ## Start stack in prod-like mode: only frontend exposed
	@$(DC) -f docker-compose.prod.yml --profile infra --profile app up -d --build
	@echo -e "$(GREEN)Prod-like: наружу только frontend → http://localhost:$(FRONTEND_PORT) (api и swagger — через nginx)$(NC)"

down: ## Stop all containers (data on volumes is kept)
	@$(DC) --profile infra --profile app down

clean: ## Stop containers and DELETE volumes (all data is lost)
	@$(DC) --profile infra --profile app down -v

ps: ## Show containers
	@$(DC) --profile infra --profile app ps

logs: ## Show logs, e.g. make logs SERVICE=backend
	@$(DC) logs -f $(SERVICE)

# ----------------------------------------------------------------------------
# Backend (local run, infra from make up-infra)
# ----------------------------------------------------------------------------

backend-run: ## Run backend locally via mvnw (uses backend/.env if present)
	@set -a; [ -f backend/.env ] && . backend/.env; set +a; cd backend && ./mvnw spring-boot:run

backend-build: ## Build backend jar
	@cd backend && ./mvnw -DskipTests package

backend-test: ## Run backend tests
	@cd backend && ./mvnw test

# ----------------------------------------------------------------------------
# Frontend (local run: make frontend-dev)
# ----------------------------------------------------------------------------

frontend-install: ## Install frontend dependencies (npm install)
	@cd frontend && npm install

frontend-dev: ## Run Vite dev server
	@cd frontend && npm run dev

frontend-build: ## Build frontend into frontend/dist
	@cd frontend && npm run build
