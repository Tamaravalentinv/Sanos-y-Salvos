#!/bin/bash

# View logs from all Docker containers
# Color definitions
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}================================${NC}"
echo -e "${GREEN}Sanos y Salvos - Container Logs${NC}"
echo -e "${GREEN}================================${NC}"

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo -e "${RED}Docker is not installed.${NC}"
    exit 1
fi

echo -e "${YELLOW}Tailing logs from all containers (Ctrl+C to exit)...${NC}\n"

# Tail logs from all containers
docker-compose logs -f
