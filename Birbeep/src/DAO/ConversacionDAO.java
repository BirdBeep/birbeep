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

public class ConversacionDAO {
	private static final int MYSQL_DUPLICATE_PK = 1062;
	private Connection con;
	public ConversacionDAO(Connection con) {
		this.con = con;
	}
	public boolean altaConversacion(Conversaciones conver) {
		PreparedStatement orden=null;
		try {
			orden =con.prepareStatement(DbQuery.setConver());
			orden.setString(1, conver.getIdConversacion());
			orden.executeUpdate();
		} catch (SQLException e) {
			if (e.getErrorCode() == MYSQL_DUPLICATE_PK) {
				return false;
			}
			System.out.println(e.getMessage());
		} finally{
			try {
				orden.close();
				//datos.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return true;
	}
	public Conversaciones recuperarConv(String idConver)  {
		Conversaciones c=new Conversaciones();
		PreparedStatement orden=null;
		ResultSet datos=null;
		try {
			orden =con.prepareStatement(DbQuery.getConver());
			orden.setString(1, idConver);
			datos=orden.executeQuery();
			if(datos.next()){
				c.setIdConversacion(datos.getString(1));
				//c.setFecha(datos.getDate(2));
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
