# Ink-Sync Setup for Windows

This guide explains how to run the Ink-Sync server and client on a Windows machine.

## Prerequisites

1.  **Java Development Kit (JDK)**: Make sure you have JDK version 11 or higher installed. You can download it from [Oracle](https://www.oracle.com/java/technologies/javase-downloads.html) or use an open-source distribution like [AdoptOpenJDK](https://adoptopenjdk.net/).
2.  **MySQL (Optional)**: If you want to use the database tracking feature, you need a local MySQL server. You can install it using the [MySQL Installer for Windows](https://dev.mysql.com/downloads/installer/). A popular alternative that includes MySQL is [XAMPP](https://www.apachefriends.org/index.html).

    *If you don't have MySQL installed, the application will still run, but it won't be able to log server sessions or user activity.*

## How to Run

### 1. Run the Server

Double-click on `run-server.bat`.

-   This will first compile the project and then start the server.
-   A command prompt window will open, followed by the **Ink-Sync Server GUI**.
-   The GUI will show you the **Server IP** and a 3-digit **PIN**. Clients will use this PIN to connect.
-   To stop the server, click the **"Stop Server"** button on the GUI. This is the recommended way to shut down gracefully.

### 2. Run the Client

Double-click on `run-client.bat`.

-   This will open the **Ink-Sync Client** window.
-   Make sure the server is running before you start a client.
-   Enter the 3-digit **PIN** from the server GUI to connect.

## Database Management (Optional)

If you have MySQL set up, you can use these scripts:

-   **`view-database.bat`**: Double-click to see the current contents of the `server_sessions` and `user_activity` tables. The window will stay open until you press a key.
-   **`clear-database.bat`**: Double-click to delete all records from the database. This is useful for starting with a fresh log.

That's it! You're ready to collaborate.

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