package Client3.Modelo;

public class Mensaje {
	private String emisor;
	private String receptor;
	private String texto;
	private String conver;
	
	public Mensaje(){};
	
	public Mensaje(String emisor,String receptor,String texto,String conver){
		this.emisor=emisor;
		this.receptor=receptor;
		this.texto=texto;
		this.conver=conver;
	}
	
	public String getEmisor() {
		return emisor;
	}
	public void setEmisor(String emisor) {
		this.emisor = emisor;
	}
	public String getReceptor() {
		return receptor;
	}
	public void setReceptor(String receptor) {
		this.receptor = receptor;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public String getConver() {
		return conver;
	}

	public void setConver(String conver) {
		this.conver = conver;
	}

}
