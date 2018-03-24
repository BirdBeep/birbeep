import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
/**
 * 
 * @author BirdBeep
 * 
 * Clase principal del servidor, abre el Socket de escucha y se queda esperando en un try con recursos el desafio de un cliente
 * Mediante el socket de dialogo se llama a una clase que extiende de Thread para procesar los mensajes
 *
 */
public class Server {
	private static final int PORT = 1234;
	private static ServerSocket serverSocket;
	public static void main(String[] args) {
		TrustManager[] trustManagers=null;
		KeyManager[] keyManagers=null;
		try {
			trustManagers = getTrusts();
			keyManagers = getKeys();

			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(keyManagers, trustManagers, null);

			SSLServerSocketFactory ssf = sc.getServerSocketFactory();
			serverSocket = ssf.createServerSocket(PORT);
		} catch (IOException e) {//Para cuando crea el socket
			System.out.println(e.getMessage());
		} catch (NoSuchAlgorithmException e) {//Para cuando obtiene el contexto
			System.out.println(e.getMessage());
		} catch (KeyStoreException e) {//Para el almacen de certificados de confianza
			System.out.println(e.getMessage());
		} catch (CertificateException e) {
			System.out.println(e.getMessage());
		} catch (KeyManagementException e) {//Para cuando obtiene certificado propio desde el almacen de confianza
			System.out.println(e.getMessage());
		} catch (UnrecoverableKeyException e) {//Para obtener su propio certificado (Metodo getKEys())
			System.out.println(e.getMessage());
		}
		do{
			try (SSLSocket clientSocket = (SSLSocket) serverSocket.accept()){
				for (KeyManager k : keyManagers){
					if (clientSocket.getSession().getPeerCertificates()[0]==k){
						ClientHandler handler = new ClientHandler(clientSocket);
						handler.start();	
					}else{
						System.out.println("No es un cliente de alta");
					}
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}while (true);
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
		trustedStore.load(new FileInputStream("src/main/certs/server/serverTrustedCerts.jks"), "servpass".toCharArray());

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
		keyStore.load(new FileInputStream("src/main/certs/server/serverKey.jks"),"servpass".toCharArray());

		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(keyStore, "servpass".toCharArray());

		KeyManager[] keyManagers = kmf.getKeyManagers();
		return keyManagers;
	}

}
