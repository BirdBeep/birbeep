package Client1.Modelo;

public class PeticionMSG extends Peticion{
	
	private Mensajes mensaje;
	
	public PeticionMSG(){
	};
	
	public PeticionMSG( Mensajes mensaje) {
		super(5);
		this.mensaje=mensaje;
	}

	public Mensajes getMensaje() {
		return mensaje;
	}

	public void setMensaje(Mensajes mensaje) {
		this.mensaje = mensaje;
	}

}
