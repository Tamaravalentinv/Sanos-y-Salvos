#!/bin/bash

# Build all Docker images for Sanos y Salvos microservices
# Color definitions
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}================================${NC}"
echo -e "${GREEN}Sanos y Salvos - Docker Build${NC}"
echo -e "${GREEN}================================${NC}"

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo -e "${RED}Docker is not installed. Please install Docker first.${NC}"
    exit 1
fi

echo -e "${YELLOW}Building Docker images...${NC}\n"

# Build all services
docker-compose build --no-cache

if [ $? -eq 0 ]; then
    echo -e "\n${GREEN}✓ All Docker images built successfully!${NC}"
    echo -e "${YELLOW}\nNext steps:${NC}"
    echo -e "  1. Start the stack: ${GREEN}./up.sh${NC}"
    echo -e "  2. Check logs: ${GREEN}./logs.sh${NC}"
    echo -e "  3. Health check: ${GREEN}./health-check.sh${NC}"
else
    echo -e "\n${RED}✗ Build failed. Check the output above for errors.${NC}"
    exit 1
fi
