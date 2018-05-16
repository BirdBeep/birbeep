package Main;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

public class EnvCert implements Serializable{
public transient Certificate certificate;
private void writeObject(ObjectOutputStream out)throws IOException {
		out.defaultWriteObject();
		try {
			out.writeObject(certificate.getEncoded());
		} catch (CertificateEncodingException cee) {
			throw new IOException("Can't serialize object " + cee);
		}
	}
private void readObject(ObjectInputStream in)throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		try {
			byte b[] = (byte []) in.readObject();
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			certificate = cf.generateCertificate(new 
	        ByteArrayInputStream(b));
		} catch (CertificateException ce) {
			throw new IOException("Can't de-serialize object " + ce);
		}
	}
}
