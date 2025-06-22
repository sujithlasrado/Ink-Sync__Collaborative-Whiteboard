#!/bin/bash
# Change directory to the script's parent directory (project root)
cd "$(dirname "$0")/.."

echo "🗑️  Clearing Ink-Sync Database Contents"
echo "======================================"
echo ""

echo "Compiling and running database clearer..."
java -cp "lib/*:src" server.DatabaseViewer clear

echo ""
echo "✅ Database cleared successfully!"
echo "All server sessions and user activity records have been removed." 