package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Entidades.Conexiones;
import Entidades.Mensajes;
import Entidades.Usuarios;
import Util.DbQuery;

public class ConexionDAO {
	private Connection con;
	public ConexionDAO(Connection con) {
		this.con = con;
	}
	
	public void altaConexion(String ip,String user)throws SQLException {
		String now=new java.util.Date().toString();
		PreparedStatement orden=null;
		ResultSet datos=null;
		orden =con.prepareStatement(DbQuery.insertConexion());
		//orden.setString(1, idUser); Como van las id de las tablas?? Autoincrement VS Comunes
		orden.setString(2, ip);
		orden.setString(3, now);
		orden.setString(4, user);
		datos=orden.executeQuery();
	}

	public Conexiones obtenerUltimaCon(String id)throws SQLException{
		PreparedStatement orden=null;
		ResultSet datos=null;
		Conexiones conexion = new Conexiones();
		//try {
			orden =con.prepareStatement(DbQuery.getLastCon());
			orden.setString(1, id);
			datos=orden.executeQuery();
			if (datos.next()){
				conexion.setIdConexion(datos.getString(1));
				conexion.setIp(datos.getString(2));
				conexion.setUltimaActualizacion(datos.getDate(3));
				conexion.setUser(datos.getString(4));
			}
			return conexion;
	}
}
