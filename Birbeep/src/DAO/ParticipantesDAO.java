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
			orden.setInt(2, part.getIdconversacion());
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
				cs.setIdconversacion(datos.getInt(2));
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
	
	public List<Integer> recuperar_id_conversacion(String id_user){
		List <Integer> id=new ArrayList<Integer>();
		int conversacion;
		PreparedStatement orden=null;
		ResultSet datos=null;
		try{
			orden =con.prepareStatement(DbQuery.getUsersConver());
			orden.setString(1, id_user);
			datos=orden.executeQuery();
			if(datos.next()){
				conversacion=datos.getInt(2);
				id.add(conversacion);
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
		return id;
	}

	public List<Participantes> recuperarPorConv(Conversaciones conver, Usuarios user) {
		List<Participantes> part=new ArrayList<Participantes>();
		PreparedStatement orden=null;
		ResultSet datos=null;
		try{
			orden =con.prepareStatement(DbQuery.getPartConver());
			orden.setInt(1,conver.getIdConversacion());
			orden.setString(2, user.getId());
			datos=orden.executeQuery();
			if(datos.next()){
				Participantes cs = new Participantes();
				cs.setUser(datos.getString(1));
				//cs.setIdconversacion(datos.getInt(2));
				part.add(cs);
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
		return part;
	}

}
