#!/bin/bash

if pgrep -f "java.*server.Server" > /dev/null; then
    echo "✅ Server is RUNNING"
else
    echo "❌ Server is NOT running"
fi 