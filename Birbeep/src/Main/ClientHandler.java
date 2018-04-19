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

import DAO.MensajeDAO;
import DAO.ParticipantesDAO;
import DAO.ConexionDAO;
import DAO.ConversacionDAO;
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
	private Scanner input;
	private PrintWriter output;
	private static final int PORT_UDP = 60000;
	private static final String TOKEN="true";//Esto avisa al cliente de que tiene mensajes para leer..
	private UsuarioDAO serv1;
	private MensajeDAO serv2;
	private ConexionDAO serv3; 
	private ConversacionDAO serv4;
	private ParticipantesDAO serv5;
	private ConexionMySQL con;
	
	/**
	 * Constructor de la clase, inicializa: servicios para interactuar con la capa DAO y
	 * los flujos del socket.
	 * @param socket seguro recibido desde el servidor
	 * @throws SQLException si hay problemas al inicializar la conexión
	 */
	public ClientHandler(Socket socket) throws SQLException {
		con=new ConexionMySQL();
		client = socket;
		serv1=new UsuarioDAO(con.getConexion());
		serv2=new MensajeDAO(con.getConexion());
		serv3=new ConexionDAO(con.getConexion());
		serv4=new ConversacionDAO(con.getConexion());
		serv5=new ParticipantesDAO(con.getConexion());
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
			serv4.altaConversacion();
			serv3.altaConexion(client.getInetAddress().toString(),emisor);
			usu_recep=serv1.recuperarUsuario(user);
			serv5.altaParticipantes(emisor,usu_recep.getId(),serv4.recuperarConv());//Chirria un poco tener que dar de alta para luego cogerla en el mismo método
			serv2.insertarMensaje(usu_recep.getId(),emisor,input.nextLine(),serv5.recuperarPartConv(emisor,usu_recep.getId()));//Tercer campo con el sobre
			//sendToken(usu_recep);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Envia un token UDP al cliente correspondiente (cli) para avisar de que hay nuevos mensajes
	 * @param cli que nos lo proporciona el mensaje desde el cliente emisor
	 * @throws SQLException La tratará run
	 */
		private void sendToken(Usuarios cli) throws SQLException {
			Conexiones c=null;
			DatagramSocket socketUDP=null;
			try {
				c=serv3.obtenerUltimaCon(cli.getId());
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
