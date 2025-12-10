#!/bin/bash

################################################################################
# Azure Container Instances Deployment Script
#
# Deploys the Number Guessing Game API to Azure Container Instances using Docker Hub
#
# Usage:
#   ./deploy-to-aci.sh <docker-username> [container-name] [resource-group] [region]
#
# Examples:
#   ./deploy-to-aci.sh myusername                    # Uses defaults
#   ./deploy-to-aci.sh myusername numguess eastus    # Custom container name and region
#   ./deploy-to-aci.sh myusername numguess my-rg eastus  # Full customization
#
# Prerequisites:
#   - Azure CLI 2.50+ (az --version)
#   - Docker installed and logged in (docker login)
#   - Docker Hub account with numguess-api image pushed
#   - Azure subscription and logged in (az login)
#
################################################################################

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Default values
DOCKER_USERNAME="${1:-}"
CONTAINER_NAME="${2:-numguess-api}"
RESOURCE_GROUP="${3:-numguess-rg}"
REGION="${4:-eastus}"
IMAGE_TAG="1.0.0"
CPU="1"
MEMORY="1"

################################################################################
# Helper Functions
################################################################################

print_header() {
    echo -e "\n${BLUE}================================${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}================================${NC}\n"
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_info() {
    echo -e "${YELLOW}ℹ $1${NC}"
}

print_step() {
    echo -e "\n${BLUE}→ $1${NC}"
}

usage() {
    echo "Azure Container Instances Deployment Script"
    echo ""
    echo "Usage: $0 <docker-username> [container-name] [resource-group] [region]"
    echo ""
    echo "Arguments:"
    echo "  docker-username   Docker Hub username (required)"
    echo "  container-name    Container instance name (default: numguess-api)"
    echo "  resource-group    Azure resource group (default: numguess-rg)"
    echo "  region            Azure region (default: eastus)"
    echo ""
    echo "Examples:"
    echo "  $0 myusername"
    echo "  $0 myusername numguess eastus"
    echo "  $0 myusername numguess my-rg eastus"
    exit 1
}

################################################################################
# Validation
################################################################################

print_header "Azure Container Instances Deployment"

if [ -z "$DOCKER_USERNAME" ]; then
    print_error "Docker username is required"
    usage
fi

echo "Configuration:"
echo "  Docker Image: ${DOCKER_USERNAME}/numguess-api:${IMAGE_TAG}"
echo "  Container Name: $CONTAINER_NAME"
echo "  Resource Group: $RESOURCE_GROUP"
echo "  Region: $REGION"
echo "  CPU: $CPU"
echo "  Memory: ${MEMORY}GB"
echo ""

# Check prerequisites
print_step "Checking prerequisites..."

# Check Azure CLI
if ! command -v az &> /dev/null; then
    print_error "Azure CLI is not installed"
    echo "Install from: https://learn.microsoft.com/en-us/cli/azure/install-azure-cli"
    exit 1
fi
print_success "Azure CLI found: $(az version --query '\"azure-cli\"' -o tsv)"

# Check Docker
if ! command -v docker &> /dev/null; then
    print_error "Docker is not installed"
    echo "Install from: https://docs.docker.com/get-docker/"
    exit 1
fi
print_success "Docker found: $(docker version --format '{{.Server.Version}}')"

# Check Azure login
if ! az account show &> /dev/null; then
    print_error "Not logged in to Azure"
    print_info "Running: az login"
    az login
fi
CURRENT_ACCOUNT=$(az account show --query 'name' -o tsv)
print_success "Logged in to Azure: $CURRENT_ACCOUNT"

# Check if Docker image exists
print_step "Verifying Docker image..."
if docker pull "${DOCKER_USERNAME}/numguess-api:${IMAGE_TAG}" 2>&1 | grep -q "Pulling from"; then
    print_success "Docker image verified: ${DOCKER_USERNAME}/numguess-api:${IMAGE_TAG}"
else
    print_error "Could not pull Docker image: ${DOCKER_USERNAME}/numguess-api:${IMAGE_TAG}"
    echo ""
    echo "Make sure to:"
    echo "  1. Build the image: docker build -t numguess-api:$IMAGE_TAG ./numguess-service"
    echo "  2. Tag it: docker tag numguess-api:$IMAGE_TAG ${DOCKER_USERNAME}/numguess-api:$IMAGE_TAG"
    echo "  3. Push it: docker push ${DOCKER_USERNAME}/numguess-api:$IMAGE_TAG"
    exit 1
fi

################################################################################
# Create Resource Group
################################################################################

print_step "Creating resource group..."
if az group exists --name "$RESOURCE_GROUP" --query value -o tsv | grep -q true; then
    print_success "Resource group already exists: $RESOURCE_GROUP"
else
    az group create --name "$RESOURCE_GROUP" --location "$REGION" > /dev/null
    print_success "Resource group created: $RESOURCE_GROUP"
fi

################################################################################
# Deploy Container Instance
################################################################################

print_step "Deploying to Azure Container Instances..."

# Check if container already exists
if az container show --resource-group "$RESOURCE_GROUP" --name "$CONTAINER_NAME" &> /dev/null; then
    print_info "Container instance already exists: $CONTAINER_NAME"
    print_info "Deleting old instance..."
    az container delete --resource-group "$RESOURCE_GROUP" --name "$CONTAINER_NAME" --yes > /dev/null
    sleep 5
fi

# Deploy container
az container create \
    --resource-group "$RESOURCE_GROUP" \
    --name "$CONTAINER_NAME" \
    --image "${DOCKER_USERNAME}/numguess-api:${IMAGE_TAG}" \
    --cpu "$CPU" \
    --memory "$MEMORY" \
    --ports 8080 \
    --environment-variables SPRING_PROFILES_ACTIVE=production \
    --dns-name-label "$CONTAINER_NAME" \
    > /dev/null

print_success "Container instance created: $CONTAINER_NAME"

################################################################################
# Get Deployment Details
################################################################################

print_step "Retrieving deployment details..."

# Get container details
CONTAINER_INFO=$(az container show \
    --resource-group "$RESOURCE_GROUP" \
    --name "$CONTAINER_NAME" \
    --query "{FQDN:ipAddress.fqdn, IP:ipAddress.ip, Status:containers[0].instanceView.currentState.state}" \
    -o json)

FQDN=$(echo "$CONTAINER_INFO" | jq -r '.FQDN')
IP=$(echo "$CONTAINER_INFO" | jq -r '.IP')
STATUS=$(echo "$CONTAINER_INFO" | jq -r '.Status')

echo ""
print_success "Container Status: $STATUS"
print_success "FQDN: $FQDN"
print_success "IP Address: $IP"

################################################################################
# Wait for Container to Start
################################################################################

print_step "Waiting for container to start (up to 60 seconds)..."

HEALTH_CHECK_RETRIES=0
MAX_RETRIES=12
RETRY_INTERVAL=5

while [ $HEALTH_CHECK_RETRIES -lt $MAX_RETRIES ]; do
    if curl -sf "http://${FQDN}:8080/actuator/health" > /dev/null 2>&1; then
        print_success "Health check passed!"
        break
    fi

    HEALTH_CHECK_RETRIES=$((HEALTH_CHECK_RETRIES + 1))
    if [ $HEALTH_CHECK_RETRIES -lt $MAX_RETRIES ]; then
        print_info "Health check attempt $HEALTH_CHECK_RETRIES/$MAX_RETRIES (waiting ${RETRY_INTERVAL}s)..."
        sleep $RETRY_INTERVAL
    fi
done

if [ $HEALTH_CHECK_RETRIES -eq $MAX_RETRIES ]; then
    print_error "Container health check failed after 60 seconds"
    print_info "Container may still be starting. Check logs with:"
    echo "  az container logs --resource-group $RESOURCE_GROUP --name $CONTAINER_NAME --follow"
else
    print_success "Container is healthy!"
fi

################################################################################
# Display Results
################################################################################

print_header "Deployment Successful!"

echo "Application Details:"
echo "  Resource Group: $RESOURCE_GROUP"
echo "  Container Name: $CONTAINER_NAME"
echo "  Docker Image: ${DOCKER_USERNAME}/numguess-api:${IMAGE_TAG}"
echo "  Region: $REGION"
echo ""

echo "URLs:"
echo "  API Base: http://${FQDN}:8080"
echo "  Health Check: http://${FQDN}:8080/actuator/health"
echo "  Swagger UI: http://${FQDN}:8080/swagger-ui.html"
echo "  Games API: http://${FQDN}:8080/games"
echo ""

echo "Useful Commands:"
echo "  View logs:"
echo "    az container logs --resource-group $RESOURCE_GROUP --name $CONTAINER_NAME --follow"
echo ""
echo "  Get container details:"
echo "    az container show --resource-group $RESOURCE_GROUP --name $CONTAINER_NAME"
echo ""
echo "  Delete container:"
echo "    az container delete --resource-group $RESOURCE_GROUP --name $CONTAINER_NAME"
echo ""
echo "  Delete resource group:"
echo "    az group delete --name $RESOURCE_GROUP"
echo ""

echo "Testing the API:"
echo "  curl http://${FQDN}:8080/actuator/health"
echo "  curl -X POST http://${FQDN}:8080/games -H 'Content-Type: application/json' -d '{}'"
echo ""

print_success "Deployment complete!"
