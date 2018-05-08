package Client1;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocket;

import com.google.gson.Gson;

import Client1.Modelo.Certificado;
import Client1.Modelo.Conversaciones;
import Client1.Modelo.Mensajes;
import Client1.Modelo.Peticion;
import Client1.Modelo.PeticionMSG;
import Client1.Modelo.PeticionUPDATEUser;
import Client1.Modelo.Usuarios;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.transformer.IterableTransformer;

public class Sender extends Thread {
	private SSLSocket socket;
	private Peticion peticion;
	private List<Usuarios> usuarios;
	private List<Mensajes> mensajes;
	public void run() {
		socket = SSLConexion.getSocket();
		if(socket!=null){
			do {
				try {
					TimeUnit.DAYS.sleep(30);
				} catch (InterruptedException ie) {
					ObjectOutputStream oos=null;
					ObjectInputStream ois=null;
					try {
						oos = new ObjectOutputStream(socket.getOutputStream());
						JSONSerializer serializer = new JSONSerializer(); 
						String ms=serializer.exclude("*.class").serialize(peticion);
						oos.writeObject(ms);
						oos.flush();
						ois = new ObjectInputStream(socket.getInputStream());
						int tipo=peticion.getTipo();
						switch (tipo) {
						case 1://Solicitud de LOGIN
							Usuarios user= new JSONDeserializer<Usuarios>().deserialize(ois.readObject().toString(),Usuarios.class);
							SSLConexion.setUser(user);
							break;
						case 2://Actualizar USUARIOS
							do{
								Usuarios usuario= new JSONDeserializer<Usuarios>().deserialize(ois.readObject().toString(),Usuarios.class);
								usuarios.add(usuario);
							}while(ois.readObject()!=null);
							break;
						case 3://Conversaciones
							Conversaciones conver=null;
							List<Conversaciones> convs=new ArrayList<Conversaciones>();
							try{
								do{
									conver= new JSONDeserializer<Conversaciones>().deserialize(ois.readObject().toString(),Conversaciones.class);
									convs.add(conver);
								}while(ois.readObject()!=null);
							}catch(EOFException e){
								System.out.println(convs.get(0).getIdConversacion());
							}
							break;
						case 4://Actualizar mensajes
							Mensajes mensaje=null;
							List<Mensajes> mnsjs=new ArrayList<Mensajes>();
							try{
								do{
									mensaje= new JSONDeserializer<Mensajes>().deserialize(ois.readObject().toString(),Mensajes.class);
									mnsjs.add(mensaje);
								}while(ois.readObject()!=null);
							}catch(EOFException e){
								System.out.println(Security.descrifrar(mnsjs.get(mnsjs.size()-1).getTexto()));
							}
							break;
						case 5://Enviar mensaje
							//Mensajes mensaje = new JSONDeserializer<Mensajes>().deserialize(ois.readObject().toString(),Mensajes.class);
							//System.out.println(Security.descrifrar(mensaje.getTexto()));
							break;
						case 6://Certificado
							PublicKey cert= new JSONDeserializer<PublicKey>().deserialize(ois.readObject().toString(),PublicKey.class);//Probar con Key en lugar de Certificate
							//SSLConexion.setCert(cert);
							System.out.println(cert.toString());
							break;
						case 7://LOGOUT
							break;
						default:
							break;
						}
						oos.close();	
						ois.close();
					} catch (IOException | ClassNotFoundException ioe) {
						ioe.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
	
				}
			} while (true);
		}
	}

	public Peticion getPeticion() {
		return peticion;
	}

	public void setPeticion(Peticion peticion) {
		this.peticion = peticion;
	}

}