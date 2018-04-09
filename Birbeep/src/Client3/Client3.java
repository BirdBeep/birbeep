package Client3;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Scanner;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
/**
 * 
 * @author Birdbeep
 * 
 * Envia un aviso al servidor de que está conectado para que éste actualice su estado (Evento lanzado por la interfaz de user??)
 * Instancia un hilo para escuchar los posibles mensajes UDP del server
 * Instancia un hilo para enviar mensajes TCP seguros al server
 * Inicia los hilos.
 *
 */

public class Client3 {
	private static final int PORT_UDP = 1234;
	private static InetAddress server;
	private static final String TOKEN = "client3";
	public static void main(String[] args) {
		DatagramSocket socketUDP=null;
		try {
			server=InetAddress.getLocalHost();
		} catch (UnknownHostException inetAddrEx) {//Para recuperar la direccion del servidor
			System.out.println(inetAddrEx.getMessage());
		}
		try {
			socketUDP=new DatagramSocket();
			DatagramPacket outPacket=new DatagramPacket(TOKEN.getBytes(),TOKEN.length(),server,PORT_UDP);
			socketUDP.send(outPacket);
		} catch (IOException eSocketEx) {//Incluye la "SocketException"
			System.out.println("El servidor no está operativo!");
			System.out.println(eSocketEx.getMessage());
		}finally{//El socket UDP está enviado (y)
			socketUDP.close();
		}
		ClientListenUDP task1=new ClientListenUDP();//Para recibir notificaciones por parte del server
		Thread t1=new Thread(task1);
		
		ClientSendMSG task2=new ClientSendMSG();//Para interactuar con el PUF para enviar mensajes
		Thread t2=new Thread(task2);
		t1.start();
		t2.start();
	}
}
