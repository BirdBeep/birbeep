package Client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import Client.Modelo.PeticionMensaje;
import Client.Modelo.Usuarios;
import Client.Modelo.Mensajes;
import Client.Modelo.PeticionCertificado;

public class SSLConexion extends Thread{
	private static final int PORT = 12345;
	private static  InetAddress server;
	private static  SSLContext sc;
	private static SSLSocketFactory ssf;
	private static SSLSocket socket;
	private static Sender sender = new Sender();
	private static Listener listener = new Listener();
	private static Certificate cert;
	
	
	public void run() {
		TrustManager[] trustManagers = null;
		KeyManager[] keyManagers = null;
		try {
			server = InetAddress.getLocalHost();
			try {
				trustManagers = getTrusts();
			} catch (KeyStoreException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (CertificateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				keyManagers = getKeys();
			} catch (UnrecoverableKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (KeyStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CertificateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sc = SSLContext.getInstance("TLS");
			sc.init(keyManagers, trustManagers, null);

			ssf = sc.getSocketFactory();
			socket = (SSLSocket) ssf.createSocket(server, PORT);
			socket.startHandshake();

			sender.start();
			listener.start();

		} catch (NoSuchAlgorithmException e) {// Para cuando obtiene el contexto
			System.out.println(e.getMessage());
//		} catch (KeyStoreException e) {// Para el almacen de certificados de
//										// confianza
//			System.out.println(e.getMessage());
//		} catch (CertificateException e) {
//			System.out.println(e.getMessage());
//		} catch (UnrecoverableKeyException e) {// Para obtener su propio
//												// certificado (Metodo
												// getKEys())
			System.out.println(e.getMessage());
		} catch (FileNotFoundException e) {// Para cuando recupera el fichero de
											// la ruta
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (KeyManagementException e) {// Para cuando inicializa el alamcen
											// y su certificado en el contexto
											// SSL
			System.out.println(e.getMessage());
		}
	}

	private static TrustManager[] getTrusts() throws KeyStoreException,
			NoSuchAlgorithmException, CertificateException,
			FileNotFoundException, IOException {
		KeyStore trustedStore = KeyStore.getInstance("JKS");
		trustedStore.load(new FileInputStream(
				"src/Main/certs/client1/sebasTrustedCerts.jks"), "111111"
				.toCharArray());

		TrustManagerFactory tmf = TrustManagerFactory
				.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(trustedStore);

		TrustManager[] trustManagers = tmf.getTrustManagers();
		return trustManagers;
	}

	private static KeyManager[] getKeys() throws KeyStoreException,
			NoSuchAlgorithmException, CertificateException,
			FileNotFoundException, IOException, UnrecoverableKeyException {
		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(
				new FileInputStream("src/Main/certs/client1/sebasKey.jks"),
				"123456".toCharArray());

		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory
				.getDefaultAlgorithm());
		kmf.init(keyStore, "123456".toCharArray());

		KeyManager[] keyManagers = kmf.getKeyManagers();
		return keyManagers;
	}

	public static InetAddress getServer() {
		return server;
	}

	public static void setServer(InetAddress server) {
		SSLConexion.server = server;
	}

	public static SSLContext getSc() {
		return sc;
	}

	public static void setSc(SSLContext sc) {
		SSLConexion.sc = sc;
	}

	public static SSLSocketFactory getSsf() {
		return ssf;
	}

	public static void setSsf(SSLSocketFactory ssf) {
		SSLConexion.ssf = ssf;
	}

	public static SSLSocket getSocket() {
		return socket;
	}

	public static void setSocket(SSLSocket socket) {
		SSLConexion.socket = socket;
	}

	public static Sender getSender() {
		return sender;
	}

	public static void setSender(Sender sender) {
		SSLConexion.sender = sender;
	}

	public static Listener getListener() {
		return listener;
	}

	public static void setListener(Listener listener) {
		SSLConexion.listener = listener;
	}

	public static int getPort() {
		return PORT;
	}
	
	public static void setCert(Certificate cert){
		SSLConexion.cert=cert;
	}
	
	public static Certificate getCert(){
		return cert;
	}

}