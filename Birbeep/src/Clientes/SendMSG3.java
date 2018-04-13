package Clientes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.net.ssl.SSLSocket;
/**
 * 
 * @author Birdbeep 
 * Aquí debe ir toda la lógica de interacción con la interface gráfica
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
