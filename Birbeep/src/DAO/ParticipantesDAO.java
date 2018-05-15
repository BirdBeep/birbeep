package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Entidades.Conversaciones;
import Entidades.Participantes;
import Entidades.Usuarios;
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
	
	
	public List<Participantes> recuperarTodasConv(Usuarios cli) {
		List<Participantes> cons=new ArrayList<Participantes>();
		PreparedStatement orden=null;
		ResultSet datos=null;
		try{
			orden =con.prepareStatement(DbQuery.getUsersConver());
			orden.setString(1,cli.getId());
			datos=orden.executeQuery();
			if(datos.next()){
				Participantes cs = new Participantes();
				cs.setUser(datos.getString(1));
				cs.setIdconversacion(datos.getString(2));
				cons.add(cs);
			}
		}catch(SQLException e){
			System.out.println(e.getMessage());
		}finally{
			try {
				orden.close();
				datos.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return cons;
	}

}
