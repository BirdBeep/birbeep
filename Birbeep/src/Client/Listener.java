package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Listener extends Thread {
	private DatagramSocket dtgs;
	private DatagramPacket input;
	
	public void run() {
		try {
			byte[] buffer = new byte[4];
			input = new DatagramPacket(buffer, buffer.length);
			dtgs = new DatagramSocket(60000);
			dtgs.receive(input);
			//PeticionUpdateUsuarios peticion = new PeticionUpdateUsuarios(MainWindow.getUser());
			Sender sender = SSLConexion.getSender();
			//sender.setPeticion(peticion);
			sender.interrupt();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}