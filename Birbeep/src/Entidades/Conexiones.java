package Entidades;

import java.sql.Date;

public class Conexiones {
	private String idConexion;
	private String ip;
	private Date ultimaActualizacion;
	private String user;
	
	public String getIdConexion() {
		return idConexion;
	}
	public void setIdConexion(String idConexion) {
		this.idConexion = idConexion;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Date getUltimaActualizacion() {
		return ultimaActualizacion;
	}
	public void setUltimaActualizacion(Date ultimaActualizacion) {
		this.ultimaActualizacion = ultimaActualizacion;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	
}
