package Entidades;

public class PeticionUpdateUsuarios extends Peticion{
	
	private Usuarios usuario;
	
	public PeticionUpdateUsuarios() {}
		
	public PeticionUpdateUsuarios(Usuarios usuario) {
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