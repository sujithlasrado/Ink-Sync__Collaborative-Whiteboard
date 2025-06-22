#!/bin/bash
# Change directory to the script's parent directory (project root)
cd "$(dirname "$0")/.."

if pgrep -f "java.*server.Server" > /dev/null; then
    echo "✅ Server is RUNNING"
else
    echo "❌ Server is NOT running"
fi 