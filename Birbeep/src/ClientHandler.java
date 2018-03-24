import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
/**
 * 
 * @author Birdbeep
 * 
 * Este código es provisional (24/03/2018)
 * Este código es solo orientativo
 *
 */

class ClientHandler extends Thread {
	private Socket client;
	private Scanner input;
	private PrintWriter output;

	public ClientHandler(Socket socket) {
		// Set up reference to associated socket...
		client = socket;

		try {
			input = new Scanner(client.getInputStream());
			output = new PrintWriter(client.getOutputStream(), true);
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
	}

	public void run() {
		String received;
		do {
			// Accept message from client on
			// the socket's input stream...
			received = input.nextLine();

			// Echo message back to client on
			// the socket's output stream...
			output.println("ECHO: " + received);

			// Repeat above until '***CLOSE***' sent by client...
		} while (!received.equals("***CLOSE***"));

		try {
			if (client != null) {
				System.out.println("Closing down connection...");
				client.close();
			}
		} catch (IOException ioEx) {
			System.out.println("Unable to disconnect!");
		}
	}
}
