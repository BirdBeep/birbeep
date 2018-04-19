package Clientes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.net.ssl.SSLSocket;

public class SendMSG1 extends Thread{
	private SSLSocket client;
	public SendMSG1(SSLSocket client){
		this.client=client;
	}
	/**
	 * 
	 * @author Birdbeep 
	 * Aquí debe ir toda la lógica de interacción con la interface gráfica
	 *
	 */
	public void run(){
		try {
			Scanner input = new Scanner(client.getInputStream());//Debe recibir los mensajes pendientes
			PrintWriter output = new PrintWriter(client.getOutputStream(), true);
			output.println("Ruben");//Este es el receptor HAY QUE ENVIAR EL ID
			output.println("Sebas");//Emisor, ENVIAR ID!!!!
			output.println("probando....");//Mensaje(Esto va en un sobre encriptado)Se obtendrá de una clase programada por Sebas
			} catch (IOException e) {
			System.out.println(e.getMessage());;
		}
	}

}