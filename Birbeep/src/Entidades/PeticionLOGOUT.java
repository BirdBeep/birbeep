package Entidades;

public class PeticionLOGOUT extends Peticion {
	private Usuarios usuario;
	
	public Usuarios getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuarios usuario) {
		this.usuario = usuario;
	}
		
	public PeticionLOGOUT(){};
	public PeticionLOGOUT(Usuarios user){
		super(7);
		this.usuario=usuario;
	};
	}
