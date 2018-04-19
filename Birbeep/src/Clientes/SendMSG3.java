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
	/**
	 * Cuando se instancia el thread se inicializa el socket seguro de diálogo
	 * @param client
	 */
	public SendMSG3(SSLSocket client){
		this.client=client;
	}
	
	public void run(){
		try {
			Scanner input = new Scanner(client.getInputStream());//Debe recibir los mensajes pendientes
			PrintWriter output = new PrintWriter(client.getOutputStream(), true);
			output.println("Sebas");//Este es el receptor HAY QUE ENVIAR EL ID,  quizá un fichero de properties para almacenar los contactos
			output.println("Ruben");//Emisor HAY QUE ENVIAR EL ID
			output.println("probando....");//Mensaje(Esto va en un sobre encriptado)Se obtendrá de una clase programada por Sebas
		} catch (IOException e) {
			System.out.println(e.getMessage());;
		}
	}

}
