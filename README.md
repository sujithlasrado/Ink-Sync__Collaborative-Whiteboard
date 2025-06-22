# Ink-Sync: Collaborative Whiteboard Application

A Java-based collaborative whiteboard application with client-server architecture, featuring real-time drawing synchronization and database tracking.

## Features

- **Real-time Collaboration**: Multiple users can draw simultaneously on shared whiteboards
- **PIN-based Connection**: Easy LAN connection using server PIN codes
- **Database Tracking**: MySQL database tracks server sessions and user activity
- **Swing GUI**: User-friendly interface for both client and server
- **Session Management**: Track when users join/leave and server start/stop times

## Database Features

The application now includes MySQL database integration to track:

- **Server Sessions**: Start and end times of server sessions
- **User Activity**: Entry and exit times for each user with username and board information
- **Session Statistics**: Real-time statistics available through the server GUI

## Prerequisites

- Java 8 or higher
- MySQL Server
- Network connectivity for client-server communication

## Setup Instructions

### 1. Database Setup

1. **Install MySQL Server** (if not already installed):
   ```bash
   # macOS
   brew install mysql
   
   # Ubuntu
   sudo apt-get install mysql-server
   ```

2. **Start MySQL Service**:
   ```bash
   # macOS
   brew services start mysql
   
   # Ubuntu
   sudo systemctl start mysql
   ```

3. **Set up the database**:
   ```bash
   ./MAC/setup-database.sh
   ```

4. **Update database credentials** in `src/server/DatabaseManager.java`:
   ```java
   private static final String DB_USER = "your_username";
   private static final String DB_PASSWORD = "your_password";
   ```

### 2. Download Dependencies

```bash
./download-mysql-driver.sh
```

### 3. Compile and Run

**Start the Server**:
```bash
./MAC/run-server.sh
```

**Start a Client**:
```bash
./MAC/run-client.sh
```

**Stop the Server**:
```bash
./MAC/stop-server.sh
```

## Usage

### Server
- The server GUI shows the server IP, PIN, and connection status
- Click "Show Session Statistics" to view current session data
- Click "Stop Server" to gracefully shut down the server

### Client
- Enter the server PIN (last 3 digits of server IP)
- Enter your username
- Select or create a board
- Start drawing collaboratively!

### Database Monitoring

View database contents:
```bash
./MAC/view-database.sh
```

## Database Schema

### server_sessions
- `session_id` (Primary Key)
- `start_time` (DATETIME)
- `end_time` (DATETIME, NULL if active)
- `status` (ENUM: 'active', 'ended')

### user_activity
- `activity_id` (Primary Key)
- `session_id` (Foreign Key to server_sessions)
- `username` (VARCHAR)
- `board_name` (VARCHAR)
- `entry_time` (DATETIME)
- `exit_time` (DATETIME, NULL if active)
- `status` (ENUM: 'active', 'exited')

## Project Structure

```
Ink-Sync/
├── src/
│   ├── client/          # Client-side code
│   ├── server/          # Server-side code
│   ├── command/         # Command pattern implementation
│   └── testResources/   # Test utilities
├── lib/                 # External libraries (JUnit, MySQL)
├── bin/                 # Compiled classes
├── docs/               # Documentation and screenshots
└── scripts/            # Build and run scripts
```

## Troubleshooting

### Database Connection Issues
- Ensure MySQL is running: `brew services list` (macOS) or `sudo systemctl status mysql` (Linux)
- Verify credentials in `DatabaseManager.java`
- Check MySQL logs for connection errors

### Network Issues
- Ensure server and client are on the same network
- Check firewall settings
- Verify the PIN is correct (last 3 digits of server IP)

### Compilation Issues
- Ensure all dependencies are downloaded: `./download-mysql-driver.sh`
- Check Java version: `java -version`
- Verify classpath includes all required JARs

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is for educational purposes.
