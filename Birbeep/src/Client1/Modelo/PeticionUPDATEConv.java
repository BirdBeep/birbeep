package Client1.Modelo;


public class PeticionUPDATEConv extends Peticion {
public Usuarios user;
public Usuarios getUser() {
	return user;
}
public void setUser(Usuarios user) {
	this.user = user;
}
public PeticionUPDATEConv(){};
public PeticionUPDATEConv(Usuarios user){
	super(3);
	this.user=user;
};
}
