package Client3.Modelo;

public class PeticionMSG extends Peticion{
	
	private Mensaje mensaje;
	
	public PeticionMSG(){
		super();
	};
	
	public PeticionMSG(int tipo, Mensaje mensaje) {
		super(tipo);
		this.mensaje=mensaje;
	}

	public Mensaje getMensaje() {
		return mensaje;
	}

	public void setMensaje(Mensaje mensaje) {
		this.mensaje = mensaje;
	}

}
