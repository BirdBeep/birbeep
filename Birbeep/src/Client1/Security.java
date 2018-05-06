package Client1;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.cert.Certificate;
import java.util.Arrays;

public class Security {

	public static void main(String[] args) throws Exception {
		//System.out.println(descrifrar(cifrar("Hola")));
		//System.out.println(descrifrar(cifrar("Adios")));
		//System.out.println(descrifrar(cifrar("Esto es una prueba")));
		
		
		
		//System.out.println(cifrar("Hola"));
	}

	public static KeyStore ks;
	public static FileInputStream ksfis;
	public static BufferedInputStream ksbufin;

	public static Cipher rsa;
	public static Cipher aes;
	public static MessageDigest digest;
	
	public static Key privateKey, publicKey, key;
	
	public static KeyGenerator keyGenerator;
	

	public static String descrifrar(byte[] encriptado) throws Exception {
		// Obtener la clase para desencriptar asimétricamente
		ks = KeyStore.getInstance("JKS");
		ksfis = new FileInputStream("src/Main/certs/client1/sebasKey.jks");
		ksbufin = new BufferedInputStream(ksfis);
		ks.load(ksbufin, "123456".toCharArray());
		privateKey = (PrivateKey) ks.getKey("sebasKey", "123456".toCharArray());

		rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		rsa.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] desencriptado = rsa.doFinal(encriptado);

		// Obtener la key
		byte[] encriptado2 = new byte[desencriptado.length-16];
		byte[] encodedKey = new byte[16];
		System.arraycopy(desencriptado, 0, encriptado2, 0, desencriptado.length-16);
		System.arraycopy(desencriptado, desencriptado.length-16, encodedKey,0, 16);
		key = new SecretKeySpec(encodedKey, 0, 16, "AES");

		// Obtener la clase para desencriptar simétricamente
		aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
		aes.init(Cipher.DECRYPT_MODE, key);
		byte[] desencriptado2 = aes.doFinal(encriptado2);

		// Obtener el mensaje y el hash
		byte[] mensaje = new byte[desencriptado2.length-32];
		byte[] hash = new byte[32];
		System.arraycopy(desencriptado2, 0, mensaje, 0, desencriptado2.length-32);
		System.arraycopy(desencriptado2, desencriptado2.length-32, hash,0,32);

		// Calculamos el hash
		digest = MessageDigest.getInstance("SHA-256");
		byte[] hash2 = digest.digest(mensaje);
		
		String text = new String(mensaje);
		// Comparamos el hash del mensaje con el calculado
		if (Arrays.equals(hash,hash2))
			return text;
		else
			return "Error";

	}

	public static String cifrar(String text) throws Exception {
		byte [] mensaje = text.getBytes(StandardCharsets.UTF_8);
		// Calculamos el hash
		digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(mensaje);
		
		
		// Obtenemos el mensaje de la suma del texto con su hash
		byte[] mensaje2 = new byte[mensaje.length + 32];
		System.arraycopy(mensaje, 0, mensaje2, 0, mensaje.length);
		System.arraycopy(hash, 0, mensaje2, mensaje.length, 32);
		

		// Generamos una clave de 128 bits adecuada para AES
		keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(128);
		key = keyGenerator.generateKey();
		key = new SecretKeySpec(hash, 0, 16, "AES");

		// Obtener la clase para encriptar simétricamente
		aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
		aes.init(Cipher.ENCRYPT_MODE, key);
		byte[] encriptado = aes.doFinal(mensaje2);
		
		// Obtenemos el mensaje de la suma del texto+hash con su key
		byte[] mensaje3 = new byte[encriptado.length + 16];
		System.arraycopy(encriptado, 0, mensaje3, 0, encriptado.length);
		System.arraycopy(key.getEncoded(), 0, mensaje3, encriptado.length, 16);

		ks = KeyStore.getInstance("JKS");
		ksfis = new FileInputStream("src/Main/certs/client1/sebasKey.jks");
		ksbufin = new BufferedInputStream(ksfis);
		ks.load(ksbufin, "123456".toCharArray());
		Certificate cert =  ks.getCertificate("sebasKey");
		publicKey = (PublicKey) cert.getPublicKey();

		rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		rsa.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] encriptado2 = rsa.doFinal(mensaje3);
		
		return encriptado2.toString();

	}
	

}