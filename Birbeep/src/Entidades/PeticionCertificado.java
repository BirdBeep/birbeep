package Entidades;

public class PeticionCertificado extends Peticion{
	private int id;
	private String usu;
	
	public String getUserId() {
		return usu;
	}

	public void setUserId(String usu) {
		this.usu = usu;
	}

	public PeticionCertificado(){};
	
	public PeticionCertificado(int id,String usu){
		super(6);
		this.id=id;
		this.usu=usu;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
