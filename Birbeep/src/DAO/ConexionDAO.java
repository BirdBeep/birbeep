package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, Calendar.HOUR_OF_DAY, Calendar.MINUTE);
		java.sql.Date now = new java.sql.Date(cal.getTimeInMillis());
	    PreparedStatement orden=null;
		orden =con.prepareStatement(DbQuery.insertConexion());
		orden.setString(1, ip);
		orden.setString(2, user);
		orden.setDate(3, now);
		orden.executeUpdate();
	}

	public Conexiones obtenerUltimaCon(String id)throws SQLException{//Comprobar los campos
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
