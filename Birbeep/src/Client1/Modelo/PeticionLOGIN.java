package Client1.Modelo;

public class PeticionLOGIN extends Peticion{
private Usuarios usuario;
	
	public PeticionLOGIN(){
		super(1);
	};
	
	public PeticionLOGIN(int tipo, Usuarios usuario) {
		super(tipo);
		this.usuario=usuario;
	}

	public Usuarios getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuarios usuario) {
		this.usuario = usuario;
	}


}