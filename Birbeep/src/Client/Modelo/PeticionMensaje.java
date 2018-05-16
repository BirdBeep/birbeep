package Client.Modelo;

public class PeticionMensaje extends Peticion{
	
	private Mensajes mensaje;
	
	public PeticionMensaje(){
		super();
	};
	
	public PeticionMensaje(Mensajes mensaje) {
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
