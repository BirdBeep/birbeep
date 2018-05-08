package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
	public List<Usuarios> recuperarTodos() {
		PreparedStatement orden=null;
		ResultSet datos=null;
		List<Usuarios> contactos=new ArrayList<Usuarios>();
		Usuarios usuario=new Usuarios();
		try {
			orden =con.prepareStatement(DbQuery.getAllUsers());
			datos=orden.executeQuery();
			while (datos.next()){
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
}
