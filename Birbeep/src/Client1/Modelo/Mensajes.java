package Client1.Modelo;

public class Mensajes {
	private String id;
	private String emisor;
	private String receptor;
	private String texto;
	private String conver;
	
	public Mensajes(){};
	
	public Mensajes(String emisor, String receptor, String texto, String conver) {
		super();
		//this.id = id;
		this.emisor = emisor;
		this.receptor = receptor;
		this.texto = texto;
		this.conver = conver;
	}

	public String getConver() {
		return conver;
	}
	public void setConver(String conver) {
		this.conver = conver;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
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

