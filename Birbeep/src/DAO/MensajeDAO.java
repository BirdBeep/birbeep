package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Entidades.Conversaciones;
import Entidades.Mensajes;
import Entidades.Usuarios;
import Util.DbQuery;

public class MensajeDAO {
	private Connection con;
	public MensajeDAO(Connection con) {
		this.con = con;
	}
	public void insertarMensaje(Mensajes mensaje) {
		PreparedStatement orden=null;
		try {
			orden =con.prepareStatement(DbQuery.setMSG());
			orden.setString(1, mensaje.getEmisor());
			orden.setString(2, mensaje.getReceptor());
			orden.setString(3, mensaje.getTexto());
			orden.setString(4, mensaje.getConver());
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
	public List<Mensajes> recuperarMensajes(Usuarios user) {
		List<Mensajes> mnsgs=new ArrayList<Mensajes>();
		PreparedStatement orden=null;
		ResultSet datos=null;
		try{
			orden=con.prepareStatement(DbQuery.getMensajes());
			orden.setString(1, user.getId());
			datos=orden.executeQuery();
			while (datos.next()){
				Mensajes m = new Mensajes();
				m.setEmisor(datos.getString(1));
				m.setReceptor(datos.getString(2));
				m.setTexto(datos.getString(3));
				m.setConver(datos.getString(4));
				mnsgs.add(m);
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
		return mnsgs;
	}
}
