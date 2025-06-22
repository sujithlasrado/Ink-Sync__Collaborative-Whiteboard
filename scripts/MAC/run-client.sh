#!/bin/bash
# Change directory to the script's parent directory (project root)
cd "$(dirname "$0")/../.."

echo "Compiling project..."
javac -cp "lib/*:src" -d bin src/server/*.java src/client/*.java src/command/*.java

echo "Starting client..."
echo "Note: Make sure the server is running first with ./MAC/run-server.sh"
java -cp "lib/*:bin:src" client.Client 