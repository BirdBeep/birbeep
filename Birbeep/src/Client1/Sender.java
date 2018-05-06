package Client1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.net.ssl.SSLSocket;

import Client1.Modelo.Mensaje;
import Client1.Modelo.Peticion;
import Client1.Modelo.PeticionMSG;
import flexjson.JSONSerializer;

public class Sender extends Thread {
	private SSLSocket socket;
	private Peticion peticion;
	
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
		if(socket==(null))
			System.out.println("Mal");
		do {
			try {
				Sender.sleep(9999999);
			} catch (InterruptedException ie) {
				ObjectOutputStream oos=null;
				ObjectInputStream ois=null;
				try {
					oos = new ObjectOutputStream(socket.getOutputStream());
					//ois = new ObjectInputStream(socket.getInputStream()); //SE QUEDA AQUÍ YA QUE NO VIENE NADA POR EL SOCKET
					try{
						ois.readObject();
						peticion=(Peticion) ois.readObject();
						ois.close();
					}catch(NullPointerException e){
						JSONSerializer serializer = new JSONSerializer(); 
						String ms=serializer.exclude("*.class").serialize( peticion );
						oos.writeObject(ms);
						oos.flush();
						oos.close();
					}
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