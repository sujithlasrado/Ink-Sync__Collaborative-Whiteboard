#!/bin/bash
# Change directory to the script's parent directory (project root)
cd "$(dirname "$0")/.."

echo "Compiling and running database viewer..."
java -cp "lib/*:src" server.DatabaseViewer 