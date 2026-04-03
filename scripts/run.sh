#!/bin/bash

# Navigate up one level to the project root directory so 'src' and 'bin' paths always work
cd "$(dirname "$0")/.." || exit

echo "Compiling project..."

mkdir -p bin

# Compile all java files from src into bin
javac -d bin $(find src -name "*.java")

# Check if compilation was successful before trying to run
if [ $? -eq 0 ]; then
    echo "Running proxy server..."
    java -cp bin app.ProxyServer
else
    echo "Compilation failed. Server will not start."
fi