# Ink-Sync Windows Setup Guide

This guide will help Windows users set up and run the Ink-Sync collaborative whiteboard application.

## Prerequisites

- **Java 8 or higher** (Download from: https://adoptium.net/)
- **MySQL** (Optional - for database tracking)

## Quick Start (Without Database)

If you just want to run the application without database tracking:

1. **Download the project files** from your friend
2. **Open Command Prompt** in the project directory
3. **Compile and run**:
   ```cmd
   javac -cp "lib/*:src" -d bin src/**/*.java
   java -cp "lib/*:bin:src" server.Server
   ```
4. **In another Command Prompt**:
   ```cmd
   java -cp "lib/*:bin:src" client.Client
   ```

The application will run without database tracking and show messages like:
```
Database connection failed: ...
Application will run without database tracking.
```

## Full Setup (With Database)

### Option 1: Install MySQL Server

1. **Download MySQL** from: https://dev.mysql.com/downloads/mysql/
2. **Install MySQL** with default settings
3. **Set root password** during installation
4. **Update DatabaseManager.java** with your password:
   ```java
   private static final String DB_PASSWORD = "your_password";
   ```

### Option 2: Use XAMPP (Easier)

1. **Download XAMPP** from: https://www.apachefriends.org/
2. **Install XAMPP** (includes MySQL)
3. **Start MySQL** from XAMPP Control Panel
4. **Set root password** (usually empty by default)
5. **Update DatabaseManager.java**:
   ```java
   private static final String DB_PASSWORD = ""; // or your password
   ```

### Option 3: Use WAMP

1. **Download WAMP** from: https://www.wampserver.com/
2. **Install WAMP** (includes MySQL)
3. **Start WAMP** and ensure MySQL is running
4. **Update DatabaseManager.java** with your credentials

## Running the Application

### Method 1: Using Scripts (Recommended)

1. **Open Command Prompt** in the project directory
2. **Run the server**:
   ```cmd
   run-server.bat
   ```
3. **In another Command Prompt**:
   ```cmd
   run-client.bat
   ```

### Method 2: Manual Commands

1. **Compile the project**:
   ```cmd
   javac -cp "lib/*:src" -d bin src/**/*.java
   ```

2. **Start the server**:
   ```cmd
   java -cp "lib/*:bin:src" server.Server
   ```

3. **Start a client**:
   ```cmd
   java -cp "lib/*:bin:src" client.Client
   ```

## Connecting to Your Friend's Server

1. **Ask your friend for their server PIN** (last 3 digits of their IP)
2. **Enter the PIN** in the client connection dialog
3. **Enter your username** and join a board
4. **Start drawing collaboratively!**

## Troubleshooting

### Java Issues
- **"java not recognized"**: Install Java and add to PATH
- **"javac not found"**: Install Java JDK (not just JRE)

### MySQL Issues
- **"Access denied"**: Check username/password in DatabaseManager.java
- **"Connection refused"**: Make sure MySQL is running
- **"Driver not found"**: The MySQL driver is included in the lib folder

### Network Issues
- **"Connection timeout"**: Check if server is running and PIN is correct
- **"Address already in use"**: Server is already running, use different port

### Database Not Working
- The application will run without database tracking
- You'll see messages about database connection failing
- All drawing and collaboration features will still work

## File Structure

```
Ink-Sync/
├── src/                    # Source code
├── lib/                    # Libraries (MySQL driver, JUnit)
├── bin/                    # Compiled classes
├── run-server.bat         # Windows server script
├── run-client.bat         # Windows client script
├── view-database.bat      # Database viewer script
└── README.md              # This file
```

## Support

If you encounter issues:
1. Check that Java is installed: `java -version`
2. Verify MySQL is running (if using database)
3. Check the console output for error messages
4. Ask your friend for help with network configuration

The application will work perfectly without database tracking - you just won't see session statistics! 