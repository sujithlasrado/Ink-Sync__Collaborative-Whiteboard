#!/bin/bash
# Change directory to the script's parent directory (project root)
cd "$(dirname "$0")/.."

echo "Compiling project..."
javac -d src -cp "lib/*:src" src/server/*.java src/client/*.java src/command/*.java

# Run server with GUI
echo "Starting server..."
java -cp "lib/*:src" server.Server &
echo "Server started! Use 'scripts/stop-server.sh' to stop it." 