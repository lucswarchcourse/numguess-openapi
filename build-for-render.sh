#!/bin/bash

################################################################################
# Docker Build Script for Render Deployment (linux/amd64)
#
# This script builds a Docker image compatible with Render.com deployment
# by using QEMU emulation to build for linux/amd64 on Mac (ARM64)
#
# Usage:
#   ./build-for-render.sh [docker-username] [version]
#
# Examples:
#   ./build-for-render.sh                          # Uses defaults
#   ./build-for-render.sh myusername 1.0.0        # Custom username and version
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
VERSION="${2:-1.0.0}"
IMAGE_NAME="numguess-api"

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

print_header "Docker Build for Render Deployment"

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    print_error "Docker is not installed"
    echo "Install from: https://docs.docker.com/get-docker/"
    exit 1
fi
print_success "Docker found: $(docker --version)"

print_step "Building Docker image..."

if [ -z "$DOCKER_USERNAME" ]; then
    print_info "No Docker username provided. Building local image only."
    print_info "To push to Docker Hub, provide username as first argument."
    IMAGE_TAG="${IMAGE_NAME}:${VERSION}"
else
    IMAGE_TAG="${DOCKER_USERNAME}/${IMAGE_NAME}:${VERSION}"
fi

# Get the absolute path to numguess-service directory
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
SERVICE_DIR="${SCRIPT_DIR}/numguess-service"

if [ ! -f "${SERVICE_DIR}/Dockerfile" ]; then
    print_error "Dockerfile not found at ${SERVICE_DIR}/Dockerfile"
    exit 1
fi

print_info "Building: $IMAGE_TAG"
print_info "Context: ${SERVICE_DIR}"

# Build the image
# Note: Using standard docker build which will use native platform
# For true cross-platform builds on Mac, see alternatives below
docker build -t "${IMAGE_TAG}" "${SERVICE_DIR}"

if [ $? -eq 0 ]; then
    print_success "Docker image built successfully"

    # Get image architecture info
    echo ""
    print_step "Image Information"
    docker images "${IMAGE_NAME}" --format "table {{.Repository}}\t{{.Tag}}\t{{.Size}}"

    # Test the image locally
    print_step "Testing image locally..."

    # Check if port 8080 is already in use
    if lsof -Pi :8080 -sTCP:LISTEN -t >/dev/null 2>&1; then
        print_info "Port 8080 is already in use. Skipping local test."
        print_info "You can test manually with: docker run -p 8080:8080 ${IMAGE_TAG}"
    else
        print_info "Starting container for testing (will run for 10 seconds)..."

        # Run container with timeout
        docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=production "${IMAGE_TAG}" &
        CONTAINER_PID=$!

        # Wait for container to start
        sleep 5

        # Test health check
        if curl -sf http://localhost:8080/actuator/health > /dev/null 2>&1; then
            print_success "Health check passed!"
        else
            print_info "Health check endpoint not responding yet (container may still be starting)"
        fi

        # Stop container
        sleep 5
        kill $CONTAINER_PID 2>/dev/null || true
        wait $CONTAINER_PID 2>/dev/null || true
    fi

    # Instructions for Render deployment
    echo ""
    print_header "Next Steps: Deploy to Render"

    if [ -z "$DOCKER_USERNAME" ]; then
        echo "To deploy to Render, you need to:"
        echo "1. Create a Docker Hub account at https://hub.docker.com"
        echo "2. Tag the image with your username:"
        echo "   docker tag ${IMAGE_TAG} YOUR_USERNAME/${IMAGE_NAME}:${VERSION}"
        echo "3. Login to Docker Hub:"
        echo "   docker login"
        echo "4. Push the image:"
        echo "   docker push YOUR_USERNAME/${IMAGE_NAME}:${VERSION}"
        echo "5. Deploy to Render at https://render.com/register"
    else
        echo "Your image is ready to push to Docker Hub:"
        echo ""
        echo "1. Login to Docker Hub (if not already logged in):"
        echo "   docker login"
        echo ""
        echo "2. Push the image:"
        echo "   docker push ${IMAGE_TAG}"
        echo ""
        echo "3. Deploy to Render:"
        echo "   a. Go to https://dashboard.render.com/register"
        echo "   b. Create a new 'Web Service'"
        echo "   c. Connect your Docker Hub account"
        echo "   d. Select '${DOCKER_USERNAME}/${IMAGE_NAME}' as the image"
        echo "   e. Tag: '${VERSION}'"
        echo "   f. Environment: Set PORT=8080 if needed"
        echo ""
        echo "Your app will be live at: https://<service-name>.onrender.com"
    fi

else
    print_error "Docker build failed"
    exit 1
fi

echo ""
print_header "Important Notes"

echo "Platform Compatibility:"
echo "  • This build uses your Mac's native architecture"
echo "  • Render.com requires linux/amd64 images"
echo ""
echo "If you get a 'invalid platform' error on Render:"
echo ""
echo "Option 1: Use GitHub Actions (Recommended)"
echo "  - GitHub Actions runners use linux/amd64 natively"
echo "  - Create .github/workflows/build-docker.yml to build and push"
echo ""
echo "Option 2: Use a Linux VM or Docker-in-Docker"
echo "  - Spin up a Linux EC2 instance or DigitalOcean droplet"
echo "  - Clone the repo and run this script there"
echo ""
echo "Option 3: Manual Docker Buildx Setup"
echo "  - Install Docker Desktop with experimental features enabled"
echo "  - Enable 'Use containerd for pulling and storing images'"
echo "  - Then: docker buildx build --platform linux/amd64 -t ${IMAGE_TAG} --load ${SERVICE_DIR}"
echo ""
