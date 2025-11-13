#!/bin/bash

################################################################################
# Azure Deployment Script for Number Guessing Game API
#
# Usage:
#   ./deploy-to-azure.sh <app-name> [resource-group] [location] [pricing-tier]
#
# Examples:
#   ./deploy-to-azure.sh numguess-api                                    # Uses defaults
#   ./deploy-to-azure.sh numguess-api my-rg eastus B1                   # Custom parameters
#
# Prerequisites:
#   - Azure CLI installed (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
#   - Logged in to Azure: az login
#   - Maven installed
#
################################################################################

set -e

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
APP_NAME="${1:-numguess-api}"
RESOURCE_GROUP="${2:-numguess-rg}"
LOCATION="${3:-eastus}"
PRICING_TIER="${4:-F1}"
DEPLOYMENT_NAME="numguess-deployment-$(date +%s)"

echo -e "${BLUE}================================${NC}"
echo -e "${BLUE}Azure Deployment Configuration${NC}"
echo -e "${BLUE}================================${NC}"
echo "App Name:        $APP_NAME"
echo "Resource Group:  $RESOURCE_GROUP"
echo "Location:        $LOCATION"
echo "Pricing Tier:    $PRICING_TIER"
echo "Deployment ID:   $DEPLOYMENT_NAME"
echo ""

# Check prerequisites
echo -e "${YELLOW}Checking prerequisites...${NC}"

if ! command -v az &> /dev/null; then
    echo -e "${RED}❌ Azure CLI not found. Install it from: https://learn.microsoft.com/en-us/cli/azure/install-azure-cli${NC}"
    exit 1
fi

if ! command -v mvn &> /dev/null; then
    echo -e "${RED}❌ Maven not found. Please install Maven.${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Prerequisites OK${NC}"
echo ""

# Check Azure login
echo -e "${YELLOW}Verifying Azure authentication...${NC}"
if ! az account show &> /dev/null; then
    echo -e "${RED}❌ Not logged into Azure. Run: az login${NC}"
    exit 1
fi

SUBSCRIPTION=$(az account show --query name -o tsv)
echo -e "${GREEN}✓ Logged in to: $SUBSCRIPTION${NC}"
echo ""

# Create resource group if it doesn't exist
echo -e "${YELLOW}Setting up resource group...${NC}"
if az group exists --name "$RESOURCE_GROUP" | grep -q false; then
    echo "Creating resource group: $RESOURCE_GROUP"
    az group create --name "$RESOURCE_GROUP" --location "$LOCATION" > /dev/null
    echo -e "${GREEN}✓ Resource group created${NC}"
else
    echo -e "${GREEN}✓ Resource group already exists${NC}"
fi
echo ""

# Build the application
echo -e "${YELLOW}Building Maven project...${NC}"
cd numguess-service
mvn clean package -DskipTests -q
echo -e "${GREEN}✓ Build completed${NC}"
cd ..
echo ""

# Deploy ARM template
echo -e "${YELLOW}Deploying Azure infrastructure...${NC}"
az deployment group create \
    --resource-group "$RESOURCE_GROUP" \
    --template-file azure-app-service.json \
    --parameters appName="$APP_NAME" location="$LOCATION" pricingTier="$PRICING_TIER" \
    --no-wait \
    > /dev/null

echo -e "${GREEN}✓ Infrastructure deployment started${NC}"
echo ""

# Deploy application using Maven plugin
echo -e "${YELLOW}Deploying Spring Boot application...${NC}"
cd numguess-service
mvn azure-webapp:deploy \
    -Dazure.subscriptionId="$(az account show --query id -o tsv)" \
    -Dazure.resourceGroup="$RESOURCE_GROUP" \
    -Dazure.appName="$APP_NAME" \
    -q

echo -e "${GREEN}✓ Application deployment completed${NC}"
cd ..
echo ""

# Get the application URL
echo -e "${YELLOW}Retrieving application details...${NC}"
APP_URL="https://${APP_NAME}.azurewebsites.net"
echo ""

echo -e "${BLUE}================================${NC}"
echo -e "${GREEN}Deployment Successful!${NC}"
echo -e "${BLUE}================================${NC}"
echo ""
echo "Application URL: $APP_URL"
echo "Resource Group:  $RESOURCE_GROUP"
echo "App Name:        $APP_NAME"
echo ""
echo -e "${YELLOW}Useful commands:${NC}"
echo "  View logs:      az webapp log tail --resource-group $RESOURCE_GROUP --name $APP_NAME"
echo "  Stop app:       az webapp stop --resource-group $RESOURCE_GROUP --name $APP_NAME"
echo "  Start app:      az webapp start --resource-group $RESOURCE_GROUP --name $APP_NAME"
echo "  Delete app:     az group delete --name $RESOURCE_GROUP"
echo ""
echo "Testing the API:"
echo "  Swagger UI:     $APP_URL/swagger-ui.html"
echo "  Health Check:   $APP_URL/actuator/health"
echo "  API Docs:       $APP_URL/v3/api-docs"
echo ""
