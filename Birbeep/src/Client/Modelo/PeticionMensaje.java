package Client.Modelo;

public class PeticionMensaje extends Peticion{
	
	private Mensajes mensaje;
	
	public PeticionMensaje(){
		super();
	};
	
	public PeticionMensaje(int tipo, Mensajes mensaje) {
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
