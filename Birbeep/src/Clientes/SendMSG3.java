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
			output.println("Sebas");//Este es el receptor HAY QUE ENVIAR EL ID
			output.println("probando....");//Mensaje(Esto va en un sobre encriptado)Se obtendrá de una clase programada por Sebas
			output.println("Ruben");//Emisor, todavia no se ha encontrado utilidad a este campo
		} catch (IOException e) {
			System.out.println(e.getMessage());;
		}
	}

}
