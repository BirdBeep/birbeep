package Client.Modelo;

public class PeticionCertificado extends Peticion{
	private int id;
	private String usu;
	
	public PeticionCertificado(){};
	
	public PeticionCertificado(int id, String usu){
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
	
	public String getUserId(){
		return usu;
	}
	
	public void setUserId(String usu){
		this.usu=usu;
	}
}
