package Entidades;

public class Mensajes {
	private int id;
	private String emisor;
	private String receptor;
	private String texto;
	private int conver;
	
	public Mensajes(){};
	
	public int getConver() {
		return conver;
	}
	public void setConver(int conver) {
		this.conver = conver;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
}
