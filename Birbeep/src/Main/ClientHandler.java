package Main;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
import Entidades.Conversaciones;
import Entidades.Mensajes;
import Entidades.Participantes;
import Entidades.Peticion;
import Entidades.PeticionMSG;
import Entidades.Usuarios;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
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
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private static final int PORT_UDP = 60000;
	private static final String TOKEN="true";//Esto avisa al cliente de que tiene mensajes para leer..
	private String ip;
	private UsuarioDAO serv1;
	private MensajeDAO serv2;
	private ConexionDAO serv3; 
	private ConversacionDAO serv4;
	private ParticipantesDAO serv5;
	private ConexionMySQL con;
	private KeyStore ks;
	private FileInputStream ksfis;
	private BufferedInputStream ksbufin;
	
	/**
	 * Constructor de la clase, inicializa: servicios para interactuar con la capa DAO y
	 * los flujos del socket.
	 * @param socket seguro recibido desde el servidor
	 * @throws SQLException si hay problemas al inicializar la conexión
	 */
	public ClientHandler(Socket socket) throws SQLException {
		con=new ConexionMySQL();
		client = socket;
		ip=client.getInetAddress().toString();
		serv1=new UsuarioDAO(con.getConexion());
		serv2=new MensajeDAO(con.getConexion());
		serv3=new ConexionDAO(con.getConexion());
		serv4=new ConversacionDAO(con.getConexion());
		serv5=new ParticipantesDAO(con.getConexion());
		try {
			input = new ObjectInputStream(client.getInputStream());
			output = new ObjectOutputStream(client.getOutputStream());
		} catch (IOException ioEx) {
			System.out.println(ioEx.getMessage());
		}
	}
	public void run() {
		PeticionMSG p=null;//antes Peticion p=null, pero no se puede deserializar porque esta clase no tiene el atributo mensaje
		try {
			p = new JSONDeserializer<PeticionMSG>().deserialize(input.readObject().toString(),PeticionMSG.class);
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		int tipo=p.getTipo();
		switch (tipo) {
		case 1:
			try {
				actualizar(p);
			} catch (Exception e) { //Trata la SQLException y las relativas a la obtención de clave
				System.out.println(e.getMessage());
			}
			break;
		case 2:
			//PeticionMSG peticion = (PeticionMSG) p;
			recibirMensaje(p);
			break;

		default:
			break;
		}
	}
	/**
	 * El tipo de petición ha sido "actualización", recuperamos la info de quién es quien ha solicitado esta petición y
	 * de que conversación se trata, recuperamos una lista de mensajes de la bbdd y los enviamos a por el socket
	 * @param peticion con los parámetros necesarios (Emisor, conversación)
	 * @throws Exception capturada en el "run()" para SQLException y para la obtención de clave pública
	 * 
	 */
	private void actualizar(PeticionMSG peticion) throws Exception { //PUEDE SER QUE CONVERSACION SEA NULL CUANDO SE ENTABLA POR VEZ PRIMERA???
		Usuarios u = new Usuarios();
		u.setId(peticion.getMensaje().getEmisor());//HABRÁ QUE CREAR UNA "PETICION" QUE TENGA EL CAMPO "USUARIO" PARA EL "TIPO 1"
		Usuarios cli = serv1.recuperarUsuario(u);
		Conversaciones conver=new Conversaciones();
		conver.setIdConversacion(peticion.getMensaje().getConver());
		List<Mensajes> mensajes=new ArrayList<Mensajes>();
		mensajes=serv2.recuperarMensajes(cli,conver);
		String clavePub=recuperarClave(peticion.getMensaje().getReceptor());//NECESITA EL CAMPO "RECEPTOR" EN LA PETICION
		//mensajes.add(clavePub); LA IDEA ES INCLUIR COMO ULTIMO "MENSAJE" LA CLAVE PÚBLICA, PERO ENTONCES HAY QUE MANDAR SOLO EL TEXTO DE LOS MENSAJES
		JSONSerializer serializer = new JSONSerializer(); 
		String ms=serializer.serialize( mensajes );
		output.writeObject(ms);
		output.flush();
		output.close();
	}
	/**
	 * El tipo de petición ha sido "mensaje", creamos una conversación + una conexión, se comprueba si la conversación ya existe:
	 * Dos posibles escenarios -> No existe (gestionConversacion()) o Ya existe (gestionConversacion2())
	 * Finalmente se envia un aviso al destinatario.
	 * @param peticion con los parámetros necesarios (Emisor, receptor, conversación, mensaje)
	 */
	private void recibirMensaje(PeticionMSG peticion) {
		Mensajes m = peticion.getMensaje();
		Usuarios userA = new Usuarios();
		Usuarios userB = new Usuarios();
		userA.setId(m.getEmisor());
		userB.setId(m.getReceptor());
		Usuarios usu_emisor=serv1.recuperarUsuario(userA);
		Usuarios usu_recep=serv1.recuperarUsuario(userB);	
		Conversaciones conversacion = new Conversaciones();
		conversacion.setIdConversacion(m.getConver());
		Conexiones conexion=new Conexiones();
		conexion.setIp(ip);
		conexion.setUser(usu_emisor.getId());
		serv3.altaConexion(conexion);
		if(serv4.altaConversacion(conversacion)){//La conversación es nueva
			gestionConversacion(conversacion,usu_emisor,usu_recep,m.getTexto());
		}else{//Existia la conver
			gestionConversacion2(conversacion,usu_emisor,usu_recep,m.getTexto());
		}
		sendToken(usu_recep);
	}
	/**
	 * Es la primera vez que intercambian información emisor y receptor
	 * Crea las instancias de los participantes que intervienen y una de mensaje
	 * @param conversacion fue dada de alta anteriormente 
	 * @param usu_emisor fue recuperado de la bbdd según la info de quien entabló conversación
	 * @param usu_recep fue recuperado de la bbdd según la info de quién entabló conversación
	 * @param sobre viene de quién entabló conversación
	 */
	private void gestionConversacion(Conversaciones conversacion, Usuarios usu_emisor, Usuarios usu_recep, String sobre) {
		Participantes participantesA=new Participantes();
		Participantes participantesB=new Participantes();
		Mensajes mensaje=new Mensajes();
		
		participantesA.setUser(usu_emisor.getId());
		mensaje.setEmisor(usu_emisor.getId());
		participantesB.setUser(usu_recep.getId());
		Conversaciones conv_recup=serv4.recuperarConv(conversacion.getIdConversacion());//Trabajamos con los objetos ya de la bbdd(en lugar de pasarle el id, recuperamos una conver de la bbdd)
		participantesA.setIdconversacion(conv_recup.getIdConversacion());
		participantesB.setIdconversacion(conv_recup.getIdConversacion());
		mensaje.setReceptor(usu_recep.getId());
		mensaje.setTexto(sobre);
		mensaje.setConver(conv_recup.getIdConversacion());
		serv5.altaParticipantes(participantesA);
		serv5.altaParticipantes(participantesB);
		serv2.insertarMensaje(mensaje);
	}
	/**
	 * Ya existen datos en relación a quién interviene y se van a relacionar los mensajes con la conversación que ya existe
	 * @param conversacion la que se dió de alta la primera vez
	 * @param usu_emisor fue recuperado de la bbdd según la info de quien entabló conversación 
	 * @param usu_recep fue recuperado de la bbdd según la info de quien entabló conversación
	 * @param sobre viene de quién entabló conversación
	 */
	private void gestionConversacion2(Conversaciones conversacion, Usuarios usu_emisor, Usuarios usu_recep, String sobre) {
		Mensajes mensaje=new Mensajes();
		Conversaciones conv_recup=serv4.recuperarConv(conversacion.getIdConversacion());
		mensaje.setEmisor(usu_emisor.getId());
		mensaje.setReceptor(usu_recep.getId());
		mensaje.setTexto(sobre);
		mensaje.setConver(conv_recup.getIdConversacion());
		serv2.insertarMensaje(mensaje);
	}
	/**
	 * Devuelve la clave del receptor correspondiente en la conversación iniciada
	 * @param propietario viene de la petición en el cliente
	 * @return clave con la clave publica del correspondiente usuario
	 * @throws Exception capturada en "run()"
	 */
	private String recuperarClave(String propietario) throws Exception{
		Key clave=null;
		if(propietario.equals("client1")){
			ks = KeyStore.getInstance("JKS");
			ksfis = new FileInputStream("src/Main/certs/client1/sebasKey.jks");
			ksbufin = new BufferedInputStream(ksfis);
			ks.load(ksbufin, "123456".toCharArray());
			Certificate cert =  ks.getCertificate("sebasKey");
			clave = (PublicKey) cert.getPublicKey();
		}
		if(propietario.equals("client2")){
			ks = KeyStore.getInstance("JKS");
			ksfis = new FileInputStream("src/Main/certs/client2/luismiKey.jks");
			ksbufin = new BufferedInputStream(ksfis);
			ks.load(ksbufin, "567890".toCharArray());
			Certificate cert =  ks.getCertificate("luismiKey");
			clave = (PublicKey) cert.getPublicKey();
		}
		if(propietario.equals("client3")){
			ks = KeyStore.getInstance("JKS");
			ksfis = new FileInputStream("src/Main/certs/client3/rubenKey.jks");
			ksbufin = new BufferedInputStream(ksfis);
			ks.load(ksbufin, "aaaaaa".toCharArray());
			Certificate cert =  ks.getCertificate("rubenKey");
			clave = (PublicKey) cert.getPublicKey();
		}
		return clave.toString();//COMPROBAR QUE ES LA CLAVE HECHA CADENA, NO LA DIRECCIÓN DE MEMORIA DE LA INSTANCIA
	}
	/**
	 * Envia un token UDP al receptor correspondiente (receptor) para avisar de que hay nuevos mensajes o de que se actualice, depende de "Peticion"
	 * @param receptor que nos lo proporciona el mensaje desde el cliente emisor
	 * @throws SQLException La tratará run
	 */
		private void sendToken(Usuarios receptor) {
			Conexiones c=null;
			DatagramSocket socketUDP=null;
			try {
				c=serv3.obtenerUltimaCon(receptor.getId());//Para saber la última ip que ha tenido el usuario 
				socketUDP=new DatagramSocket();
				DatagramPacket outPacket=new DatagramPacket(TOKEN.getBytes(),TOKEN.length(),InetAddress.getByName(c.getIp()),PORT_UDP);
				//socketUDP.send(outPacket); HAY QUE CONTEMPLAR LA RECEPCION EN LA PARTE CLIENTE
			} catch (IOException eSocketEx) {//Incluye la "SocketException"
				System.out.println("El cliente no está operativo!");
				System.out.println(eSocketEx.getMessage());
			}finally{//El socket UDP está enviado (y)
				socketUDP.close();
			}
		}

}

