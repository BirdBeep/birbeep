package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.cert.Certificate;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocket;

import Client.GUI.MainWindow;
import Client.Modelo.Conversaciones;
import Client.Modelo.Mensajes;
import Client.Modelo.Peticion;
import Client.Modelo.Usuarios;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class Sender extends Thread {
	private SSLSocket socket;
	private Peticion peticion;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private List<Usuarios> usuarios;
	private List<Conversaciones> convers;
	private List<Mensajes> mensajes;
	private boolean ok = true;
	public void run() {
		socket = SSLConexion.getSocket();
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if(socket!=null){
			do {
				try {
					TimeUnit.DAYS.sleep(30);
				} catch (InterruptedException ie) {
					try {		
						JSONSerializer serializer = new JSONSerializer(); 
						String ms=serializer.exclude("*.class").serialize(peticion);
						oos.writeObject(ms);
						oos.flush();
						int tipo=peticion.getTipo();
						switch (tipo) {
						case 1:
							Usuarios usuario= new JSONDeserializer<Usuarios>().deserialize(ois.readObject().toString(),Usuarios.class);
							MainWindow.setUser(usuario);
							MainWindow.getInterfaz().interrupt();		
							break;	
						case 2:
							Object obj=null;
							do {
								obj=ois.readObject();
								if(obj!=null) {
									usuario= new JSONDeserializer<Usuarios>().deserialize(obj.toString(),Usuarios.class);
									MainWindow.usuarios.add(usuario);
								}							
							}while(obj!=null);
							MainWindow.getInterfaz().interrupt();
							break;
						case 3:
							obj=null;
							do {
								obj=ois.readObject();
								if(obj!=null) {
									Conversaciones conver= new JSONDeserializer<Conversaciones>().deserialize(obj.toString(),Conversaciones.class);
									MainWindow.convers.add(conver);
								}							
							}while(obj!=null);
							MainWindow.getInterfaz().interrupt();
							break;
						case 4:
							obj=null;
							do {
								obj=ois.readObject();
								if(obj!=null) {
									Mensajes mensaje= new JSONDeserializer<Mensajes>().deserialize(obj.toString(),Mensajes.class);
									MainWindow.mensajes.add(mensaje);
								}							
							}while(obj!=null);
							MainWindow.getInterfaz().interrupt();
							break;
						case 5:
							Mensajes mensaje = new JSONDeserializer<Mensajes>().deserialize(ois.readObject().toString(),Mensajes.class);
							System.out.println(Security.descrifrar(mensaje.getTexto()));
							break;
						case 6:
							Object o = ois.readObject();
							if (o instanceof EnvCert){
								EnvCert certif=(EnvCert) o;
								SSLConexion.setCert(certif.certificate);
							}
							break;
						case 7:
							ok=false;
							break;
						default:
							break;
						}
					} catch (ClassNotFoundException ioe) {
						ioe.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					} 	
				}
			} while (ok);
		}
	}

	public Peticion getPeticion() {
		return peticion;
	}

	public void setPeticion(Peticion peticion) {
		this.peticion = peticion;
	}

}