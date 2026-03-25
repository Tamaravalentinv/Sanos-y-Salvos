#!/bin/bash

# Stop and remove all Docker containers for Sanos y Salvos
# Color definitions
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}================================${NC}"
echo -e "${GREEN}Sanos y Salvos - Stopping Stack${NC}"
echo -e "${GREEN}================================${NC}"

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo -e "${RED}Docker is not installed.${NC}"
    exit 1
fi

echo -e "${YELLOW}Stopping Docker containers...${NC}\n"

# Stop and remove containers, networks, images
docker-compose down -v

if [ $? -eq 0 ]; then
    echo -e "\n${GREEN}✓ Stack stopped and cleaned successfully!${NC}"
else
    echo -e "\n${RED}✗ Failed to stop stack. Check the output above for errors.${NC}"
    exit 1
fi
