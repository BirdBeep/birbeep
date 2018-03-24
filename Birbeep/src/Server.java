import java.io.IOException;
import java.net.ServerSocket;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
/**
 * 
 * @author BirdBeep
 * 
 * Clase principal del servidor, abre el Socket de escucha y se queda esperando en un try con recursos el desafio de un cliente
 * Mediante el socket de dialogo se llama a una clase que extiende de Thread para procesar los mensajes
 *
 */
public class Server {
	private static final int PORT = 1234;
	private static SSLServerSocket serverSocket;
	public static void main(String[] args) {
		try {
			serverSocket=(SSLServerSocket) new ServerSocket(PORT);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		do{
			try (SSLSocket clientSocket = (SSLSocket) serverSocket.accept()){
				ClientHandler handler = new ClientHandler(clientSocket);
				handler.start();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}while (true);
	}

}
