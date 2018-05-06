package Client3;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
//import java.security.Certificate;
import java.security.Key;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public class Security {

	public static void main(String[] args) throws Exception {
		String sobre=cifrar("Hola");
		descrifrar(sobre);
	}

	public static KeyStore ks;
	public static FileInputStream ksfis;
	public static BufferedInputStream ksbufin;

	public static Cipher rsa;
	public static Cipher aes;
	public static MessageDigest digest;
	
	public static Key privateKey, publicKey, key;
	
	public static KeyGenerator keyGenerator;
	

	public static String descrifrar(String encriptado) throws Exception {
		// Obtener la clase para desencriptar asimétricamente
		ks = KeyStore.getInstance("JKS");
		ksfis = new FileInputStream("src/Main/certs/client1/sebasKey.jks");
		ksbufin = new BufferedInputStream(ksfis);
		ks.load(ksbufin, "123456".toCharArray());
		privateKey = (PrivateKey) ks.getKey("sebasKey", "123456".toCharArray());

		rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		rsa.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] desencriptado = rsa.doFinal(encriptado.getBytes());

		// Obtener la key
		String MSG = new String(desencriptado);
		String[] MENSAJE = MSG.split(":");
		String encriptado2 = MENSAJE[0];

		byte[] encodedKey = MENSAJE[1].getBytes();
		key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");

		// Obtener la clase para desencriptar asimétricamente
		aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
		aes.init(Cipher.DECRYPT_MODE, key);
		byte[] desencriptado2 = aes.doFinal(encriptado2.getBytes());

		// Obtener el mensaje y el hash
		String msg = new String(desencriptado2);
		String[] mensaje = msg.split(":");
		String text = mensaje[0];
		String hash = mensaje[1];

		// Calculamos el hash
		digest = MessageDigest.getInstance("SHA-256");
		byte[] hash2 = digest.digest(text.getBytes(StandardCharsets.UTF_8));

		// Comparamos el hash del mensaje con el calculado
		if (hash.equals(hash2))
			return text;
		else
			return "Error";

	}

	public static String cifrar(String text) throws Exception {
		// Calculamos el hash
		digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));

		// Obtenemos el mensaje de la suma del texto con su hash
		String msg = text + ":" + hash;

		// Generamos una clave de 128 bits adecuada para AES
		keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(128);
		key = keyGenerator.generateKey();
		key = new SecretKeySpec(hash, 0, 16, "AES");

		// Obtener la clase para encriptar simétricamente
		aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
		aes.init(Cipher.ENCRYPT_MODE, key);
		byte[] encriptado = aes.doFinal(msg.getBytes());

		String MSG = encriptado + ":" + key;

		ks = KeyStore.getInstance("JKS");
		ksfis = new FileInputStream("src/Main/certs/client1/sebasKey.jks");
		ksbufin = new BufferedInputStream(ksfis);
		ks.load(ksbufin, "123456".toCharArray());
		Certificate cert = (Certificate) ks.getCertificate("sebasKey");
		publicKey = (PublicKey) cert.getPublicKey();

		rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		rsa.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] encriptado2 = rsa.doFinal(MSG.getBytes());
		String mensaje = new String(encriptado2);

		return mensaje;

	}
}