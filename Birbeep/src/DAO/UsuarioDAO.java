package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Entidades.Usuario;
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
	public Usuario recuperarUsuario(Usuario usu) throws SQLException{
		PreparedStatement orden=null;
		ResultSet datos=null;
		Usuario usuario=new Usuario();
		//try {
			orden =con.prepareStatement(DbQuery.getUser());
			orden.setString(1, usu.getId());
			datos=orden.executeQuery();
			if (datos.next()){
				usuario.setId(datos.getString(1));
				usuario.setUsername(datos.getString(2));
				usuario.setPassword(datos.getString(3));
				usuario.setNombre(datos.getString(4));
				usuario.setApellidos(datos.getString(5));
				usuario.setIp(datos.getString(6));
				usuario.setUltimaConexion(datos.getDate(7));
				usuario.setActivo(datos.getBoolean(8));
			}
		//} catch (SQLException e) {
			//e.getMessage();
		//} finally{
			orden.close();
			datos.close();
		//}
		return usuario;
	}
	/**
	 * Le pasamos un usuario recuperado en de la BBDD y actualizamos su atributo "activo"
	 * @param usu
	 * @throws SQLException
	 */
	public void activarUsuario(Usuario usu) throws SQLException{
		PreparedStatement orden=con.prepareStatement(DbQuery.setUser());
		orden.setString(1, usu.getId());
		orden.executeUpdate();
	}
}
