# 🎨 Ink-Sync: Collaborative Whiteboard

<div align="center">

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white)
![Real-time](https://img.shields.io/badge/Real--time-Collaboration-00D4AA?style=for-the-badge)

**A Java-based collaborative whiteboard application with real-time drawing synchronization**

**Created by Srujan Divakar**

[🚀 Quick Start](#-quick-start) • [🎯 Features](#-features) • [📁 Project Structure](#-project-structure)

</div>

---

## 🎯 Key Features

- **🎨 Real-time Drawing**: Multiple users can draw simultaneously
- **🖱️ Freehand Drawing**: Smooth mouse-based drawing with customizable colors and brush sizes
- **🧽 Eraser Mode**: Switch between drawing and erasing
- **🔄 Multiple Boards**: Create and switch between different drawing boards
- **🔐 PIN Connection**: Easy 6-digit PIN system for LAN connectivity
- **📊 Session Tracking**: Database integration for user activity monitoring
- **🖥️ Professional GUI**: Clean Swing interface for both client and server

---

## 🚀 Quick Start

### Prerequisites
- Java 8 or higher
- MySQL Server (optional - app works without it)

### 1. Clone and Setup
```bash
git clone <repository-url>
cd CollaborativeWhiteboard
```

### 2. Start the Application

**Start Server:**
```bash
./scripts/MAC/run-server.sh
```

**Start Client:**
```bash
./scripts/MAC/run-client.sh
```

**Stop Server:**
```bash
./scripts/MAC/stop-server.sh
```

### 3. Connect and Draw
1. **Note the PIN** shown in the server GUI
2. **Enter the PIN** in the client connection dialog
3. **Choose a username** and select/create a board
4. **Start drawing** collaboratively!

---

## 📁 Project Structure

```
CollaborativeWhiteboard/
├── src/
│   ├── client/              # Client-side application
│   │   ├── Client.java      # Main client controller
│   │   ├── ClientGUI.java   # Client user interface
│   │   ├── Canvas.java      # Drawing surface
│   │   ├── DrawingController.java # Mouse event handling
│   │   └── *Protocol.java   # Communication protocols
│   ├── server/              # Server-side application
│   │   ├── Server.java      # Main server controller
│   │   ├── ServerGUI.java   # Server management interface
│   │   ├── Board.java       # Board management
│   │   ├── DatabaseManager.java # Database integration
│   │   └── ServerProtocol.java # Server communication
│   └── command/             # Command pattern implementation
│       └── Command.java     # Drawing commands
├── lib/                     # Dependencies (JUnit, MySQL)
├── bin/                     # Compiled classes
├── docs/                    # Documentation and icons
└── scripts/                 # Build and run scripts
    ├── MAC/                 # macOS scripts
    └── Windows/             # Windows scripts
```

---

## 🎨 How to Use

### Drawing Tools
- **Draw Mode**: Default drawing with selected color
- **Eraser Mode**: Click "Mode" menu → "Erase"
- **Color Selection**: "Paint Color" menu with full color chooser
- **Brush Width**: Use the slider to adjust brush thickness (0-50px)
- **Board Switching**: "Board(s)" menu to switch between boards

### Server Features
- **PIN Generation**: 6-digit code for client connections
- **Session Statistics**: View current user activity
- **Board Management**: Monitor all active boards
- **Graceful Shutdown**: Clean server termination

---

## 🗄️ Database Setup (Optional)

The app works without a database, but you can enable session tracking:

```bash
# Install MySQL
brew install mysql && brew services start mysql  # macOS
sudo apt-get install mysql-server && sudo systemctl start mysql  # Ubuntu

# Run setup script
./scripts/MAC/setup-database.sh
```

The app automatically detects common MySQL configurations and creates necessary tables.

---

## 🧪 Testing

Run the test suite:
```bash
javac -cp "lib/*:src" -d bin src/**/*.java
java -cp "lib/*:bin:src" org.junit.runner.JUnitCore client.ClientTest server.ServerTest
```

---

## 🐛 Troubleshooting

### Common Issues
- **Connection failed**: Check if server is running and PIN is correct
- **Database errors**: App works without database - check MySQL service
- **Compilation errors**: Ensure Java 8+ and all dependencies in `lib/` folder

### Useful Commands
```bash
# Check server status
./scripts/MAC/check-server.sh

# View database contents
./scripts/MAC/view-database.sh

# Clean and rebuild
./scripts/MAC/clean-build.sh
```

---

## 🤝 Contributing

**Maintained by Srujan Divakar**

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

---

<div align="center">

**Built with ❤️ by Srujan Divakar and Sujith Lasrado using Java, Swing, and MySQL**

</div>
