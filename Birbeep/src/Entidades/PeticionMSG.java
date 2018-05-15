package Entidades;

public class PeticionMSG extends Peticion{
	
	private Mensajes mensaje;
	
	public PeticionMSG(){
		super();
	};
	
	public PeticionMSG(int tipo, Mensajes mensaje) {
		super(tipo);
		this.mensaje=mensaje;
	}

	public Mensajes getMensaje() {
		return mensaje;
	}

	public void setMensaje(Mensajes mensaje) {
		this.mensaje = mensaje;
	}

}
