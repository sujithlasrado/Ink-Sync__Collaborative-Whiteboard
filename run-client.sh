#!/bin/bash
cd /Users/levi/Ink-Sync
echo "Compiling project..."
javac -cp "lib/*:src" -d bin src/**/*.java
echo "Starting client..."
echo "Note: Make sure the server is running first with ./run-server.sh"
java -cp "lib/*:bin:src" client.Client 