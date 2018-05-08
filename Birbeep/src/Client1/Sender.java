package Client1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.cert.Certificate;

import javax.net.ssl.SSLSocket;

import Client1.Modelo.Certificado;
import Client1.Modelo.Mensaje;
import Client1.Modelo.Peticion;
import Client1.Modelo.PeticionMSG;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class Sender extends Thread {
	private SSLSocket socket;
	private Peticion peticion;
	private JSONSerializer serializer ;
	private Certificado certificado;
	private Certificate cert;
	
	public static void main(String [] args){
		SSLConexion.initSSLConexion();
		PeticionMSG p = new PeticionMSG(2,new Mensaje("client1","client3","Probando...","conv01"));
		Sender s= new Sender();
		s.start();
		s.setPeticion(p);
		s.interrupt();
	}

	public void run() {
		socket = SSLConexion.getSocket();
		serializer= new JSONSerializer(); 
		if(socket==(null))
			System.out.println("Mal");
		//////////////////////////////// Para recibir el certificado /////////////////////////////////////
		PeticionMSG p = new PeticionMSG(3,null);
		String pet=serializer.exclude("*.class").serialize( p );
		try {
			ObjectOutputStream oos1=new ObjectOutputStream(socket.getOutputStream());
			oos1.writeObject(pet);
			oos1.flush();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		try {
			ObjectInputStream ois1=new ObjectInputStream(socket.getInputStream());
			certificado = new JSONDeserializer<Certificado>().deserialize(ois1.readObject().toString(),Certificado.class);
			System.out.println("El certificado: "+certificado);
		} catch (IOException | ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		//////////////////////////// Fin recibir certificado ///////////////////////////////////////////////
		do {
			try {
				Sender.sleep(9999999);
			} catch (InterruptedException ie) {
				ObjectOutputStream oos=null;
				ObjectInputStream ois=null;
				try {
					oos = new ObjectOutputStream(socket.getOutputStream());
					String ms=serializer.exclude("*.class").serialize( peticion );
					oos.writeObject(ms);
					//oos.flush();
					//oos.close();
					ois = new ObjectInputStream(socket.getInputStream());
					Mensaje m = new JSONDeserializer<Mensaje>().deserialize(ois.readObject().toString(),Mensaje.class);
					Security s= new Security();
					try {
						System.out.println(s.descrifrar(m.getTexto()));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//ois.close();
				} catch (IOException | ClassNotFoundException ioe) {
					ioe.printStackTrace();
				}

			}
		} while (true);
	}

	public Peticion getPeticion() {
		return peticion;
	}

	public void setPeticion(Peticion peticion) {
		this.peticion = peticion;
	}

}