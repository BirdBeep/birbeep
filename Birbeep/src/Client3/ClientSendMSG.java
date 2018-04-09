package Client3;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
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
 * Envia un mensaje seguro al servidor para que lo gestione, el mensaje debe contener información 
 * del destinatario
 *
 */
public class ClientSendMSG implements Runnable {
	private static final int PORT = 465;//Evita la excepción "Unrecognized SSL message,plain text conection?" 587
	private static InetAddress server;
	@Override
	public void run() {
		TrustManager[] trustManagers=null;
		KeyManager[] keyManagers=null;
		try {
			server=InetAddress.getLocalHost();
			trustManagers = getTrusts();
			keyManagers = getKeys();
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(keyManagers, trustManagers, null);

			SSLSocketFactory ssf = sc.getSocketFactory();
			SSLSocket client = (SSLSocket) ssf.createSocket(server, PORT);
			client.startHandshake();
			
			Scanner input = new Scanner(client.getInputStream());
			PrintWriter output = new PrintWriter(client.getOutputStream(), true);
			output.println("client1");//Este es el destinatario
			
	}catch(NoSuchAlgorithmException e) {//Para cuando obtiene el contexto
		System.out.println(e.getMessage());
	} catch (KeyStoreException e) {//Para el almacen de certificados de confianza
		System.out.println(e.getMessage());
	} catch (CertificateException e) {
		System.out.println(e.getMessage());
	} catch (UnrecoverableKeyException e) {//Para obtener su propio certificado (Metodo getKEys())
		System.out.println(e.getMessage());
	} catch (FileNotFoundException e) {//Para cuando recupera el fichero de la ruta 
		System.out.println(e.getMessage());
	} catch (IOException e) {
		System.out.println(e.getMessage());
	} catch (KeyManagementException e) {//Para cuando inicializa el alamcen y su certificado en el contexto SSL
		System.out.println(e.getMessage());
	}
	}
	/**
	 * 
	 * @return Array con los certificados de confianza, las excepciones siguientes son tratadas en el "run()"
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static TrustManager[] getTrusts() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {
		KeyStore trustedStore = KeyStore.getInstance("JKS");
		trustedStore.load(new FileInputStream("src/Main/certs/client3/rubenTrustedCerts.jks"), "333333".toCharArray());

		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(trustedStore);

		TrustManager[] trustManagers = tmf.getTrustManagers();
		return trustManagers;
	}
	/**
	 * 
	 * @return Array con la clave pública, las excepciones siguientes son tratadas en el "run()"
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws UnrecoverableKeyException
	 */
	private static KeyManager[] getKeys() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException {
		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(new FileInputStream("src/Main/certs/client3/rubenKey.jks"),"aaaaaa".toCharArray());

		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(keyStore, "aaaaaa".toCharArray());

		KeyManager[] keyManagers = kmf.getKeyManagers();
		return keyManagers;
	}
}
