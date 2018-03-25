import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Scanner;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
/**
 * 
 * @author Birdbeep
 * 
 * Envia un aviso al servidor de que está conectado para que éste actualice su estado
 * Se conecta al servidor mediante el contexto SSL
 * Abre los flujos I/O para el intercambio de mensajes
 *
 */

public class Client1 {
	private static final int PORT = 1234;
	private static InetAddress server;
	private static final String TOKEN = "true";
	public static void main(String[] args) {
		TrustManager[] trustManagers=null;
		KeyManager[] keyManagers=null;
		try {
			server=InetAddress.getLocalHost();
		} catch (UnknownHostException inetAddr) {//Para recuperar la direccion del servidor
			inetAddr.getMessage();
		}
		try {
			DatagramSocket socketUDP=new DatagramSocket();
			DatagramPacket outPacket=new DatagramPacket(TOKEN.getBytes(),TOKEN.length(),server,PORT);
			socketUDP.send(outPacket);
		} catch (IOException eSocket) {//Incluye la "SocketException"
			eSocket.getMessage();
		}
		try {
			trustManagers = getTrusts();
			keyManagers = getKeys();
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(keyManagers, trustManagers, null);

			SSLSocketFactory ssf = sc.getSocketFactory();
			SSLSocket client = (SSLSocket) ssf.createSocket(server, PORT);
			client.startHandshake();
			
			Scanner input = new Scanner(client.getInputStream());
			PrintWriter output = new PrintWriter(client.getOutputStream(), true);
			
	}catch(NoSuchAlgorithmException e) {//Para cuando obtiene el contexto
		System.out.println(e.getMessage());
	} catch (KeyStoreException e) {//Para el almacen de certificados de confianza
		System.out.println(e.getMessage());
	} catch (CertificateException e) {
		System.out.println(e.getMessage());
	} catch (UnrecoverableKeyException e) {//Para obtener su propio certificado (Metodo getKEys())
		System.out.println(e.getMessage());
	} catch (FileNotFoundException e) {//Para cuando recupera el fichero de la ruta 
		e.getMessage();
	} catch (IOException e) {
		e.getMessage();
	} catch (KeyManagementException e) {//Para cuando inicializa el alamcen y su certificado en el contexto SSL
		e.getMessage();
	}
	}
	/**
	 * 
	 * @return Array con los certificados de confianza
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static TrustManager[] getTrusts() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {
		KeyStore trustedStore = KeyStore.getInstance("JKS");
		trustedStore.load(new FileInputStream("src/main/certs/client1/rubenTrustedCerts.jks"), "aaaaa".toCharArray());

		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(trustedStore);

		TrustManager[] trustManagers = tmf.getTrustManagers();
		return trustManagers;
	}
	/**
	 * 
	 * @return Array con la clave pública
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws UnrecoverableKeyException
	 */
	private static KeyManager[] getKeys() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException {
		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(new FileInputStream("src/main/certs/client1/rubenKey.jks"),"aaaaa".toCharArray());

		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(keyStore, "aaaaa".toCharArray());

		KeyManager[] keyManagers = kmf.getKeyManagers();
		return keyManagers;
	}
}
