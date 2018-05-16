package Client.Modelo;

public class Mensajes {
	private String emisor;
	private String receptor;
	private String texto;
	private int conver;
	
	public Mensajes(){};
	
	public Mensajes(String emisor,String receptor,String texto,int conver){
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
	public int getConver() {
		return conver;
	}

	public void setConver(int conver) {
		this.conver = conver;
	}

}
