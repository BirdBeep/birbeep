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
			output.println("Sebas");//Este es el receptor HAY QUE ENVIAR EL ID
			output.println("probando....");//Mensaje(Esto va en un sobre encriptado)Se obtendr� de una clase programada por Sebas
			output.println("Ruben");//Emisor, todavia no se ha encontrado utilidad a este campo
		} catch (IOException e) {
			System.out.println(e.getMessage());;
		}
	}

}
