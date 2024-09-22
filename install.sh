#!/bin/sh
echo "Starting the installation script..."
set -e
set -x

echo "Starting the installation script..."

cd "$(dirname "$0")"
echo "Changed directory to script location: $(pwd)"

cd src/main/frontend
echo "Changed directory to frontend: $(pwd)"


echo "Installing frontend dependencies..."
npm install
echo "Installed frontend dependencies."


echo "Building Angular project..."
ng build --base-href http://localhost:8080/admin/ --output-hashing none
echo "Built Angular project (Complete)."

cd "$(dirname "$0")"
echo "Changed directory back to script location: $(pwd)"

echo "Removing old static files..."
rm -rf src/main/resources/static/*
echo "Copying new Angular build files to static directory..."
cp -r src/main/frontend/dist/browser/* src/main/resources/static/


echo "Building backend project..."
./gradlew build

echo "Installation and build completed successfully."