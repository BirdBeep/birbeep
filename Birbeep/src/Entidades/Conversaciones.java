package Entidades;
import java.sql.Date;

public class Conversaciones {
	private String idConversacion;
	private Date fecha;

	public String getIdConversacion() {
		return idConversacion;
	}

	public void setIdConversacion(String idConversacion) {
		this.idConversacion = idConversacion;
	}

	
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
}
