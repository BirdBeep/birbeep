package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Util.DbQuery;

public class ParticipantesDAO {
	private Connection con;
	public ParticipantesDAO(Connection con) {
		this.con = con;
	}
	
	public void altaParticipantes(String emisor, String id, int recuperarConv) throws SQLException {
		String now=new java.util.Date().toString();
		PreparedStatement orden=null;
		orden =con.prepareStatement(DbQuery.setUsersConver());
		orden.setString(1, emisor);
		orden.setString(2, id);
		orden.setInt(3, recuperarConv);
		orden.executeUpdate();
	}
	
	public int recuperarPartConv(String emisor,String receptor) throws SQLException {
		int c=-1;
		PreparedStatement orden=null;
		ResultSet datos=null;
		orden =con.prepareStatement(DbQuery.getUsersConver());
		orden.setString(1,emisor);
		orden.setString(2,receptor);
		datos=orden.executeQuery();
		if(datos.next()){
			c=datos.getInt(3);
		}
		return c;
	}

}
