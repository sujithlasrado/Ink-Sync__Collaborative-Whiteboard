#!/bin/bash
# Change directory to the script's parent directory (project root)
cd "$(dirname "$0")/../.."

echo "Compiling project..."
javac -cp "lib/*:src" -d bin src/server/*.java src/client/*.java src/command/*.java

# Run server with GUI in background
echo "Starting server..."
java -cp "lib/*:bin:src" server.Server &
SERVER_PID=$!

# Wait a moment to see if server starts successfully
sleep 2

# Check if server process is still running
if kill -0 $SERVER_PID 2>/dev/null; then
    echo "Server started successfully! Use 'MAC/stop-server.sh' to stop it."
    echo "Server PID: $SERVER_PID"
else
    echo "Failed to start server. Check the error messages above."
    exit 1
fi 