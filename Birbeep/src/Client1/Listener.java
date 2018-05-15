package Client1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import Client1.Modelo.PeticionUPDATEUsers;

public class Listener extends Thread {
	private DatagramSocket dtgs;
	private DatagramPacket input;

	public void run() {
		try {
			byte[] buffer = new byte[4];
			input = new DatagramPacket(buffer, buffer.length);
			dtgs = new DatagramSocket(60000);
			dtgs.receive(input);
			//PeticionUPDATEUsers peticion = new PeticionUPDATEUsers(1,SSLConexion.getUser());
			Sender sender = SSLConexion.getSender();
			//sender.setPeticion(peticion);
			sender.interrupt();//sender.notify();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}