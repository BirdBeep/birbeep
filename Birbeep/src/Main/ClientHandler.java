package Main;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Scanner;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import DAO.UsuarioDAO;
import Entidades.ConexionMySQL;
import Entidades.Usuario;
/**
 * 
 * @author Birdbeep
 * 
 * Se encarga de guardar el mensaje recibido en la BBDD y 
 * avisar mediante un token UDP al cliente que tiene nuevos mensajes
 * 
 *
 */

class ClientHandler extends Thread {
	private SSLSocket client;
	private SSLSession sslSession;
	private Scanner input;
	private PrintWriter output;
	private KeyManager[] cert;
	private static final int PORT_UDP = 4321;
	private static final String TOKEN="true";//Esto avisa al cliente de que tiene mensajes para leer..
	private UsuarioDAO serv;
	ConexionMySQL con;
	String ipDestino;
	
	/**
	 * Constructor de la clase, inicializa: la sesión del cliente, un servicio para interactuar con la capa DAO y
	 * los flujos del socket.
	 * @param socket seguro recibido desde el servidor
	 * @param keyManagers con los certificados desde el servidor
	 * @throws SQLException si hay problemas al inicializar la conexión
	 */
	public ClientHandler(SSLSocket socket,KeyManager[] keyManagers) throws SQLException {
		con=new ConexionMySQL();
		client = socket;
		sslSession=client.getSession();//Obtenemos la sesión
		cert=keyManagers;
		serv=new UsuarioDAO(con.getConexion());
		try {
			input = new Scanner(client.getInputStream());
			output = new PrintWriter(client.getOutputStream(), true);
		} catch (IOException ioEx) {
			System.out.println(ioEx.getMessage());
		}
	}
/**
 * Envia un token UDP al cliente correspondiente (cli) para avisar de que hay nuevos mensajes
 * @param cli que nos lo proporciona el mensaje desde el cliente emisor
 */
	private void sendToken(String cli) {
		Usuario user = new Usuario();
		user.setId(cli);
		try {
			Usuario actual=serv.recuperarUsuario(user);
			ipDestino=actual.getIp();
		} catch (SQLException e) {//para la conexion, va a tratar tambien en el caso que se propague desde DAO
			System.out.println(e.getMessage());
		}
		DatagramSocket socketUDP=null;
		try {
			socketUDP=new DatagramSocket();
			DatagramPacket outPacket=new DatagramPacket(TOKEN.getBytes(),TOKEN.length(),null,PORT_UDP);//FALTA LA IP DE DESTINO (tiene que ser la obtenida en idpDestino)
			socketUDP.send(outPacket);
		} catch (IOException eSocketEx) {//Incluye la "SocketException"
			System.out.println("El cliente no está operativo!");
			System.out.println(eSocketEx.getMessage());
		}finally{//El socket UDP está enviado (y)
			socketUDP.close();
		}
	}

	public void run() {
		try {
			String usr=sslSession.getPeerPrincipal().getName();//Sabemos el nombre del cliente que se ha conectado.
			for (KeyManager k : cert){
				if (client.getSession().getPeerCertificates()[0]==k){
					//Aqui ya hemos comprobado que es quien dice ser
					//to-do Hacer la compresión HASH y almacenar en BBDD el mensaje
					
					//recuperar el id de cliente destinatario desde el mensaje enviado(pasar el id al siguiente método)
					sendToken(input.nextLine());
				}else{
					System.out.println("No es un cliente de alta");
				}
			}
		} catch (SSLPeerUnverifiedException e) {
			System.out.println(e.getMessage());
		}
		String received="";
		do {
			// Accept message from client on
			// the socket's input stream...
			//received = input.nextLine(); SE HA IGUALADO LA VAR A VACIO Y EVITA UNA EXCEPTION DE TIPO "No line found"

			// Echo message back to client on
			// the socket's output stream...
			output.println("ECHO: " + received);

			// Repeat above until '***CLOSE***' sent by client...
		} while (!received.equals("***CLOSE***"));

		try {
			if (client != null) {
				System.out.println("Closing down connection...");
				client.close();
			}
		} catch (IOException ioEx) {
			System.out.println("Unable to disconnect!");
		}
	}
}
