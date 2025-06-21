#!/bin/bash
echo "Looking for running server process..."

# Method 1: Try to kill by process name (graceful)
pkill -f "java.*server.Server"
if [ $? -eq 0 ]; then
    echo "Server stopped successfully using pkill!"
    exit 0
fi

# Method 2: Try alternative approach
pkill -f "server.Server"
if [ $? -eq 0 ]; then
    echo "Server stopped successfully using alternative method!"
    exit 0
fi

# Method 3: Force kill if graceful didn't work
pkill -9 -f "java.*server.Server"
if [ $? -eq 0 ]; then
    echo "Server force stopped successfully!"
    exit 0
fi

# Method 4: Try to find and kill by port (if server runs on specific port)
lsof -ti:4444 | xargs kill -9 2>/dev/null
if [ $? -eq 0 ]; then
    echo "Server stopped successfully by killing port 4444!"
    exit 0
fi

echo "No server process found running." 