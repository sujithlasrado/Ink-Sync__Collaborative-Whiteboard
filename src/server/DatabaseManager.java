package server;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Database manager for tracking server sessions and user activity
 * Auto-detects local database configurations for hassle-free setup
 */
public class DatabaseManager {
    
    // Common local database configurations to try
    private static final String[] DB_URLS = {
        "jdbc:mysql://localhost:3306/inksync_db",
        "jdbc:mysql://127.0.0.1:3306/inksync_db"
    };
    
    private static final String[] DB_USERS = {
        "root",
        "inksync_user"
    };
    
    private static final String[] DB_PASSWORDS = {
        "", // No password (common for local development)
        "password", // Common default password
        "root", // Common root password
        "admin", // Common admin password
        "mysql", // Common MySQL password
        "srujan07" // Your actual password
    };
    
    private Connection connection;
    private int currentSessionId;
    
    /**
     * Initialize database connection and create tables if they don't exist
     * Automatically tries common local database configurations
     */
    public DatabaseManager() {
        System.out.println("🔍 Auto-detecting local database configuration...");
        
        // Try to load MySQL JDBC driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL JDBC driver not found. Database tracking disabled.");
            System.err.println("   To enable database tracking, ensure mysql-connector-java.jar is in the lib folder.");
            connection = null;
            return;
        }
        
        // Try different database configurations
        for (String dbUrl : DB_URLS) {
            for (String dbUser : DB_USERS) {
                for (String dbPassword : DB_PASSWORDS) {
                    if (tryConnect(dbUrl, dbUser, dbPassword)) {
                        System.out.println("✅ Database connected successfully!");
                        System.out.println("   URL: " + dbUrl);
                        System.out.println("   User: " + dbUser);
                        System.out.println("   Password: " + (dbPassword.isEmpty() ? "(none)" : "***"));
                        
                        // Create database and tables
                        createDatabaseIfNotExists();
                        createTablesIfNotExist();
                        return;
                    }
                }
            }
        }
        
