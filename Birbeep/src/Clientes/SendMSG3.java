package Clientes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.net.ssl.SSLSocket;
/**
 * 
 * @author Birdbeep 
 * Aqu� debe ir toda la l�gica de interacci�n con la interface gr�fica
 *
 */
public class SendMSG3 extends Thread{
	private SSLSocket client;
	public SendMSG3(SSLSocket client){
		this.client=client;
	}
	
	public void run(){
		try {
			Scanner input = new Scanner(client.getInputStream());
			PrintWriter output = new PrintWriter(client.getOutputStream(), true);
			output.println("client1");//Este es el destinatario
		} catch (IOException e) {
			System.out.println(e.getMessage());;
		}
	}

}
