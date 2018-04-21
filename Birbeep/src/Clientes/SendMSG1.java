package Clientes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.net.ssl.SSLSocket;

public class SendMSG1 extends Thread{
	private SSLSocket client;
	/**
	 * Cuando se instancia el thread se inicializa el socket seguro de di�logo
	 * @param client
	 */
	public SendMSG1(SSLSocket client){
		this.client=client;
	}
	/**
	 * 
	 * @author Birdbeep 
	 * Aqu� debe ir toda la l�gica de interacci�n con la interface gr�fica
	 *
	 */
	public void run(){
		try {
			Scanner input = new Scanner(client.getInputStream());//Debe recibir los mensajes pendientes
			PrintWriter output = new PrintWriter(client.getOutputStream(), true);
			output.println("client3");//Este es el receptor HAY QUE ENVIAR EL ID, quiz� un fichero de properties para almacenar los contactos
			output.println("client1");//Emisor, ENVIAR ID!!!!
			output.println("probando....");//Mensaje(Esto va en un sobre encriptado)Se obtendr� de una clase programada por Sebas
			} catch (IOException e) {
			System.out.println(e.getMessage());;
		}
	}

}