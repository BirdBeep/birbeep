package Client.Modelo;


public class PeticionLogin extends Peticion{
private Usuarios usuario;
	
	public PeticionLogin(){
		super();
	};
	
	public PeticionLogin(Usuarios usuario) {
		super(1);
		this.usuario=usuario;
	}

	public Usuarios getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuarios usuario) {
		this.usuario = usuario;
	}


}
