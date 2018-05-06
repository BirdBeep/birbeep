package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Entidades.Participantes;
import Util.DbQuery;

public class ParticipantesDAO {
	private Connection con;
	public ParticipantesDAO(Connection con) {
		this.con = con;
	}
	
	public void altaParticipantes(Participantes part) {
		//String now=new java.util.Date().toString();
		PreparedStatement orden=null;
		try {
			orden =con.prepareStatement(DbQuery.setUsersConver());
			orden.setString(1, part.getUser());
			orden.setString(2, part.getIdconversacion());
			orden.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally{
			try {
				orden.close();
				//datos.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public int recuperarConvPart(String emisor,String id) {
		int c=-1;
		PreparedStatement orden=null;
		ResultSet datos=null;
		try {
			orden =con.prepareStatement(DbQuery.getUsersConver());
			orden.setString(1,emisor);
			orden.setString(2, id);
			datos=orden.executeQuery();
			if(datos.next()){
				c=datos.getInt(2);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally{
			try {
				orden.close();
				datos.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return c;
	}

}
