package Entidades;

public class PeticionUPDATEConv extends Peticion{
	private Usuarios user;
public Usuarios getUser() {
		return user;
	}
	public void setUser(Usuarios user) {
		this.user = user;
	}
public PeticionUPDATEConv(Usuarios user){
	super(3);
	this.user=user;
}
public PeticionUPDATEConv(){}
}