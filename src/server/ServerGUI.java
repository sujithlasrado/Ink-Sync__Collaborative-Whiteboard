package server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * GUI for the Ink-Sync server that displays connection information
 * including the server's IP address and PIN for clients to connect.
 */
public class ServerGUI extends JFrame {
    
    private static final long serialVersionUID = 1L;
    private final Server server;
    private JLabel statusLabel;
    private JLabel ipLabel;
    private JLabel pinLabel;
    private JTextArea logArea;
    private JButton stopButton;
    
    /**
     * Creates a new ServerGUI for the given server
     * @param server the server this GUI represents
     */
    public ServerGUI(Server server) {
        this.server = server;
        setupGUI();
    }
    
    /**
     * Sets up the GUI components
     */
    private void setupGUI() {
        setTitle("Ink-Sync Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(4, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Server Information"));
        
        // Status
        statusLabel = new JLabel("Status: Running");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setForeground(Color.GREEN);
        
        // IP Address
        String ipAddress = getServerIPAddress();
        ipLabel = new JLabel("Server IP: " + ipAddress);
        ipLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // PIN (last 3 digits of IP)
        String pin = getPINFromIP(ipAddress);
        pinLabel = new JLabel("PIN: " + pin);
        pinLabel.setFont(new Font("Arial", Font.BOLD, 16));
        pinLabel.setForeground(Color.BLUE);
        
        // Instructions
        JLabel instructionsLabel = new JLabel("Clients should enter this PIN to connect");
        instructionsLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        instructionsLabel.setForeground(Color.GRAY);
        
        infoPanel.add(statusLabel);
        infoPanel.add(ipLabel);
        infoPanel.add(pinLabel);
        infoPanel.add(instructionsLabel);
        
        // Log area
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 10));
        JScrollPane logScrollPane = new JScrollPane(logArea);
        logScrollPane.setBorder(BorderFactory.createTitledBorder("Server Log"));
        logScrollPane.setPreferredSize(new Dimension(350, 150));
        
        // Control panel
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        stopButton = new JButton("Stop Server");
        stopButton.setBackground(Color.RED);
        stopButton.setForeground(Color.WHITE);
        stopButton.setFont(new Font("Arial", Font.BOLD, 12));
        
        controlPanel.add(stopButton);
        
        // Add components to main panel
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(logScrollPane, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
        
        // Add action listeners
        setupActionListeners();
        
        // Add initial log message
        logMessage("Server started successfully");
        logMessage("Waiting for client connections...");
    }
    
    /**
     * Sets up action listeners for GUI components
     */
    private void setupActionListeners() {
        // Stop button
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(
                    ServerGUI.this,
                    "Are you sure you want to stop the server?",
                    "Confirm Server Stop",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (result == JOptionPane.YES_OPTION) {
                    stopServer();
                }
            }
        });
        
        // Window closing
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stopServer();
            }
        });
    }
    
    /**
     * Stops the server gracefully
     */
    private void stopServer() {
        try {
            logMessage("Shutting down server...");
            statusLabel.setText("Status: Stopping");
            statusLabel.setForeground(Color.ORANGE);
            stopButton.setEnabled(false);
            
            // Shutdown server in background thread
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        server.shutDown();
                        logMessage("Server stopped successfully");
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                statusLabel.setText("Status: Stopped");
                                statusLabel.setForeground(Color.RED);
                                dispose(); // Close the window
                            }
                        });
                    } catch (Exception e) {
                        logMessage("Error stopping server: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }).start();
            
        } catch (Exception e) {
            logMessage("Error stopping server: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Gets the server's IP address
     * @return the IP address as a string
     */
    private String getServerIPAddress() {
        try {
            // Try to get the actual network IP address, not localhost
            java.net.InetAddress localHost = java.net.InetAddress.getLocalHost();
            String hostAddress = localHost.getHostAddress();
            
            // If we got localhost, try to find the actual network interface
            if (hostAddress.equals("127.0.0.1")) {
                java.net.NetworkInterface networkInterface = java.net.NetworkInterface.getByName("en0"); // WiFi on Mac
                if (networkInterface == null) {
                    networkInterface = java.net.NetworkInterface.getByName("eth0"); // Ethernet
                }
                if (networkInterface == null) {
                    // Try to find any non-loopback interface
                    java.util.Enumeration<java.net.NetworkInterface> interfaces = java.net.NetworkInterface.getNetworkInterfaces();
                    while (interfaces.hasMoreElements()) {
                        java.net.NetworkInterface ni = interfaces.nextElement();
                        if (!ni.isLoopback() && ni.isUp()) {
                            java.util.Enumeration<java.net.InetAddress> addresses = ni.getInetAddresses();
                            while (addresses.hasMoreElements()) {
                                java.net.InetAddress addr = addresses.nextElement();
                                if (addr.getHostAddress().contains(".") && !addr.getHostAddress().startsWith("127.")) {
                                    return addr.getHostAddress();
                                }
                            }
                        }
                    }
                } else {
                    java.util.Enumeration<java.net.InetAddress> addresses = networkInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        java.net.InetAddress addr = addresses.nextElement();
                        if (addr.getHostAddress().contains(".") && !addr.getHostAddress().startsWith("127.")) {
                            return addr.getHostAddress();
                        }
                    }
                }
            }
            
            return hostAddress;
        } catch (Exception e) {
            System.err.println("Error getting IP address: " + e.getMessage());
            return "127.0.0.1";
        }
    }
    
    /**
     * Extracts the PIN (last 3 digits) from an IP address
     * @param ipAddress the IP address
     * @return the PIN as a string
     */
    private String getPINFromIP(String ipAddress) {
        try {
            String[] parts = ipAddress.split("\\.");
            if (parts.length == 4) {
                String lastOctet = parts[3];
                // Ensure it's a 3-digit PIN by padding with zeros if necessary
                if (lastOctet.length() == 1) {
                    return "00" + lastOctet;
                } else if (lastOctet.length() == 2) {
                    return "0" + lastOctet;
                } else {
                    return lastOctet;
                }
            }
        } catch (Exception e) {
            // Fall through to default
        }
        return "001"; // Default fallback
    }
    
    /**
     * Adds a message to the log area
     * @param message the message to log
     */
    public void logMessage(String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                logArea.append(java.time.LocalTime.now().toString().substring(0, 8) + " - " + message + "\n");
                logArea.setCaretPosition(logArea.getDocument().getLength());
            }
        });
    }
    
    /**
     * Updates the status label
     * @param status the new status text
     * @param color the color for the status
     */
    public void updateStatus(String status, Color color) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                statusLabel.setText("Status: " + status);
                statusLabel.setForeground(color);
            }
        });
    }
} 