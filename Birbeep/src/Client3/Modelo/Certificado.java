package Client3.Modelo;

import java.security.cert.Certificate;

public class Certificado {
	private Certificate c;
	public Certificate getC() {
		return c;
	}
	public Certificado(){};
	public Certificado(Certificate c){
		this.c=c;
	}
}
