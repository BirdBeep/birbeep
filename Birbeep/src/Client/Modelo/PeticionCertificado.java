package Client.Modelo;

public class PeticionCertificado extends Peticion{
	private String id;
	
	public PeticionCertificado(String id){
		super(3);
		this.id=id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
