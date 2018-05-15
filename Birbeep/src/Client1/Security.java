package Client1;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public class Security {

	//public static void main(String[] args) throws Exception {
		//System.out.println(descrifrar(cifrar("Hola")));
		//System.out.println(descrifrar(cifrar("Adios")));
		//System.out.println(descrifrar(cifrar("Esto es una prueba")));

	//}

	public static KeyStore ks;
	public static FileInputStream ksfis;
	public static BufferedInputStream ksbufin;

	public static Cipher rsa;
	public static Cipher aes;
	public static MessageDigest digest;

	public static Key privateKey, publicKey, key;
	public static KeyGenerator keyGenerator;

	public static String descrifrar(String msg) throws Exception {//PARA EL FUNCIONAMIENTO CON EMISOR Y RECEPTOR HACE FALTA EL PARAM CERT
		// Pasamos el String a byte[]
		byte[] encriptado = msg.getBytes(StandardCharsets.ISO_8859_1);

		// Obtenemos el mensaje encriptado,el hash firmado y la key encriptada
		byte[] encriptado2 = new byte[encriptado.length - 512];
		byte[] firma = new byte[256];
		byte[] encodedKey = new byte[256];
		System.arraycopy(encriptado, 0, encriptado2, 0, encriptado.length - 512);
		System.arraycopy(encriptado, encriptado.length - 512, firma, 0, 256);
		System.arraycopy(encriptado, encriptado.length - 256, encodedKey, 0,
				256);

		// Obtenemos la clave privada del receptor
		ks = KeyStore.getInstance("JKS");
		ksfis = new FileInputStream("src/Main/certs/client3/rubenKey.jks");//OJO al ser el client1 donde se esta ejecutando la app tiene que ser este
		ksbufin = new BufferedInputStream(ksfis);
		ks.load(ksbufin, "aaaaaa".toCharArray());
		privateKey = (PrivateKey) ks.getKey("rubenKey", "aaaaaa".toCharArray());

		// Descriframos la clave simétrica
		rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		rsa.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] bkey = rsa.doFinal(encodedKey);
		key = new SecretKeySpec(bkey, 0, 16, "AES");

		// Obtenemos la clave pública del emisor
		ks = KeyStore.getInstance("JKS");
		ksfis = new FileInputStream("src/Client1/certs/sebasKey.jks");
		ksbufin = new BufferedInputStream(ksfis);
		ks.load(ksbufin, "123456".toCharArray());
		Certificate cert = ks.getCertificate("sebasKey");
		publicKey = (PublicKey) cert.getPublicKey();
		//AQUI SOBRA TODA LA LOGICA, SOLO CON RECIBIR EL CERT POR PARAMETRO Y LA CON ULTIMA LINEA DE CODIGO TIENE QUE VALER

		// Desciframos el hash
		rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		rsa.init(Cipher.DECRYPT_MODE, publicKey);
		byte[] hash = rsa.doFinal(firma);

		// Desciframos el mensaje
		aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
		aes.init(Cipher.DECRYPT_MODE, key);
		byte[] desencriptado2 = aes.doFinal(encriptado2);

		// Calculamos el hash del mensaje
		digest = MessageDigest.getInstance("SHA-256");
		byte[] hash2 = digest.digest(desencriptado2);

		// Pasamos el byte[] a String
		String text = new String(desencriptado2, StandardCharsets.UTF_8);

		// Comparamos el hash del mensaje con el calculado
		if (Arrays.equals(hash, hash2))
			return text;
		else
			return "Error";

	}

	public static String cifrar(String text,Certificate cert) throws Exception {
		// Pasamos el String a byte[]
		byte[] mensaje = text.getBytes(StandardCharsets.UTF_8);

		// Calculamos el hash
		digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(mensaje);

		// Obtenemos la clave privada del emisor
		ks = KeyStore.getInstance("JKS");
		ksfis = new FileInputStream("src/Client1/certs/sebasKey.jks");
		ksbufin = new BufferedInputStream(ksfis);
		ks.load(ksbufin, "123456".toCharArray());
		privateKey = (PrivateKey) ks.getKey("sebasKey", "123456".toCharArray());

		// Firmamos el hash
		rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		rsa.init(Cipher.ENCRYPT_MODE, privateKey);
		byte[] firma = rsa.doFinal(hash);

		// Generamos una clave AES para el cifrado simétrico
		keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(128);
		key = keyGenerator.generateKey();

		// Ciframos el mensaje con la clave generada
		aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
		aes.init(Cipher.ENCRYPT_MODE, key);
		byte[] encriptado = aes.doFinal(mensaje);

		// Obtenemos la suma del mensaje cifrado con su hash firmado
		byte[] mensaje2 = new byte[encriptado.length + 256];
		System.arraycopy(encriptado, 0, mensaje2, 0, encriptado.length);
		System.arraycopy(firma, 0, mensaje2, encriptado.length, 256);

		// Obtenemos la clave publica del receptor
		/*ks = KeyStore.getInstance("JKS");
		ksfis = new FileInputStream("src/Main/certs/client1/sebasKey.jks");
		ksbufin = new BufferedInputStream(ksfis);
		ks.load(ksbufin, "123456".toCharArray());
		Certificate cert = ks.getCertificate("sebasKey");*/
		publicKey = (PublicKey) cert.getPublicKey();

		// Ciframos la clave generada
		rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		rsa.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] encriptado2 = rsa.doFinal(key.getEncoded());

		// Obtenemos el sobre firmado
		byte[] mensaje3 = new byte[mensaje2.length + 256];
		System.arraycopy(mensaje2, 0, mensaje3, 0, mensaje2.length);
		System.arraycopy(encriptado2, 0, mensaje3, mensaje2.length, 256);

		String msg = new String(mensaje3, StandardCharsets.ISO_8859_1);
		return msg;

	}
	
	public static String password (String pass) throws Exception{
		digest= MessageDigest.getInstance("MD5");		 
		return new String(digest.digest(pass.getBytes(StandardCharsets.UTF_8)), StandardCharsets.ISO_8859_1);
		 
	}

}