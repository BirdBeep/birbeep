package Entidades;

public class Mensajes {
	private String id;
	private Usuarios emisor;
	private Usuarios receptor;
	private String texto;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Usuarios getEmisor() {
		return emisor;
	}
	public void setEmisor(Usuarios emisor) {
		this.emisor = emisor;
	}
	public Usuarios getReceptor() {
		return receptor;
	}
	public void setReceptor(Usuarios receptor) {
		this.receptor = receptor;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
}
