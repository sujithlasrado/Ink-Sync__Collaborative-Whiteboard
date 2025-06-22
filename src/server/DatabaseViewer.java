package server;

import java.sql.*;
import java.time.format.DateTimeFormatter;

/**
 * Simple utility to view database contents
 */
public class DatabaseViewer {
    
    public static void main(String[] args) {
        // Check if clear command is requested
        if (args.length > 0 && args[0].equals("clear")) {
            clearDatabase();
            return;
        }
        
        System.out.println("=== Ink-Sync Database Viewer ===\n");
        
        try {
            DatabaseManager dbManager = new DatabaseManager();
            
            if (dbManager.getConnection() == null) {
                System.out.println("❌ Database connection failed!");
                System.out.println("\n📋 MySQL Setup Instructions:");
                System.out.println("1. Install MySQL if not installed:");
                System.out.println("   brew install mysql");
                System.out.println("\n2. Start MySQL service:");
                System.out.println("   brew services start mysql");
                System.out.println("   # OR");
                System.out.println("   mysql.server start");
                System.out.println("\n3. Set up root password (if needed):");
                System.out.println("   mysql_secure_installation");
                System.out.println("\n4. Update DatabaseManager.java with correct credentials:");
                System.out.println("   - Change DB_PASSWORD to your MySQL root password");
                System.out.println("   - Or create a new user with proper permissions");
                System.out.println("\n5. Test connection:");
                System.out.println("   mysql -u root -p");
                System.out.println("\n6. Create database (if needed):");
                System.out.println("   CREATE DATABASE IF NOT EXISTS inksync_db;");
                return;
            }
            
            // Show all server sessions
            showAllSessions(dbManager);
            
            // Show all user activity
            showAllUserActivity(dbManager);
            
            dbManager.close();
            
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            System.out.println("\n📋 Troubleshooting:");
            System.out.println("1. Make sure MySQL is running");
            System.out.println("2. Check credentials in DatabaseManager.java");
            System.out.println("3. Ensure database 'inksync_db' exists");
            System.out.println("4. Verify MySQL user has proper permissions");
        }
    }
    
    private static void clearDatabase() {
        System.out.println("🗑️  Clearing Ink-Sync Database Contents");
        System.out.println("======================================");
        System.out.println("");
        
        try {
            DatabaseManager dbManager = new DatabaseManager();
            
            if (dbManager.getConnection() == null) {
                System.out.println("❌ Database connection failed! Cannot clear database.");
                return;
            }
            
            Connection conn = dbManager.getConnection();
            
            // Clear user activity first (due to foreign key constraint)
            String clearUserActivity = "DELETE FROM user_activity";
            String clearServerSessions = "DELETE FROM server_sessions";
            String resetUserActivityAutoIncrement = "ALTER TABLE user_activity AUTO_INCREMENT = 1";
            String resetServerSessionsAutoIncrement = "ALTER TABLE server_sessions AUTO_INCREMENT = 1";
            
            Statement stmt = conn.createStatement();
            
            System.out.println("Clearing user activity records...");
            int userActivityDeleted = stmt.executeUpdate(clearUserActivity);
            System.out.println("   Deleted " + userActivityDeleted + " user activity records");
            
            System.out.println("Clearing server sessions...");
            int serverSessionsDeleted = stmt.executeUpdate(clearServerSessions);
            System.out.println("   Deleted " + serverSessionsDeleted + " server sessions");
            
            System.out.println("Resetting auto-increment counters...");
            stmt.executeUpdate(resetUserActivityAutoIncrement);
            stmt.executeUpdate(resetServerSessionsAutoIncrement);
            
            dbManager.close();
            
            System.out.println("");
            System.out.println("✅ Database cleared successfully!");
            System.out.println("All server sessions and user activity records have been removed.");
            
        } catch (Exception e) {
            System.out.println("❌ Error clearing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void showAllSessions(DatabaseManager dbManager) {
        try {
            Connection conn = dbManager.getConnection();
            if (conn == null) {
                System.out.println("❌ No database connection available");
                return;
            }
            
            String sql = "SELECT * FROM server_sessions ORDER BY start_time DESC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("=== Server Sessions ===");
            System.out.printf("%-10s %-20s %-20s %-10s%n", "Session ID", "Start Time", "End Time", "Status");
            System.out.println("------------------------------------------------------------");
            
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.printf("%-10d %-20s %-20s %-10s%n",
                    rs.getInt("session_id"),
                    rs.getString("start_time"),
                    rs.getString("end_time") != null ? rs.getString("end_time") : "N/A",
                    rs.getString("status")
                );
            }
            
            if (!hasData) {
                System.out.println("No server sessions found. Start the server to create sessions.");
            }
            System.out.println();
            
        } catch (SQLException e) {
            System.err.println("Error viewing sessions: " + e.getMessage());
        }
    }
    
    private static void showAllUserActivity(DatabaseManager dbManager) {
        try {
            Connection conn = dbManager.getConnection();
            if (conn == null) {
                System.out.println("❌ No database connection available");
                return;
            }
            
            String sql = "SELECT ua.*, ss.start_time as session_start FROM user_activity ua " +
                        "JOIN server_sessions ss ON ua.session_id = ss.session_id " +
                        "ORDER BY ua.entry_time DESC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("=== User Activity ===");
            System.out.printf("%-10s %-15s %-20s %-20s %-20s %-10s%n", 
                "Activity ID", "Username", "Board", "Entry Time", "Exit Time", "Status");
            System.out.println("--------------------------------------------------------------------------------");
            
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.printf("%-10d %-15s %-20s %-20s %-20s %-10s%n",
                    rs.getInt("activity_id"),
                    rs.getString("username"),
                    rs.getString("board_name"),
                    rs.getString("entry_time"),
                    rs.getString("exit_time") != null ? rs.getString("exit_time") : "N/A",
                    rs.getString("status")
                );
            }
            
            if (!hasData) {
                System.out.println("No user activity found. Connect clients to the server to see activity.");
            }
            System.out.println();
            
        } catch (SQLException e) {
            System.err.println("Error viewing user activity: " + e.getMessage());
        }
    }
} 