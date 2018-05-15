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

import com.google.gson.Gson;

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
import Entidades.PeticionCERT;
import Entidades.PeticionLOGIN;
import Entidades.PeticionMSG;
import Entidades.PeticionUPDATEConv;
import Entidades.PeticionUPDATEMsg;
import Entidades.PeticionUPDATEUsers;
import Entidades.Usuarios;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.transformer.IterableTransformer;

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
		Peticion peticion=null;
		Object obj =null;
		do{
			try {
				obj=input.readObject();
				peticion = new JSONDeserializer<Peticion>().deserialize(obj.toString(),Peticion.class);
			} catch (ClassNotFoundException e) {
				System.out.println(e.getMessage());
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			int tipo=peticion.getTipo();
			switch (tipo) {
			case 1:
				PeticionLOGIN peticiononLOGIN = (PeticionLOGIN) new JSONDeserializer<PeticionLOGIN>().deserialize(obj.toString(),PeticionLOGIN.class);
				try {
					login(peticiononLOGIN);
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			case 2:
				PeticionUPDATEUsers peticionUPDATEUsers= (PeticionUPDATEUsers) new JSONDeserializer<PeticionUPDATEUsers>().deserialize(obj.toString(),PeticionUPDATEUsers.class);
				try {
					actualizarUsuarios(peticionUPDATEUsers);
				} catch (Exception e) { //Trata la SQLException y las relativas a la obtencion de clave
					System.out.println(e.getMessage());
				}
				break;
			case 3:
				PeticionUPDATEConv peticionUPDATE= (PeticionUPDATEConv) new JSONDeserializer<PeticionUPDATEConv>().deserialize(obj.toString(),PeticionUPDATEConv.class);
				try {
					actualizarConversaciones(peticionUPDATE);
				} catch (Exception e) { //Trata la SQLException y las relativas a la obtencion de clave
					System.out.println(e.getMessage());
				}
				break;
			case 4:
				PeticionUPDATEMsg peticionUPDMSG=(PeticionUPDATEMsg)new JSONDeserializer<PeticionUPDATEMsg>().deserialize(obj.toString(),PeticionUPDATEMsg.class);
				try {
					actualizarMensajesConv(peticionUPDMSG);
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			case 5:
				PeticionMSG peticionMSG= (PeticionMSG) new JSONDeserializer<PeticionMSG>().deserialize(obj.toString(),PeticionMSG.class);
				try {
					recibirMensaje(peticionMSG);
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
				break;
			case 6:
				PeticionCERT peticionCERT= (PeticionCERT) new JSONDeserializer<PeticionCERT>().deserialize(obj.toString(),PeticionCERT.class);
				try {
					devolverCert(peticionCERT);
				} catch (Exception e1) {
					System.out.println(e1.getMessage());
				}
				break;
			default:
				break;
			}
		}while(true);
	}
	/**
	 * 
	 * @param peticion
	 * @throws IOException
	 */
	private void login (PeticionLOGIN peticion) throws IOException {
		Usuarios usuario = peticion.getUsuario();
		Usuarios user=serv1.recuperarUsuario(usuario.getEmail());
		System.out.println(user.getPassword());
		if(user.getPassword()!=null) {
			Conexiones conexion=new Conexiones();
			conexion.setIp(ip);
			conexion.setUser(user.getId());
			serv3.altaConexion(conexion);
			if(usuario.getPassword().compareTo(user.getPassword())==0){
				JSONSerializer serializer = new JSONSerializer();
				String ms=serializer.exclude("*.class").serialize(user);
				output.writeObject(ms);
				output.flush();
			}else{
				user.setNombre("nopass");
				JSONSerializer serializer = new JSONSerializer(); 
				String ms=serializer.exclude("*.class").serialize(user);
				output.writeObject(ms);
				output.flush();
			}
		}else {
			System.out.println("aqui");
			user.setNombre("nouser");
			JSONSerializer serializer = new JSONSerializer(); 
			String ms=serializer.exclude("*.class").serialize(user);
			output.writeObject(ms);
			output.flush();
		}

}
	/**
	 * El tipo de petición ha sido "actualización", recuperamos la info de todos los usuarios dados de alta en la app
	 * @param peticion 
	 * @throws Exception capturada en el "run()" para SQLException
	 * 
	 */
	private void actualizarUsuarios(PeticionUPDATEUsers peticion) throws Exception { //PUEDE SER QUE CONVERSACION SEA NULL CUANDO SE ENTABLA POR VEZ PRIMERA???
		Usuarios u = new Usuarios();
		u.setId(peticion.getUsuario().getId());
		Usuarios cli = serv1.recuperarUsuario(u);
		List<Usuarios> contactos=serv1.recuperarTodos(cli);
		JSONSerializer serializer = new JSONSerializer(); 
		for(Usuarios us:contactos){
			String ms=serializer.exclude("*.class").serialize( us );
			output.writeObject(ms);
			output.flush();
		}
		output.close();
	}
	/**
	 * 
	 * @param peticionUPDATE
	 * @throws IOException 
	 */
	private void actualizarConversaciones(PeticionUPDATEConv peticionUPDATE) throws IOException {
		JSONSerializer serializer = new JSONSerializer(); 
		Usuarios us=new Usuarios();
		us.setId(peticionUPDATE.getUser().getId());
		List<Participantes> convs=new ArrayList<Participantes>();
		convs=serv5.recuperarTodasConv(us);
		for(Participantes p:convs){
			Conversaciones c=new Conversaciones();
			c.setIdConversacion(p.getIdconversacion());
			String ms=serializer.exclude("*.class").serialize(c);
			output.writeObject(ms);
			output.flush();
		}
		output.close();
	}
	/**
	 * Recuperamos los mensajes de la conversacion en la que participan los dos usuarios correspondientes
	 * Obtenemos las conversaciones en las que participan cada uno
	 * Las comparamos y de la que coincida obtenemos los mensajes
	 * @param peticionUPDMSG con emisor y receptor para obtener los mensajes en los que estos participan
	 * @throws IOException 
	 */
	private void actualizarMensajesConv(PeticionUPDATEMsg peticionUPDMSG) throws IOException {
		JSONSerializer serializer = new JSONSerializer(); 
		Usuarios emisor=new Usuarios();
		emisor.setId(peticionUPDMSG.getEmisor().getId());
		List<Mensajes> mensajes=new ArrayList<Mensajes>();
		mensajes=serv2.recuperarMensajes(emisor);
		for(Mensajes m:mensajes){
			String ms=serializer.exclude("*.class").serialize(m);
			output.writeObject(ms);
			output.flush();
		}
		output.close();
	}
	/**
	 * El tipo de petición ha sido "mensaje", creamos una conversación + una conexión, se comprueba si la conversación ya existe:
	 * Dos posibles escenarios -> No existe (gestionConversacion()) o Ya existe (gestionConversacion2())
	 * Se envia un aviso al destinatario.
	 * Se envia una respuesta "OK" al usuario que escribió
	 * @param peticion con los parámetros necesarios (Emisor, receptor, conversación, mensaje)
	 * @throws IOException 
	 */
	private void recibirMensaje(PeticionMSG peticion) throws IOException {
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
		//sendToken(usu_recep);
		JSONSerializer serializer = new JSONSerializer(); 
		String ms=serializer.exclude("*.class").serialize( m );
		output.writeObject(ms);
	}
	/**
	 * A través del usuario que viene por la petición, obtiene su correspondiente certificado
	 * A continuación lo enviamos por el flujo
	 * @param peticionCERT
	 * @throws Exception
	 */
	private void devolverCert(PeticionCERT peticionCERT) throws Exception {
		Usuarios u=new Usuarios();
		u.setId(peticionCERT.getId());
		Usuarios usr=serv1.recuperarUsuario(u);
		Certificate cert = recuperarClave(usr.getId());
		//Key key = cert.getPublicKey(); Serializar Key en lugar de Certificate???????????
		JSONSerializer serializer = new JSONSerializer(); 
		String ms=serializer.exclude("*.class").serialize(cert);
		output.writeObject(ms);
		output.flush();
		output.close();
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
	private Certificate recuperarClave(String propietario) throws Exception{
		Certificate cert=null;
		if(propietario.equals("client1")){
			ks = KeyStore.getInstance("JKS");
			ksfis = new FileInputStream("src/Main/certs/client1/sebasKey.jks");//Tiene que estar tambien en el server para mandar la clave publica al resto de users
			ksbufin = new BufferedInputStream(ksfis);
			ks.load(ksbufin, "123456".toCharArray());
			cert = ks.getCertificate("sebasKey");
		}
		if(propietario.equals("client2")){
			ks = KeyStore.getInstance("JKS");
			ksfis = new FileInputStream("src/Main/certs/client2/luismiKey.jks");
			ksbufin = new BufferedInputStream(ksfis);
			ks.load(ksbufin, "567890".toCharArray());
			cert = ks.getCertificate("luismiKey");
		}
		if(propietario.equals("client3")){
			ks = KeyStore.getInstance("JKS");
			ksfis = new FileInputStream("src/Main/certs/client3/rubenKey.jks");
			ksbufin = new BufferedInputStream(ksfis);
			ks.load(ksbufin, "aaaaaa".toCharArray());
			cert = ks.getCertificate("rubenKey");
		}
		return cert;
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


