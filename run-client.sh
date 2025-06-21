#!/bin/bash
cd /Users/levi/Ink-Sync
echo "Compiling project..."
javac -cp "src:lib/junit-4.13.2.jar:lib/hamcrest-2.2.jar" -d bin src/**/*.java
echo "Starting client..."
java -cp "bin:src" client.Client 