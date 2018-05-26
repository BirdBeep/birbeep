package Main;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Client.EnvCert;
import DAO.ConexionDAO;
import DAO.ConversacionDAO;
import DAO.MensajeDAO;
import DAO.ParticipantesDAO;
import DAO.UsuarioDAO;
import Entidades.ConexionMySQL;
import Entidades.Conexiones;
import Entidades.Conversaciones;
import Entidades.Mensajes;
import Entidades.Participantes;
import Entidades.Peticion;
import Entidades.PeticionCertificado;
import Entidades.PeticionLOGOUT;
import Entidades.PeticionLogin;
import Entidades.PeticionMensaje;
import Entidades.PeticionUPDATEConv;
import Entidades.PeticionUPDATEMsg;
import Entidades.PeticionUpdateUsuarios;
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
	private UsuarioDAO usuarioDAO;
	private MensajeDAO mensajeDAO;
	private ConexionDAO conexionDAO; 
	private ConversacionDAO conversacionDAO;
	private ParticipantesDAO participantesDAO;
	private ConexionMySQL conexion;
	private KeyStore ks;
	private FileInputStream ksfis;
	private BufferedInputStream ksbufin;
	
	/**
	 * Constructor de la clase, inicializa: servicios para interactuar con la capa DAO
	 * @param socket seguro recibido desde el servidor
	 * @throws SQLException si hay problemas al inicializar la conexión
	 */
	public ClientHandler(Socket socket) throws SQLException {
		conexion=new ConexionMySQL();
		client = socket;
		ip=client.getInetAddress().toString();
		usuarioDAO=new UsuarioDAO(conexion.getConexion());
		mensajeDAO=new MensajeDAO(conexion.getConexion());
		conexionDAO=new ConexionDAO(conexion.getConexion());
		conversacionDAO=new ConversacionDAO(conexion.getConexion());
		participantesDAO=new ParticipantesDAO(conexion.getConexion());
		try {
			input = new ObjectInputStream(client.getInputStream());
			output = new ObjectOutputStream(client.getOutputStream());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	public void run() {
		Peticion peticion=null;
		Object obj =null;
		int tipo=0;
		do{
			
			try {
				obj=input.readObject();
				peticion = new JSONDeserializer<Peticion>().deserialize(obj.toString(),Peticion.class);
			} catch (ClassNotFoundException e) {
				System.out.println(e.getMessage());
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			tipo=peticion.getTipo();
			switch (tipo) {
			case 1:
				PeticionLogin peticiononLOGIN = (PeticionLogin) new JSONDeserializer<PeticionLogin>().deserialize(obj.toString(),PeticionLogin.class);
				try {
					login(peticiononLOGIN);
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
				break;
			case 2:
				PeticionUpdateUsuarios peticionUPDATEUsers= (PeticionUpdateUsuarios) new JSONDeserializer<PeticionUpdateUsuarios>().deserialize(obj.toString(),PeticionUpdateUsuarios.class);
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
				break;
			case 5:
				PeticionMensaje peticionMSG= (PeticionMensaje) new JSONDeserializer<PeticionMensaje>().deserialize(obj.toString(),PeticionMensaje.class);
				try {
					recibirMensaje(peticionMSG);
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
				break;
			case 6:
				PeticionCertificado peticionCERT= (PeticionCertificado) new JSONDeserializer<PeticionCertificado>().deserialize(obj.toString(),PeticionCertificado.class);
				try {
					devolverCert(peticionCERT);
				} catch (Exception e1) {
					System.out.println(e1.getMessage());
				}
				break;
			case 7:
				PeticionLOGOUT peticionLOGOUT= (PeticionLOGOUT) new JSONDeserializer<PeticionLOGOUT>().deserialize(obj.toString(),PeticionLOGOUT.class);
				try {
					Usuarios usuario=peticionLOGOUT.getUsuario();
					usuarioDAO.cerrar_usuario(usuario);
				} catch (Exception e1) {
					System.out.println(e1.getMessage());
				}
			default:
				break;
			}
		}while(tipo<7);
	}
	/**
	 * 
	 * @param peticion
	 * @throws IOException
	 */
	private void login (PeticionLogin peticion) throws IOException {
		Usuarios usuario = peticion.getUsuario();
		Usuarios user=usuarioDAO.recuperarUsuario(usuario.getEmail());
		if(user.getPassword()!=null) {
			Conexiones conexion=new Conexiones();
			conexion.setIp(ip);
			conexion.setUser(user.getId());
			conexionDAO.altaConexion(conexion);
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
	private void actualizarUsuarios(PeticionUpdateUsuarios peticion) throws Exception { //PUEDE SER QUE CONVERSACION SEA NULL CUANDO SE ENTABLA POR VEZ PRIMERA???
		Usuarios u = new Usuarios();
		u.setId(peticion.getUsuario().getId());
		Usuarios cli = usuarioDAO.recuperarUsuario(u);
		List<Usuarios> contactos=usuarioDAO.recuperarTodos(cli);
		JSONSerializer serializer = new JSONSerializer(); 
		for(Usuarios us:contactos){
			String ms=serializer.exclude("*.class").serialize( us );
			output.writeObject(ms);
			output.flush();
		}
		output.writeObject(null);
	}
	/**
	 * La peticion ha sido actualizar conversaciones
	 * Recupera todas en las que el "participante" sea el usuario que las solicita
	 * Con lo que devuelve la tabla participantes crea instancias de conversaciones para enviar al cliente
	 * @param peticionUPDATE
	 * @throws IOException 
	 */
	private void actualizarConversaciones(PeticionUPDATEConv peticionUPDATE) throws IOException {
		JSONSerializer serializer = new JSONSerializer(); 
		Usuarios us=new Usuarios();
		us.setId(peticionUPDATE.getUser().getId());
		List<Participantes> convs=new ArrayList<Participantes>();
		convs=participantesDAO.recuperarTodasConv(us);
		for(Participantes p:convs){
			Conversaciones c=new Conversaciones();
			c.setIdConversacion(p.getIdconversacion());
			String ms=serializer.exclude("*.class").serialize(c);
			output.writeObject(ms);
			output.flush();
		}
		output.writeObject(null);
	}
	/**
	 * Recuperamos todos los mensajes de el usuario correspondiente
	 * @param peticionUPDMSG con usuario
	 * @throws IOException 
	 */
	private void actualizarMensajesConv(PeticionUPDATEMsg peticionUPDMSG) throws IOException {
		JSONSerializer serializer = new JSONSerializer(); 
		Usuarios usuario=new Usuarios();
		usuario.setId(peticionUPDMSG.getEmisor().getId());
		List<Mensajes> mensajes=new ArrayList<Mensajes>();
		mensajes=mensajeDAO.recuperarMensajes(usuario);
		for(Mensajes m:mensajes){
			String ms=serializer.exclude("*.class").serialize(m);
			output.writeObject(ms);
			output.flush();
		}
		output.writeObject(null);
	}
	/**
	 * El tipo de petición ha sido "mensaje", creamos una conversación + una conexión, se comprueba si la conversación ya existe:
	 * Dos posibles escenarios -> No existe (gestionConversacion()) o Ya existe (gestionConversacion2())
	 * Se envia un aviso al destinatario.
	 * Se envia una respuesta "OK" al usuario que escribió
	 * @param peticion con los parámetros necesarios (Emisor, receptor, conversación, mensaje)
	 * @throws IOException 
	 */
	private void recibirMensaje(PeticionMensaje peticion) throws IOException {
		Mensajes m = peticion.getMensaje();
		Usuarios userA = new Usuarios();
		Usuarios userB = new Usuarios();
		userA.setId(m.getEmisor());
		userB.setId(m.getReceptor());
		Usuarios usu_emisor=usuarioDAO.recuperarUsuario(userA);
		Usuarios usu_recep=usuarioDAO.recuperarUsuario(userB);
		String id_emisor=usu_emisor.getId();
		String id_receptor=usu_recep.getId();
		int conver=m.getConver();
		int id_conversacion=sacar_id_conversacion(id_emisor,id_receptor,conver);
		Conversaciones conversacion = new Conversaciones();
		conversacion.setIdConversacion(id_conversacion);
		Conexiones conexion=new Conexiones();
		conexion.setIp(ip);
		conexion.setUser(usu_emisor.getId());
		conexionDAO.altaConexion(conexion);
		if(conversacionDAO.altaConversacion(conversacion)){//La conversación es nueva
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
	 * A través de la conversaci�n que viene por la petición, obtiene los correspondientes certificados
	 * A continuación lo enviamos por el flujo
	 * @param peticionCERT
	 * @throws Exception
	 */
	private void devolverCert(PeticionCertificado peticionCERT) throws Exception {
		List<Certificate> lCertificados=new ArrayList<Certificate>();
		Conversaciones conver=new Conversaciones();
		Usuarios user = new Usuarios();
		user.setId(peticionCERT.getUserId());
		conver.setIdConversacion(peticionCERT.getId());
		List<Participantes> participantes=participantesDAO.recuperarPorConv(conver, user);
		for(Participantes p:participantes){
			lCertificados.add(recuperarClave(p.getUser()));
		}
		for(Certificate c:lCertificados){
			EnvCert envio=new EnvCert();
			envio.certificate=c;
			output.writeObject(envio);
			output.flush();
		}
		output.writeObject(null);
		
		/*Usuarios u=new Usuarios();
		u.setId(peticionCERT.getId());
		Usuarios usr=usuarioDAO.recuperarUsuario(u);
		Certificate cert = recuperarClave(usr.getId());
		EnvCert envio=new EnvCert();
		envio.certificate=cert;
		output.writeObject(envio);
		//output.writeObject(new JSONSerializer().exclude("*.class").serialize(envio)); VERSION EnvCert.java EN MAIN TAMBIEN
		output.flush();*/
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
		Conversaciones conv_recup=conversacionDAO.recuperarConv(conversacion.getIdConversacion());//Trabajamos con los objetos ya de la bbdd(en lugar de pasarle el id, recuperamos una conver de la bbdd)
		participantesA.setIdconversacion(conv_recup.getIdConversacion());
		participantesB.setIdconversacion(conv_recup.getIdConversacion());
		mensaje.setReceptor(usu_recep.getId());
		mensaje.setTexto(sobre);
		mensaje.setConver(conv_recup.getIdConversacion());
		participantesDAO.altaParticipantes(participantesA);
		participantesDAO.altaParticipantes(participantesB);
		mensajeDAO.insertarMensaje(mensaje);
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
		Conversaciones conv_recup=conversacionDAO.recuperarConv(conversacion.getIdConversacion());
		mensaje.setEmisor(usu_emisor.getId());
		mensaje.setReceptor(usu_recep.getId());
		mensaje.setTexto(sobre);
		mensaje.setConver(conv_recup.getIdConversacion());
		mensajeDAO.insertarMensaje(mensaje);
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
				c=conexionDAO.obtenerUltimaCon(receptor.getId());//Para saber la última ip que ha tenido el usuario 
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
	
		private int sacar_id_conversacion(String id_user1,String id_user2,int conver){
			int id_conver=0;
			int tamano=0;
			List<Integer> array1=new ArrayList<Integer>();
			List<Integer> array2=new ArrayList<Integer>();
			List <Integer> mayor=new ArrayList<Integer>();
			List <Integer> menor=new ArrayList<Integer>();
			
			array1=participantesDAO.recuperar_id_conversacion(id_user1);
			array2=participantesDAO.recuperar_id_conversacion(id_user2);
			
			if(array1.isEmpty()==false && array2.isEmpty()==false) {
				if(array1.size()>=array2.size()){
					for(int i=0;i<array1.size();i++){
						mayor.add(i,array1.get(i));
					}
					for(int i=0;i<array2.size();i++){
						menor.add(i,array2.get(i));
					}
				}else{
					if(array1.size()<=array2.size()){
						for(int i=0;i<array2.size();i++){
							mayor.add(i,array2.get(i));
						}
						for(int i=0;i<array1.size();i++){
							menor.set(i,array1.get(i));
						}
					}else{
						for(int i=0;i<array2.size();i++){
							mayor.add(i,array1.get(i));
							menor.add(i,array2.get(i));
						}
					}
				}
				for(int i=0;i<menor.size();i++){
					if(mayor.get(i).equals(menor.get(i))){
						id_conver=mayor.get(i);
						break;
					}
				}
				if(conver==id_conver && id_conver!=0){
					id_conver=conver;
			}else {
				tamano=mayor.size();
				id_conver=mayor.get(tamano-1)+1;
			}
			}else {if(array1.isEmpty()==true && array2.isEmpty()==false ) {
				tamano=array2.size();
				id_conver=array2.get(tamano-1)+1;
			}else {if(array2.isEmpty()==true && array1.isEmpty()==false) {
				tamano=array1.size();
				id_conver=array1.get(tamano-1)+1;
			}
				id_conver=1;
			}	
			}
		return id_conver;
		}
}


