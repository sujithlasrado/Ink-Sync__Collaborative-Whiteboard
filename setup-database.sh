#!/bin/bash

echo "Ink-Sync Database Setup"
echo "======================="
echo ""
echo "This script will help you set up the MySQL database for Ink-Sync."
echo ""

# Check if MySQL is installed
if ! command -v mysql &> /dev/null; then
    echo "MySQL is not installed. Please install MySQL first:"
    echo "  - macOS: brew install mysql"
    echo "  - Ubuntu: sudo apt-get install mysql-server"
    echo "  - Windows: Download from https://dev.mysql.com/downloads/mysql/"
    exit 1
fi

echo "MySQL is installed. Starting MySQL service..."
# Start MySQL service (this might need to be done manually depending on your system)
# brew services start mysql  # for macOS with Homebrew
# sudo systemctl start mysql # for Linux

echo ""
echo "Please run the following commands in MySQL to set up the database:"
echo ""
echo "1. Connect to MySQL as root:"
echo "   mysql -u root -p"
echo ""
echo "2. Create the database (if not exists):"
echo "   CREATE DATABASE IF NOT EXISTS inksync_db;"
echo ""
echo "3. Create a user for Ink-Sync (optional but recommended):"
echo "   CREATE USER 'inksync_user'@'localhost' IDENTIFIED BY 'your_password';"
echo "   GRANT ALL PRIVILEGES ON inksync_db.* TO 'inksync_user'@'localhost';"
echo "   FLUSH PRIVILEGES;"
echo ""
echo "4. Update the DatabaseManager.java file with your credentials:"
echo "   - Change DB_USER to 'inksync_user' (or keep 'root')"
echo "   - Change DB_PASSWORD to your chosen password"
echo ""
echo "5. Test the connection by running the server:"
echo "   ./run-server.sh"
echo ""
echo "The database tables will be created automatically when the server starts." 