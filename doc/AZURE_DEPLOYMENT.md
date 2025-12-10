# Azure Deployment Guide

This guide covers deploying the Number Guessing Game API to Microsoft Azure using multiple deployment options.

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Deployment Options](#deployment-options)
3. [Option 1: Quick Script Deployment](#option-1-quick-script-deployment)
4. [Option 2: Maven Plugin Deployment](#option-2-maven-plugin-deployment)
5. [Option 3: Docker Container Deployment](#option-3-docker-container-deployment) ⭐ **Recommended**
6. [Option 3a: Azure Container Instances](#option-3a-azure-container-instances)
7. [Option 4: Infrastructure as Code (IaC)](#option-4-infrastructure-as-code-iac)
8. [Post-Deployment Configuration](#post-deployment-configuration)
9. [Monitoring and Troubleshooting](#monitoring-and-troubleshooting)
10. [Cleanup](#cleanup)

---

## Prerequisites

### Required Tools

1. **Azure CLI** (2.50+)
   ```bash
   # Install: https://learn.microsoft.com/en-us/cli/azure/install-azure-cli
   az --version
   ```

2. **Maven** (3.6+)
   ```bash
   mvn --version
   ```

3. **Git**
   ```bash
   git --version
   ```

### Azure Subscription

- Active Azure subscription
- Permissions to create resources (App Service, Resource Groups)
- Free tier available: https://azure.microsoft.com/en-us/free/

### Azure Login

```bash
az login
# Browser opens for authentication
# Verify with:
az account show
```

---

## Deployment Options

| Option | Complexity | Speed | Cost | Best For |
|--------|-----------|-------|------|----------|
| Quick Script | ⭐ Low | 🚀 Fast | 💰 Free-B1 | Learning, demos |
| Maven Plugin | ⭐⭐ Low-Mid | 🚀 Fast | 💰 Free-B1 | Java developers |
| Docker (App Service) | ⭐⭐⭐ Mid-High | 📦 Medium | 💰 B1-S1 | Production with scaling |
| **Container Instances** | **⭐⭐ Low-Mid** | **🚀 Fast** | **💰 ~$10/mo** | **Students, lightweight apps** ⭐ |
| Infrastructure as Code | ⭐⭐⭐⭐ High | 🏗️ Slow | 💰 Flexible | Enterprise |

---

## Option 1: Quick Script Deployment

**Easiest way to get started!**

### Usage

```bash
# Basic deployment (uses defaults)
./deploy-to-azure.sh numguess-api

# Custom configuration
./deploy-to-azure.sh numguess-api my-resource-group eastus B1

# Parameters:
#   1. app-name (required)      : Must be globally unique
#   2. resource-group (optional): Default: numguess-rg
#   3. location (optional)      : Default: eastus
#   4. pricing-tier (optional)  : Default: F1 (Free)
```

### What the Script Does

1. ✅ Validates prerequisites (Azure CLI, Maven)
2. ✅ Verifies Azure login
3. ✅ Creates resource group
4. ✅ Builds application with Maven
5. ✅ Deploys infrastructure (App Service Plan, App Service)
6. ✅ Deploys application JAR
7. ✅ Outputs application URL

### Output Example

```
================================
Azure Deployment Configuration
================================
App Name:        numguess-api
Resource Group:  numguess-rg
Location:        eastus
Pricing Tier:    F1

✓ Prerequisites OK
✓ Logged in to: My Subscription
✓ Resource group already exists
✓ Build completed
✓ Infrastructure deployment started
✓ Application deployment completed

================================
Deployment Successful!
================================

Application URL: https://numguess-api.azurewebsites.net
Resource Group:  numguess-rg
App Name:        numguess-api

Useful commands:
  View logs:      az webapp log tail --resource-group numguess-rg --name numguess-api
  Stop app:       az webapp stop --resource-group numguess-rg --name numguess-api
  Start app:      az webapp start --resource-group numguess-rg --name numguess-api
  Delete app:     az group delete --name numguess-rg
```

---

## Option 2: Maven Plugin Deployment

**Direct deployment using Maven.**

### Prerequisites

Set Azure properties in environment or Maven:

```bash
# Environment variables
export AZURE_SUBSCRIPTION_ID="your-subscription-id"
export AZURE_RESOURCE_GROUP="numguess-rg"
export AZURE_APP_NAME="numguess-api"
```

Or in `~/.m2/settings.xml`:

```xml
<servers>
  <server>
    <id>azure-auth</id>
    <username>your-azure-username</username>
    <password>your-azure-password-or-token</password>
  </server>
</servers>
```

### Build and Deploy

```bash
cd numguess-service

# Build the JAR
mvn clean package -DskipTests

# Deploy to Azure
mvn azure-webapp:deploy \
  -Dazure.subscriptionId="your-subscription-id" \
  -Dazure.resourceGroup="numguess-rg" \
  -Dazure.appName="numguess-api"
```

### Maven Plugin Configuration

The `azure-webapp-maven-plugin` is configured in `pom.xml`:

```xml
<plugin>
  <groupId>com.microsoft.azure</groupId>
  <artifactId>azure-webapp-maven-plugin</artifactId>
  <version>2.13.0</version>
  <configuration>
    <runtime>
      <os>linux</os>
      <javaVersion>25</javaVersion>
      <webContainer>java</webContainer>
    </runtime>
    <region>eastus</region>
    <pricingTier>F1</pricingTier>
    <!-- ... more configuration ... -->
  </configuration>
</plugin>
```

### Useful Maven Commands

```bash
# List resource groups
mvn azure-webapp:list

# Get deployment details
mvn azure-webapp:show

# Update app settings
mvn azure-webapp:config

# View streaming logs
mvn azure-webapp:tail-log
```

---

## Option 3: Docker Container Deployment

**Best for containerized deployments and CI/CD.**

### Build Docker Image

```bash
# From project root
docker build -t numguess-api:1.0.0 ./numguess-service

# View image
docker images | grep numguess
```

### Run Locally (Test)

```bash
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=development \
  numguess-api:1.0.0

# Test
curl http://localhost:8080/actuator/health
```

### Push to Azure Container Registry

```bash
# Create container registry
az acr create --resource-group numguess-rg \
  --name numguessregistry --sku Basic

# Login to registry
az acr login --name numguessregistry

# Tag image
docker tag numguess-api:1.0.0 \
  numguessregistry.azurecr.io/numguess-api:1.0.0

# Push image
docker push numguessregistry.azurecr.io/numguess-api:1.0.0

# Deploy to App Service
az webapp create --resource-group numguess-rg \
  --plan numguess-plan \
  --name numguess-api \
  --deployment-container-image-name \
  numguessregistry.azurecr.io/numguess-api:1.0.0
```

### Docker Build Arguments

The Dockerfile includes:

- **Multi-stage build**: Reduces final image size
- **Alpine base**: Minimal JRE (25-jre-alpine)
- **Non-root user**: Security best practice
- **Health check**: Docker health monitoring
- **Spring profiles support**: Different environments

---

## Option 3a: Azure Container Instances

**Simplest container deployment for lightweight apps.** ⭐ **Recommended for student subscriptions**

Azure Container Instances (ACI) provides serverless container execution without needing to manage infrastructure. This option works around subscription limitations by using Docker Hub or GitHub Container Registry instead of Azure Container Registry.

### Prerequisites

- Docker installed locally
- Docker Hub account (free) OR GitHub account (for ghcr.io)
- Azure CLI 2.50+
- Azure subscription with ACI enabled

### Quick Start (5 minutes)

```bash
# 1. Build Docker image locally
cd numguess-service
docker build -t numguess-api:1.0.0 .

# 2. Tag for Docker Hub (replace 'yourusername')
docker tag numguess-api:1.0.0 yourusername/numguess-api:1.0.0

# 3. Push to Docker Hub
docker login
docker push yourusername/numguess-api:1.0.0

# 4. Deploy to Azure Container Instances
az container create \
  --resource-group numguess-rg \
  --name numguess-api \
  --image yourusername/numguess-api:1.0.0 \
  --ports 8080 \
  --cpu 1 \
  --memory 1 \
  --environment-variables SPRING_PROFILES_ACTIVE=production

# 5. Get the public IP
az container show --resource-group numguess-rg \
  --name numguess-api \
  --query ipAddress.ip --output tsv
```

**Application will be available at:** `http://<IP_ADDRESS>:8080`

### Detailed Deployment

#### Step 1: Build Docker Image Locally

```bash
# From project root
cd numguess-service
docker build -t numguess-api:1.0.0 .

# Verify image was created
docker images | grep numguess-api
```

**Output example:**
```
REPOSITORY       TAG      IMAGE ID      SIZE
numguess-api     1.0.0    a1b2c3d4e5f6  185MB
```

#### Step 2: Push to Docker Hub (Recommended for simplicity)

If Docker Hub is not available, skip to "Alternative: GitHub Container Registry" section.

```bash
# 1. Create free Docker Hub account at https://hub.docker.com/

# 2. Login to Docker Hub
docker login
# Enter your Docker Hub username and password

# 3. Tag image with your Docker Hub username
docker tag numguess-api:1.0.0 YOUR_DOCKER_USERNAME/numguess-api:1.0.0

# 4. Push image
docker push YOUR_DOCKER_USERNAME/numguess-api:1.0.0

# 5. Verify image is public
# Visit https://hub.docker.com/r/YOUR_DOCKER_USERNAME/numguess-api
```

#### Step 2 (Alternative): GitHub Container Registry

If using GitHub instead of Docker Hub:

```bash
# 1. Create GitHub Personal Access Token
# Go to Settings → Developer settings → Personal access tokens → Generate new token
# Select: write:packages, read:packages
# Copy the token

# 2. Login to GitHub Container Registry
echo "YOUR_GITHUB_TOKEN" | docker login ghcr.io -u YOUR_GITHUB_USERNAME --password-stdin

# 3. Tag image for ghcr.io
docker tag numguess-api:1.0.0 ghcr.io/YOUR_GITHUB_USERNAME/numguess-api:1.0.0

# 4. Push image
docker push ghcr.io/YOUR_GITHUB_USERNAME/numguess-api:1.0.0

# 5. Make image public (optional)
# Go to Settings → Packages and libraries → Container Registry Settings
```

#### Step 3: Deploy to Azure Container Instances

```bash
# Ensure you're logged in to Azure
az login
az account show

# Create resource group (if not exists)
az group create --name numguess-rg --location eastus

# Deploy container instance
az container create \
  --resource-group numguess-rg \
  --name numguess-api \
  --image YOUR_DOCKER_USERNAME/numguess-api:1.0.0 \
  --ports 8080 \
  --cpu 1 \
  --memory 1 \
  --environment-variables SPRING_PROFILES_ACTIVE=production \
  --dns-name-label numguess-api-app

# Get deployment details
az container show \
  --resource-group numguess-rg \
  --name numguess-api \
  --query "{FQDN:ipAddress.fqdn, IP:ipAddress.ip, Status:containers[0].instanceView.currentState.state}"
```

**For GitHub Container Registry, use:**
```bash
az container create \
  --resource-group numguess-rg \
  --name numguess-api \
  --image ghcr.io/YOUR_GITHUB_USERNAME/numguess-api:1.0.0 \
  --registry-login-server ghcr.io \
  --registry-username YOUR_GITHUB_USERNAME \
  --registry-password YOUR_GITHUB_TOKEN \
  --ports 8080 \
  --cpu 1 \
  --memory 1 \
  --environment-variables SPRING_PROFILES_ACTIVE=production \
  --dns-name-label numguess-api-app
```

### Testing Deployment

```bash
# Get FQDN
FQDN=$(az container show --resource-group numguess-rg \
  --name numguess-api \
  --query ipAddress.fqdn --output tsv)

# Health check
curl http://$FQDN:8080/actuator/health

# Access Swagger UI
echo "http://$FQDN:8080/swagger-ui.html"

# Create a game
curl -X POST http://$FQDN:8080/games \
  -H "Content-Type: application/json" \
  -d '{}' | jq .
```

### Container Resources

#### CPU and Memory Options

| CPU | Memory | Typical Use |
|-----|--------|-------------|
| 0.5 | 0.5 GB | Dev/test (lighter) |
| 1.0 | 1.0 GB | **Recommended** |
| 1.5 | 2.0 GB | Production light |
| 2.0 | 3.5 GB | Production medium |
| 4.0 | 7.0 GB | Production heavy |

**For this Spring Boot app, 1 CPU / 1 GB memory is sufficient.**

#### Environment Variables

Configure via `--environment-variables`:

```bash
az container create \
  --resource-group numguess-rg \
  --name numguess-api \
  --image yourusername/numguess-api:1.0.0 \
  --environment-variables \
    SPRING_PROFILES_ACTIVE=production \
    JAVA_OPTS="-Xmx512m -Xms256m"
```

### Monitoring Container Instances

```bash
# View container status
az container show \
  --resource-group numguess-rg \
  --name numguess-api

# Stream container logs
az container logs \
  --resource-group numguess-rg \
  --name numguess-api \
  --follow

# Get container events
az container attach \
  --resource-group numguess-rg \
  --name numguess-api
```

### Updating Deployment

To deploy a new version:

```bash
# 1. Build and push new version
docker build -t yourusername/numguess-api:1.0.1 ./numguess-service
docker push yourusername/numguess-api:1.0.1

# 2. Delete old container
az container delete \
  --resource-group numguess-rg \
  --name numguess-api \
  --yes

# 3. Deploy new container
az container create \
  --resource-group numguess-rg \
  --name numguess-api \
  --image yourusername/numguess-api:1.0.1 \
  --ports 8080 \
  --cpu 1 \
  --memory 1 \
  --environment-variables SPRING_PROFILES_ACTIVE=production \
  --dns-name-label numguess-api-app
```

### Cost Estimation

Azure Container Instances pricing (as of 2024):

| Configuration | Est. Monthly Cost |
|---------------|-------------------|
| 0.5 CPU, 0.5 GB | ~$3 |
| 1.0 CPU, 1.0 GB | ~$10 |
| 2.0 CPU, 3.5 GB | ~$25 |

**Note:** Based on 730 hours/month. Charges only when container is running.

### Troubleshooting

#### Container won't start

```bash
# Check logs
az container logs --resource-group numguess-rg --name numguess-api

# Check events
az container show --resource-group numguess-rg --name numguess-api \
  --query "containers[0].instanceView.events"
```

#### Health check failing

Ensure `/actuator/health` endpoint is responding:

```bash
# Get FQDN
FQDN=$(az container show --resource-group numguess-rg \
  --name numguess-api --query ipAddress.fqdn --output tsv)

# Test directly
curl -v http://$FQDN:8080/actuator/health
```

#### Image not found

Verify image is public or credentials are set:

```bash
# Test image locally
docker pull yourusername/numguess-api:1.0.0
docker run -p 8080:8080 yourusername/numguess-api:1.0.0
```

### Cleanup

```bash
# Delete container instance
az container delete \
  --resource-group numguess-rg \
  --name numguess-api \
  --yes

# Delete resource group (removes all resources)
az group delete --name numguess-rg --yes
```

### Why Container Instances for This Project

✅ **Advantages:**
- Simple, straightforward Docker deployment
- No need for Azure Container Registry (works with Docker Hub/GitHub)
- Lower cost than App Service for lightweight apps
- Automatic resource scaling
- Pay-per-second billing
- Works great with student subscriptions

✅ **Works around subscription limitations:**
- Azure for Students doesn't have Container Registry namespace
- Docker Hub/GitHub Container Registry provide free alternatives
- No additional Azure service registration needed

❌ **When NOT to use:**
- Need persistent storage
- Continuous availability required 24/7
- Multiple instances needed
- Complex networking requirements

---

## Option 4: Infrastructure as Code (IaC)

**Production-grade deployment with ARM templates.**

### Deploy Using ARM Template

```bash
# Validate template
az deployment group validate \
  --resource-group numguess-rg \
  --template-file azure-app-service.json \
  --parameters appName=numguess-api location=eastus pricingTier=B1

# Deploy
az deployment group create \
  --resource-group numguess-rg \
  --template-file azure-app-service.json \
  --parameters appName=numguess-api location=eastus pricingTier=B1

# Check deployment status
az deployment group show \
  --resource-group numguess-rg \
  --name azure-app-service
```

### Template Parameters

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `appName` | string | | Globally unique app name (1-64 chars) |
| `location` | string | eastus | Azure region (eastus, westus, etc) |
| `pricingTier` | string | F1 | F1=Free, B1=Basic, S1=Standard |

### Template Resources Created

1. **App Service Plan** (`Microsoft.Web/serverfarms`)
   - Linux-based
   - Scalable compute resources
   - Runtime: Java 25

2. **App Service** (`Microsoft.Web/sites`)
   - Web application hosting
   - System-assigned managed identity
   - Health checks configured
   - Logging enabled

3. **Configuration**
   - Environment variables
   - Java options (-Xmx512m)
   - Spring profiles (production)
   - HTTP/2 enabled
   - TLS 1.2 minimum

---

## Post-Deployment Configuration

### Environment Variables

Set via Azure Portal or CLI:

```bash
az webapp config appsettings set \
  --resource-group numguess-rg \
  --name numguess-api \
  --settings PORT=8080 \
    SPRING_PROFILES_ACTIVE=production \
    JAVA_OPTS="-Xmx512m -Xms256m"
```

### Application Properties

Update `application.properties` for Azure:

```properties
# Detected from PORT environment variable
server.port=${PORT:8080}

# Spring profiles
spring.profiles.active=production

# Actuator for health checks
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=when-authorized
```

### Logging Configuration

```bash
# Enable application logging
az webapp log config --resource-group numguess-rg \
  --name numguess-api \
  --application-logging filesystem \
  --detailed-error-messages true \
  --failed-request-tracing true

# Tail logs in real-time
az webapp log tail --resource-group numguess-rg \
  --name numguess-api \
  --follow
```

### SSL/TLS Certificates

```bash
# Azure manages HTTPS automatically for *.azurewebsites.net domain

# To use custom domain:
az webapp config hostname add --webapp-name numguess-api \
  --resource-group numguess-rg \
  --hostname api.example.com
```

---

## Monitoring and Troubleshooting

### Health Checks

```bash
# Verify application is running
curl https://numguess-api.azurewebsites.net/actuator/health

# Response example:
# {"status":"UP","components":{"db":{"status":"UP"}}}
```

### View Logs

```bash
# Stream logs
az webapp log tail --resource-group numguess-rg --name numguess-api

# Download logs
az webapp log download --resource-group numguess-rg \
  --name numguess-api --log-file logs.zip

# View specific logs
az webapp log show --resource-group numguess-rg --name numguess-api
```

### Monitor Metrics

```bash
# CPU usage
az monitor metrics list --resource numguess-api \
  --resource-group numguess-rg \
  --metric CpuPercentage

# Memory usage
az monitor metrics list --resource numguess-api \
  --resource-group numguess-rg \
  --metric MemoryPercentage

# Request count
az monitor metrics list --resource numguess-api \
  --resource-group numguess-rg \
  --metric Requests
```

### Common Issues

#### Application fails to start

```bash
# Check logs
az webapp log tail --resource-group numguess-rg --name numguess-api

# Restart application
az webapp restart --resource-group numguess-rg --name numguess-api

# Check process
az webapp remote-debugging start --resource-group numguess-rg \
  --name numguess-api
```

#### Port binding issues

Ensure `server.port` is set from `PORT` environment variable:

```properties
# In application.properties
server.port=${PORT:8080}
```

#### Memory issues

Increase App Service Plan tier or adjust JVM options:

```bash
# Scale up
az appservice plan update --name numguess-plan \
  --resource-group numguess-rg \
  --sku B1  # Or higher: B2, S1, etc

# Adjust JVM memory
az webapp config appsettings set \
  --resource-group numguess-rg \
  --name numguess-api \
  --settings JAVA_OPTS="-Xmx1024m -Xms512m"
```

---

## Cleanup

### Delete Application

```bash
# Delete resource group (removes all resources)
az group delete --resource-group numguess-rg --yes

# Or delete individual resources
az webapp delete --resource-group numguess-rg --name numguess-api
az appservice plan delete --resource-group numguess-rg \
  --name numguess-plan --yes
```

### Cost Optimization

- **Free tier (F1)**: 60 minutes/day (perfect for testing)
- **Shared tier (D1)**: Pay per day (~$9/month)
- **Basic tier (B1)**: Pay per hour (~$12/month)
- **Standard tier (S1)**: For production (~$50/month+)

### Monitor Costs

```bash
# View resource costs
az costmanagement query --timeperiod-from 2024-01-01 \
  --timeperiod-to 2024-01-31 \
  --filter "{ \"dimensions\": { \"name\": \"ResourceGroup\", \"operator\": \"In\", \"values\": [\"numguess-rg\"] } }"
```

---

## API Testing

Once deployed, test your API:

```bash
APP_URL="https://numguess-api.azurewebsites.net"

# Health check
curl $APP_URL/actuator/health

# Swagger UI
open $APP_URL/swagger-ui.html

# API documentation
curl $APP_URL/v3/api-docs | jq .

# Create a game
curl -X POST $APP_URL/games \
  -H "Content-Type: application/json" \
  -d '{}' | jq .
```

---

## Continuous Deployment

For automatic deployments on git push:

```bash
# Enable continuous deployment from GitHub
az webapp deployment github-actions add \
  --repo <your-github-repo> \
  --branch main \
  --resource-group numguess-rg \
  --name numguess-api \
  --runtime "java|25"
```

This automatically:
1. Builds on push to main
2. Tests with Maven
3. Deploys if tests pass
4. Updates Azure App Service

---

## Next Steps

- [Azure App Service Documentation](https://docs.microsoft.com/en-us/azure/app-service/)
- [Spring Boot on Azure](https://docs.microsoft.com/en-us/azure/developer/java/spring-framework/)
- [Azure CLI Reference](https://docs.microsoft.com/en-us/cli/azure/reference-index)
- [Azure cost calculator](https://azure.microsoft.com/en-us/pricing/calculator/)

---

## Support

For issues or questions:
1. Check Azure Portal for resource health
2. Review application logs: `az webapp log tail ...`
3. Verify environment variables are set correctly
4. Test locally with Docker before deploying
5. Check Azure status page: https://status.azure.com/
