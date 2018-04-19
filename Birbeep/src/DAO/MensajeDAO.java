package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Entidades.Mensajes;
import Entidades.Usuarios;
import Util.DbQuery;

public class MensajeDAO {
	private Connection con;
	public MensajeDAO(Connection con) {
		this.con = con;
	}
	public void insertarMensaje(String idUser, String idUserEmisor, String mensaje,int conv) throws SQLException {
		PreparedStatement orden=null;
		orden =con.prepareStatement(DbQuery.setMSG());
		orden.setString(1, idUserEmisor);
		orden.setString(2, idUser);
		orden.setString(3, mensaje);
		orden.setInt(4, conv);
		orden.executeUpdate();
	}
}
