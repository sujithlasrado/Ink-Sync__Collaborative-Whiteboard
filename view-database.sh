#!/bin/bash

echo "Compiling and running database viewer..."
javac -cp "lib/*:src" -d src src/server/DatabaseViewer.java
java -cp "lib/*:src" server.DatabaseViewer 