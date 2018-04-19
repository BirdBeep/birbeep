package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Util.DbQuery;

public class ConversacionDAO {
	private Connection con;
	public ConversacionDAO(Connection con) {
		this.con = con;
	}
	public void altaConversacion() throws SQLException {
		PreparedStatement orden=null;
		orden =con.prepareStatement(DbQuery.setConver());
		orden.executeUpdate();
	}
	public int recuperarConv() throws SQLException {
		int c=-1;
		PreparedStatement orden=null;
		ResultSet datos=null;
		orden =con.prepareStatement(DbQuery.getConver());//Buscar la opcion que no es Prepared ya que no estamos enviando params
		datos=orden.executeQuery();
		if(datos.next()){
			c=datos.getInt(1);
		}
		return c;
	}
	
}
