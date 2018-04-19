package Clientes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.net.ssl.SSLSocket;

public class SendMSG1 extends Thread{
	private SSLSocket client;
	/**
	 * Cuando se instancia el thread se inicializa el socket seguro de diálogo
	 * @param client
	 */
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
			output.println("client3");//Este es el receptor HAY QUE ENVIAR EL ID, quizá un fichero de properties para almacenar los contactos
			output.println("client1");//Emisor, ENVIAR ID!!!!
			output.println("probando....");//Mensaje(Esto va en un sobre encriptado)Se obtendrá de una clase programada por Sebas
			} catch (IOException e) {
			System.out.println(e.getMessage());;
		}
	}

}