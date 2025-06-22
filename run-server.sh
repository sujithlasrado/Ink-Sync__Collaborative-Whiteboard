#!/bin/bash
cd /Users/levi/Ink-Sync
echo "Compiling project..."
javac -cp "lib/*:src" -d bin src/**/*.java
echo "Starting server in background..."
java -cp "lib/*:bin:src" server.Server &
echo "Server started! Use './stop-server.sh' to stop it." 