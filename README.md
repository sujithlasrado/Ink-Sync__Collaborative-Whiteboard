# Collaborative Whiteboard Editor

This is the source code for a collaborative whiteboard server and client, written with @srujandivakar and @sujithlasrado for our 2nd sem mini project.

To run the code, first launch the server by running the main loop in /src/server/Server.java.  Then run the main loop in /src/client/Client.java to launch an instance of the whiteboard client.  From there, enter a username, create a new board, join the board and start drawing.  To collaborate on a board, run another instance of the client and connect to the same board.  In real time, all updates will propagate across all board clients.

To connect to a whiteboard server running anywhere but localhost:4444, change the host and port parameters in the main loop of the board class.
