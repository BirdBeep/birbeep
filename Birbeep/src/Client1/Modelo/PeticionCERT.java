package Client1.Modelo;

public class PeticionCERT extends Peticion{
	private String id;
	
	public PeticionCERT(){};
	
	public PeticionCERT(String id){
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