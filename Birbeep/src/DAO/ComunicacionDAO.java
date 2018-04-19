package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Entidades.Mensajes;
import Entidades.Usuarios;
import Util.DbQuery;

public class ComunicacionDAO {
	private Connection con;
	public ComunicacionDAO(Connection con) {
		this.con = con;
	}
	public void insertarMensaje(String idUser, String idUserEmisor, String mensaje) throws SQLException {
		String now=new java.util.Date().toString();
		PreparedStatement orden=null;
		ResultSet datos=null;
		Mensajes m=new Mensajes();
		orden =con.prepareStatement(DbQuery.setMSG());
		orden.setString(2, idUser);
		orden.setString(3, idUserEmisor);
		orden.setString(4, mensaje);
		orden.setString(5, now);
		datos=orden.executeQuery();
	}
}
