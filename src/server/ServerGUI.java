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
    private JButton stopButton;
    private JButton statsButton;
    
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
        setSize(350, 250);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245));
        
        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
            "Server Information",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            new Color(50, 50, 50)
        ));
        infoPanel.setBackground(Color.WHITE);
        
        // Status
        statusLabel = new JLabel("Status: Running");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setForeground(new Color(0, 150, 0));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // IP Address
        String ipAddress = getServerIPAddress();
        ipLabel = new JLabel("Server IP: " + ipAddress);
        ipLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        ipLabel.setForeground(new Color(80, 80, 80));
        ipLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // PIN (last 6 digits of IP)
        String pin = getPINFromIP(ipAddress);
        pinLabel = new JLabel("PIN: " + pin);
        pinLabel.setFont(new Font("Arial", Font.BOLD, 24));
        pinLabel.setForeground(Color.BLACK);
        pinLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pinLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Instructions
        JLabel instructionsLabel = new JLabel("Clients should enter this PIN to connect");
        instructionsLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        instructionsLabel.setForeground(new Color(120, 120, 120));
        instructionsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        infoPanel.add(statusLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(ipLabel);
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(pinLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(instructionsLabel);
        
        // Control panel
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        controlPanel.setBackground(new Color(245, 245, 245));
        
        stopButton = new JButton("Stop Server");
        stopButton.setFont(new Font("Arial", Font.BOLD, 12));
        stopButton.setBackground(new Color(220, 53, 69));
        stopButton.setForeground(Color.WHITE);
        stopButton.setOpaque(true);
        stopButton.setBorderPainted(false);
        stopButton.setFocusPainted(false);
        
        statsButton = new JButton("Session Stats");
        statsButton.setFont(new Font("Arial", Font.BOLD, 12));
        statsButton.setBackground(new Color(0, 123, 255));
        statsButton.setForeground(Color.WHITE);
        statsButton.setOpaque(true);
        statsButton.setBorderPainted(false);
        statsButton.setFocusPainted(false);
        
        controlPanel.add(stopButton);
        controlPanel.add(statsButton);
        
        // Add components to main panel
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
        
        // Add action listeners
        setupActionListeners();
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
        
        // Stats button
        statsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String stats = server.getSessionStatistics();
                JOptionPane.showMessageDialog(
                    ServerGUI.this,
                    stats,
                    "Session Statistics",
                    JOptionPane.INFORMATION_MESSAGE
                );
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
            statusLabel.setText("Status: Stopping");
            statusLabel.setForeground(new Color(255, 140, 0));
            stopButton.setEnabled(false);
            statsButton.setEnabled(false);
            
            // Shutdown server in background thread
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        server.shutDown();
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                statusLabel.setText("Status: Stopped");
                                statusLabel.setForeground(new Color(220, 53, 69));
                                dispose(); // Close the window
                            }
                        });
                    } catch (Exception e) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                statusLabel.setText("Status: Error");
                                statusLabel.setForeground(new Color(220, 53, 69));
                                JOptionPane.showMessageDialog(
                                    ServerGUI.this,
                                    "Error stopping server: " + e.getMessage(),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE
                                );
                            }
                        });
                        e.printStackTrace();
                    }
                }
            }).start();
            
        } catch (Exception e) {
            statusLabel.setText("Status: Error");
            statusLabel.setForeground(new Color(220, 53, 69));
            JOptionPane.showMessageDialog(
                this,
                "Error stopping server: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }
    
    /**
     * Gets the server's IP address by intelligently filtering out virtual and loopback interfaces.
     * @return the IP address as a string
     */
    private String getServerIPAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                String interfaceName = networkInterface.getDisplayName().toLowerCase();

                // Skip loopback, virtual, and disabled interfaces
                if (networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp() || 
                    interfaceName.contains("virtual") || interfaceName.contains("vmnet") || interfaceName.contains("vbox")) {
                    continue;
                }

                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    // Return the first valid IPv4 address found
                    if (addr.getAddress().length == 4 && !addr.isLoopbackAddress()) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting IP address: " + e.getMessage());
        }
        // Fallback to localhost if a suitable IP is not found
        return "127.0.0.1";
    }
    
    /**
     * Extracts the PIN (last two parts of IP) from an IP address, as a 6-digit string.
     * @param ipAddress the IP address
     * @return the PIN as a 6-digit string (e.g., "200034")
     */
    private String getPINFromIP(String ipAddress) {
        try {
            String[] parts = ipAddress.split("\\.");
            if (parts.length == 4) {
                int part3 = Integer.parseInt(parts[2]);
                int part4 = Integer.parseInt(parts[3]);
                return String.format("%03d%03d", part3, part4);
            }
        } catch (Exception e) {
            // Fall through to default
        }
        return "000001"; // Default fallback
    }
} 