        // If we get here, no configuration worked
        System.err.println("❌ Could not connect to any local database configuration.");
        System.err.println("   Application will run without database tracking.");
        System.err.println("   To enable database tracking:");
        System.err.println("   1. Install MySQL (XAMPP, WAMP, or standalone)");
        System.err.println("   2. Start MySQL service");
        System.err.println("   3. Create database: CREATE DATABASE inksync_db;");
        System.err.println("   4. Restart the application");
        connection = null;
    }
    
    /**
     * Try to connect to database with given credentials
     */
    private boolean tryConnect(String dbUrl, String dbUser, String dbPassword) {
        try {
            System.out.print("   Trying: " + dbUser + "@" + dbUrl.replace("jdbc:mysql://", "") + "... ");
            
            // First try to connect to MySQL server (without database)
            String serverUrl = dbUrl.substring(0, dbUrl.lastIndexOf("/"));
            Connection tempConnection = DriverManager.getConnection(serverUrl, dbUser, dbPassword);
            
            // If successful, try to connect to our database
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            tempConnection.close();
            
            System.out.println("✅ SUCCESS!");
            return true;
            
        } catch (SQLException e) {
            System.out.println("❌ Failed");
            return false;
        }
    }
    
    /**
     * Create database if it doesn't exist
     */
    private void createDatabaseIfNotExists() {
        try {
            // Connect to MySQL server (without specifying database)
            String serverUrl = "jdbc:mysql://localhost:3306";
            Connection tempConnection = null;
            
            // Try to connect with the same credentials that worked
            for (String dbUser : DB_USERS) {
                for (String dbPassword : DB_PASSWORDS) {
                    try {
                        tempConnection = DriverManager.getConnection(serverUrl, dbUser, dbPassword);
                        break;
                    } catch (SQLException e) {
                        continue;
                    }
                }
                if (tempConnection != null) break;
            }
            
            if (tempConnection != null) {
                Statement stmt = tempConnection.createStatement();
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS inksync_db");
                tempConnection.close();
                System.out.println("✅ Database 'inksync_db' created/verified");
            }
            
        } catch (SQLException e) {
            System.err.println("⚠️  Could not create database: " + e.getMessage());
        }
    }
    
    /**
     * Create tables if they don't exist
     */
    private void createTablesIfNotExist() {
        try {
            Statement stmt = connection.createStatement();
            
            // Server sessions table
            String createSessionsTable = """
                CREATE TABLE IF NOT EXISTS server_sessions (
                    session_id INT AUTO_INCREMENT PRIMARY KEY,
                    start_time DATETIME NOT NULL,
                    end_time DATETIME NULL,
                    status ENUM('active', 'ended') DEFAULT 'active'
                )
                """;
            
            // User activity table
            String createUserActivityTable = """
                CREATE TABLE IF NOT EXISTS user_activity (
                    activity_id INT AUTO_INCREMENT PRIMARY KEY,
                    session_id INT NOT NULL,
                    username VARCHAR(50) NOT NULL,
                    board_name VARCHAR(100) NOT NULL,
                    entry_time DATETIME NOT NULL,
                    exit_time DATETIME NULL,
                    status ENUM('active', 'exited') DEFAULT 'active',
                    FOREIGN KEY (session_id) REFERENCES server_sessions(session_id)
                )
                """;
            
            stmt.executeUpdate(createSessionsTable);
            stmt.executeUpdate(createUserActivityTable);
            
            System.out.println("Database tables created/verified successfully");
            
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Start a new server session
     * @return session ID
     */
    public int startServerSession() {
        if (connection == null) {
            System.out.println("Database not available - skipping session tracking");
            return -1;
        }
        
        try {
            String sql = "INSERT INTO server_sessions (start_time, status) VALUES (?, 'active')";
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            LocalDateTime now = LocalDateTime.now();
            pstmt.setString(1, now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                currentSessionId = rs.getInt(1);
                System.out.println("Server session started with ID: " + currentSessionId);
                return currentSessionId;
            }
            
        } catch (SQLException e) {
            System.err.println("Error starting server session: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }
    
    /**
     * End the current server session
     */
    public void endServerSession() {
        if (connection == null || currentSessionId == -1) {
            System.out.println("Database not available - skipping session end tracking");
            return;
        }
        
        try {
            String sql = "UPDATE server_sessions SET end_time = ?, status = 'ended' WHERE session_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            
            LocalDateTime now = LocalDateTime.now();
            pstmt.setString(1, now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            pstmt.setInt(2, currentSessionId);
            
            pstmt.executeUpdate();
            System.out.println("Server session ended: " + currentSessionId);
            
        } catch (SQLException e) {
            System.err.println("Error ending server session: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Record user entry
     * @param username the username
     * @param boardName the board name
     * @return activity ID
     */
    public int recordUserEntry(String username, String boardName) {
        if (connection == null || currentSessionId == -1) {
            System.out.println("Database not available - skipping user entry tracking");
            return -1;
        }
        
        try {
            String sql = "INSERT INTO user_activity (session_id, username, board_name, entry_time, status) VALUES (?, ?, ?, ?, 'active')";
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setInt(1, currentSessionId);
            pstmt.setString(2, username);
            pstmt.setString(3, boardName);
            
            LocalDateTime now = LocalDateTime.now();
            pstmt.setString(4, now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int activityId = rs.getInt(1);
                System.out.println("User entry recorded: " + username + " on board " + boardName + " (ID: " + activityId + ")");
                return activityId;
            }
            
        } catch (SQLException e) {
            System.err.println("Error recording user entry: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }
    
    /**
     * Record user exit
     * @param username the username
     */
    public void recordUserExit(String username) {
        if (connection == null || currentSessionId == -1) {
            System.out.println("Database not available - skipping user exit tracking");
            return;
        }
        
        try {
            String sql = "UPDATE user_activity SET exit_time = ?, status = 'exited' WHERE session_id = ? AND username = ? AND status = 'active'";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            
            LocalDateTime now = LocalDateTime.now();
            pstmt.setString(1, now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            pstmt.setInt(2, currentSessionId);
            pstmt.setString(3, username);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User exit recorded: " + username);
            }
            
        } catch (SQLException e) {
            System.err.println("Error recording user exit: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Get current session statistics
     * @return formatted statistics string
     */
    public String getSessionStatistics() {
        try {
            StringBuilder stats = new StringBuilder();
            
            // Get session info
            String sessionSql = "SELECT start_time, status FROM server_sessions WHERE session_id = ?";
            PreparedStatement sessionStmt = connection.prepareStatement(sessionSql);
            sessionStmt.setInt(1, currentSessionId);
            
            ResultSet sessionRs = sessionStmt.executeQuery();
            if (sessionRs.next()) {
                stats.append("Session ID: ").append(currentSessionId).append("\n");
                stats.append("Start Time: ").append(sessionRs.getString("start_time")).append("\n");
                stats.append("Status: ").append(sessionRs.getString("status")).append("\n\n");
            }
            
            // Get active users
            String activeUsersSql = "SELECT username, board_name, entry_time FROM user_activity WHERE session_id = ? AND status = 'active'";
            PreparedStatement usersStmt = connection.prepareStatement(activeUsersSql);
            usersStmt.setInt(1, currentSessionId);
            
            ResultSet usersRs = usersStmt.executeQuery();
            stats.append("Active Users:\n");
            while (usersRs.next()) {
                stats.append("- ").append(usersRs.getString("username"))
                     .append(" (Board: ").append(usersRs.getString("board_name"))
                     .append(", Joined: ").append(usersRs.getString("entry_time"))
                     .append(")\n");
            }
            
            return stats.toString();
            
        } catch (SQLException e) {
            System.err.println("Error getting session statistics: " + e.getMessage());
            return "Error retrieving statistics";
        }
    }
    
    /**
     * Close database connection
     */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
    
    /**
     * Get current session ID
     * @return session ID
     */
    public int getCurrentSessionId() {
        return currentSessionId;
    }
    
    /**
     * Get database connection (for external use)
     * @return database connection
     */
    public Connection getConnection() {
        return connection;
    }
} 