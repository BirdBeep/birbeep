package Client1.Modelo;

public class PeticionUPDATEMsg extends Peticion{
	private Usuarios emisor;
	public PeticionUPDATEMsg(){};
	public PeticionUPDATEMsg(Usuarios emisor){
		super(4);
		this.emisor=emisor;
	}
	public Usuarios getEmisor() {
		return emisor;
	}
	public void setEmisor(Usuarios emisor) {
		this.emisor = emisor;
	}
}
