#!/bin/bash

cd ./frontend/FXCalculatorAdmin/ || echo "project directory not found" exit

#install node modules
npm install

#build angular project
ng build --base-href http://localhost:8080/admin/ --output-hashing none

#clean directory
rm -rf ./src/main/resources/static/admin/*

# Copy
cp -r  dist/browser/* ./src/main/resources/static/admin/

echo "Build and copy completed successfully."