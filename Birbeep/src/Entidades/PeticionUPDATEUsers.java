package Entidades;

public class PeticionUPDATEUsers extends Peticion{
	
	private Usuarios usuario;
	
	public PeticionUPDATEUsers(){};
		
	public PeticionUPDATEUsers( Usuarios usuario) {
		super(2);
		this.usuario=usuario;
	}

	public Usuarios getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuarios usuario) {
		this.usuario = usuario;
	}
		
}