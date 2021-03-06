package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Entidades.Usuarios;
import Util.DbQuery;

public class UsuarioDAO {
	//private static final String DB_ERR = "Error de la base de datos";
	//private static final int MYSQL_DUPLICATE_PK = 1062;
	private Connection con;
	public UsuarioDAO(Connection con) {
		this.con = con;
	}
	/**
	 * Le pasamos un user creado solo con el id y nos devuelve uno completo con los valores extraidos de la BBDD
	 * @param usu
	 * @return Usuario
	 * @throws SQLException
	 */
	public Usuarios recuperarUsuario(Usuarios usu) {
		PreparedStatement orden=null;
		ResultSet datos=null;
		Usuarios usuario=new Usuarios();
		try {
			orden =con.prepareStatement(DbQuery.getUser());
			orden.setString(1, usu.getId());
			datos=orden.executeQuery();
			if (datos.next()){
				usuario.setId(datos.getString(1));
				usuario.setPassword(datos.getString(2));
				usuario.setNombre(datos.getString(3));
				usuario.setApellidos(datos.getString(4));
				usuario.setEmail(datos.getString(5));
				usuario.setUltimaConexion(datos.getDate(6));
			}
		} catch (SQLException e) {
			e.getMessage();
		} finally{
			try {
				orden.close();
				datos.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return usuario;
	}
	/**
	 * 
	 * @param email
	 * @return usuario
	 */
	public Usuarios recuperarUsuario(String email) {
		PreparedStatement orden=null;
		ResultSet datos=null;
		Usuarios usuario=new Usuarios();
		try {
			orden =con.prepareStatement(DbQuery.getUserEmail());
			orden.setString(1, email);
			datos=orden.executeQuery();
			if (datos.next()){
				usuario.setId(datos.getString(1));
				usuario.setPassword(datos.getString(2));
				usuario.setNombre(datos.getString(3));
				usuario.setApellidos(datos.getString(4));
				usuario.setEmail(datos.getString(5));
				usuario.setUltimaConexion(datos.getDate(6));
			}
		} catch (SQLException e) {
			e.getMessage();
		} finally{
			try {
				orden.close();
				datos.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return usuario;
	}
	public List<Usuarios> recuperarTodos(Usuarios cli) {
		PreparedStatement orden=null;
		ResultSet datos=null;
		List<Usuarios> contactos=new ArrayList<Usuarios>();
		try {
			orden =con.prepareStatement(DbQuery.getAllUsers());
			orden.setString(1, cli.getId());
			datos=orden.executeQuery();
			while (datos.next()){
				Usuarios usuario=new Usuarios();
				usuario.setId(datos.getString(1));
				//usuario.setPassword(datos.getString(2));
				usuario.setNombre(datos.getString(2));
				usuario.setApellidos(datos.getString(3));
				//usuario.setEmail(datos.getString(5));
				//usuario.setUltimaConexion(datos.getDate(6));
				contactos.add(usuario);
			}
		} catch (SQLException e) {
			e.getMessage();
		} finally{
			try {
				orden.close();
				datos.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return contactos;
	}
	
	public void cerrar_usuario(Usuarios usuario) {
		Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, Calendar.HOUR_OF_DAY, Calendar.MINUTE);
	    java.sql.Date now = new java.sql.Date(cal.getTimeInMillis());
	       
	       System.out.println(now);
	       PreparedStatement orden=null;
			try {
				orden =con.prepareStatement(DbQuery.UpdateUser());
				orden.setDate(1, now);
				orden.setString(2, usuario.getId());
				orden.executeUpdate();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			} finally{
				try {
					orden.close();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
	}
}
