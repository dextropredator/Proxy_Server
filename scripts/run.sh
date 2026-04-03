#!/bin/bash

echo "Compiling project..."

mkdir -p bin

# compile all java files
javac -d bin $(find src -name "*.java")

echo "Running proxy server..."

java -cp bin app.ProxyServer