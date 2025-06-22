#!/bin/bash
# Change directory to the script's parent directory (project root)
cd "$(dirname "$0")/../.."

echo "Looking for running server process..."

# Check if server is running by looking for the process
if pgrep -f "java.*server.Server" > /dev/null; then
    echo "Found running server. Attempting graceful shutdown..."
    
    # Method 1: Try to kill by process name (graceful)
    pkill -f "java.*server.Server"
    if [ $? -eq 0 ]; then
        echo "✅ Server stopped successfully!"
        exit 0
    fi
    
    # Method 2: Try alternative approach
    pkill -f "server.Server"
    if [ $? -eq 0 ]; then
        echo "✅ Server stopped successfully using alternative method!"
        exit 0
    fi
    
    # Method 3: Force kill if graceful didn't work
    echo "Graceful shutdown failed. Force stopping server..."
    pkill -9 -f "java.*server.Server"
    if [ $? -eq 0 ]; then
        echo "✅ Server force stopped successfully!"
        exit 0
    fi
else
    echo "❌ No server process found running."
    exit 1
fi

echo "❌ Failed to stop server. You may need to manually kill the process."
exit 1 