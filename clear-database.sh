#!/bin/bash

echo "🗑️  Clearing Ink-Sync Database Contents"
echo "======================================"
echo ""

echo "Compiling and running database clearer..."
java -cp "lib/*:src" server.DatabaseViewer clear

echo ""
echo "✅ Database cleared successfully!"
echo "All server sessions and user activity records have been removed." 