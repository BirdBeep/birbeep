package Main;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.SQLException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
/**
 * 
 * @author BirdBeep
 * 
 * Clase principal del servidor, se mantiene a la escucha por si se envia un mensaje y debe procesarlo y enviarlo
 *
 */
public class Server {
	private static final int PORT = 12345;
	private static ServerSocket serverSocket;
	public static void main(String[] args){
		TrustManager[] trustManagers=null;
		KeyManager[] keyManagers=null;
		try {
			trustManagers = getTrusts();
			keyManagers = getKeys();

			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(keyManagers, trustManagers, null);

			SSLServerSocketFactory ssf = sc.getServerSocketFactory();
			serverSocket = ssf.createServerSocket(PORT);
			
			do{
				Socket dialogo = (SSLSocket) serverSocket.accept();//Esperando...
				ClientHandler clienthandler = new ClientHandler(dialogo,keyManagers);
				clienthandler.start();
			}while(true);
			
		} catch (IOException | SQLException socketEx) {//Aqui está tambien la SQL que se propaga desde los Handler
			System.out.println(socketEx.getMessage());
		} catch (NoSuchAlgorithmException e) {//Para cuando obtiene el contexto
			System.out.println(e.getMessage());
		} catch (KeyStoreException e) {//Para el almacen de certificados de confianza
			System.out.println(e.getMessage());
		} catch (CertificateException e) {
			System.out.println(e.getMessage());
		} catch (KeyManagementException e) {//Para cuando obtiene certificado propio desde el almacen de confianza
			System.out.println(e.getMessage());
		} catch (UnrecoverableKeyException e) {//Para obtener su propio certificado (Metodo getKeys())
			System.out.println(e.getMessage());
		}
	}
	/**
	 * 
	 * @return Array con los certificados de confianza, las excepciones siguientes son tratadas en el "main()"
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static TrustManager[] getTrusts() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {
		KeyStore trustedStore = KeyStore.getInstance("JKS");
		trustedStore.load(new FileInputStream("src/Main/certs/server/serverTrustedCerts.jks"), "000000".toCharArray());

		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(trustedStore);

		TrustManager[] trustManagers = tmf.getTrustManagers();
		return trustManagers;
	}
	/**
	 * 
	 * @return Array con la clave pública, las excepciones siguientes son tratadas en el "main()"
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws UnrecoverableKeyException
	 */
	private static KeyManager[] getKeys() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException {
		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(new FileInputStream("src/Main/certs/server/serverKey.jks"),"servpass".toCharArray());

		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(keyStore, "servpass".toCharArray());

		KeyManager[] keyManagers = kmf.getKeyManagers();
		return keyManagers;
	}	
}
