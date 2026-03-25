#!/bin/bash

# Health check all services
# Color definitions
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${GREEN}================================${NC}"
echo -e "${GREEN}Sanos y Salvos - Health Check${NC}"
echo -e "${GREEN}================================${NC}"

# Check if curl is installed
if ! command -v curl &> /dev/null; then
    echo -e "${RED}curl is not installed. Please install curl first.${NC}"
    exit 1
fi

# Array of services to check
services=(
    "http://localhost:8080 API Gateway"
    "http://localhost:8084 MS Usuarios"
    "http://localhost:8083 MS Reportes"
    "http://localhost:8081 MS Geolocalizacion"
    "http://localhost:8082 MS Coincidencias"
    "http://localhost:8085 MS Notificaciones"
)

echo -e "\n${YELLOW}Checking service health...${NC}\n"

all_healthy=true

for service in "${services[@]}"; do
    url=$(echo $service | awk '{print $1}')
    name=$(echo $service | awk '{$1=""; print $0}' | sed 's/^ //')
    
    # Try to reach the service
    response=$(curl -s -o /dev/null -w "%{http_code}" "$url/actuator/health" 2>/dev/null)
    
    if [ "$response" == "200" ]; then
        echo -e "${GREEN}✓${NC} $name ${BLUE}($url)${NC} - ${GREEN}HEALTHY${NC}"
    else
        echo -e "${RED}✗${NC} $name ${BLUE}($url)${NC} - ${RED}UNHEALTHY${NC} (HTTP $response)"
        all_healthy=false
    fi
done

echo ""

# Check container status
echo -e "${YELLOW}Container Status:${NC}\n"
docker-compose ps

if [ "$all_healthy" = true ]; then
    echo -e "\n${GREEN}✓ All services are healthy!${NC}"
    exit 0
else
    echo -e "\n${RED}✗ Some services are unhealthy. Check logs with: ./logs.sh${NC}"
    exit 1
fi
