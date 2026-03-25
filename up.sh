#!/bin/bash

# Start all Docker containers for Sanos y Salvos
# Color definitions
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}================================${NC}"
echo -e "${GREEN}Sanos y Salvos - Starting Stack${NC}"
echo -e "${GREEN}================================${NC}"

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo -e "${RED}Docker is not installed. Please install Docker first.${NC}"
    exit 1
fi

echo -e "${YELLOW}Starting Docker containers...${NC}\n"

# Start containers in detached mode
docker-compose up -d

if [ $? -eq 0 ]; then
    echo -e "\n${GREEN}✓ Stack started successfully!${NC}"
    echo -e "\n${YELLOW}Services running on:${NC}"
    echo -e "  API Gateway:       ${GREEN}http://localhost:8080${NC}"
    echo -e "  MS Usuarios:       ${GREEN}http://localhost:8084${NC}"
    echo -e "  MS Reportes:       ${GREEN}http://localhost:8083${NC}"
    echo -e "  MS Geolocalizacion:${GREEN}http://localhost:8081${NC}"
    echo -e "  MS Coincidencias:  ${GREEN}http://localhost:8082${NC}"
    echo -e "  MS Notificaciones: ${GREEN}http://localhost:8085${NC}"
    echo -e "  MySQL:             ${GREEN}localhost:3306${NC}"
    echo -e "\n${YELLOW}Useful commands:${NC}"
    echo -e "  View logs:         ${GREEN}./logs.sh${NC}"
    echo -e "  Health check:      ${GREEN}./health-check.sh${NC}"
    echo -e "  Stop stack:        ${GREEN}./down.sh${NC}"
else
    echo -e "\n${RED}✗ Failed to start stack. Check the output above for errors.${NC}"
    exit 1
fi
