import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
/**
 * 
 * @author Birdbeep
 * 
 * Este código es provisional (24/03/2018)
 * Este código es solo orientativo
 *
 */

class ClientHandler extends Thread {
	private SSLSocket client;
	private SSLSession sslSession;
	private Scanner input;
	private PrintWriter output;
	
	public ClientHandler(SSLSocket socket) {
		// Set up reference to associated socket...
		
		client = socket;
		sslSession=client.getSession();//Obtenemos la sesión
		try {
			String usr=sslSession.getPeerPrincipal().getName();//Sabemos el nombre del cliente que se ha conectado.
		} catch (SSLPeerUnverifiedException e) {
			System.out.println(e.getMessage());
		}
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
