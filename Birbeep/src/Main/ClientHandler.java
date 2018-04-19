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

import DAO.ComunicacionDAO;
import DAO.ConexionDAO;
import DAO.UsuarioDAO;
import Entidades.ConexionMySQL;
import Entidades.Conexiones;
import Entidades.Usuarios;
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
	private Socket client;
	private SSLSession sslSession;
	private Scanner input;
	private PrintWriter output;
	private KeyManager[] cert;
	private static final int PORT_UDP = 60000;
	private static final String TOKEN="true";//Esto avisa al cliente de que tiene mensajes para leer..
	private UsuarioDAO serv1;
	private ComunicacionDAO serv2;
	private ConexionDAO serv3; 
	ConexionMySQL con;
	String ipDestino;
	
	/**
	 * Constructor de la clase, inicializa: la sesión del cliente, un servicio para interactuar con la capa DAO y
	 * los flujos del socket.
	 * @param socket seguro recibido desde el servidor
	 * @param keyManagers con los certificados desde el servidor
	 * @throws SQLException si hay problemas al inicializar la conexión
	 */
	public ClientHandler(Socket socket,KeyManager[] keyManagers) throws SQLException {
		con=new ConexionMySQL();
		client = socket;
		//sslSession=client.getSession();//Obtenemos la sesión
		//cert=keyManagers;
		serv1=new UsuarioDAO(con.getConexion());
		serv2=new ComunicacionDAO(con.getConexion());
		serv3=new ConexionDAO(con.getConexion());
		try {
			input = new Scanner(client.getInputStream());
			output = new PrintWriter(client.getOutputStream(), true);
		} catch (IOException ioEx) {
			System.out.println(ioEx.getMessage());
		}
	}

	public void run() {
		String receptor=input.nextLine();//Primer campo con el receptor
		String emisor=input.nextLine();//Segundo campo con el emisor
		Usuarios usu_recep=null;
		Usuarios user = new Usuarios();
		user.setId(receptor);
		try {
			serv3.altaConexion(client.getInetAddress().toString(),emisor);
			usu_recep=serv1.recuperarUsuario(user);
			serv2.insertarMensaje(usu_recep.getId(),emisor,input.nextLine());//Tercer campo con el sobre
			sendToken(usu_recep);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		/**
		try {
			String usr=sslSession.getPeerPrincipal().getName();//Sabemos el nombre del cliente que se ha conectado.
			for (KeyManager k : cert){
				if (client.getSession().getPeerCertificates()[0]==k){
					//Aqui ya hemos comprobado que es quien dice ser
					//to-do Hacer la compresión HASH y almacenar en BBDD el mensaje
					
					//recuperar el id de cliente destinatario desde el mensaje enviado(pasar el id al siguiente método)
					//sendToken(input.nextLine());
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
		}*/
	}
	
	/**
	 * Envia un token UDP al cliente correspondiente (cli) para avisar de que hay nuevos mensajes
	 * @param cli que nos lo proporciona el mensaje desde el cliente emisor
	 */
		private void sendToken(Usuarios cli) {
			Conexiones c=null;
			try {
				c=serv3.obtenerUltimaCon(cli.getId());
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			DatagramSocket socketUDP=null;
			try {
				socketUDP=new DatagramSocket();
				DatagramPacket outPacket=new DatagramPacket(TOKEN.getBytes(),TOKEN.length(),InetAddress.getByName(c.getIp()),PORT_UDP);
				socketUDP.send(outPacket);
			} catch (IOException eSocketEx) {//Incluye la "SocketException"
				System.out.println("El cliente no está operativo!");
				System.out.println(eSocketEx.getMessage());
			}finally{//El socket UDP está enviado (y)
				socketUDP.close();
			}
		}

}
