#!/bin/bash
# Navigate to the project root directory
cd "$(dirname "$0")/../.."

echo "Cleaning project by removing all compiled .class files..."
find . -type f -name "*.class" -delete
echo "Project cleaned successfully."
echo ""

echo "Recompiling the entire project..."
javac -cp "lib/*:src" -d bin src/server/*.java src/client/*.java src/command/*.java
echo "Project recompiled successfully."
echo ""
echo "You can now run the server or client." 