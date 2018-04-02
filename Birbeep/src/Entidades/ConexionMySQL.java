package Entidades;

import java.sql.*;

public class ConexionMySQL {
Connection conexion;

public ConexionMySQL(String url, String user, String password) throws SQLException{
	try{
		Class.forName("com.mysql.jdbc.Driver");
		conexion=DriverManager.getConnection(url, user, password);
	}catch(ClassNotFoundException e){
			throw new SQLException("No se pueden cargar los controladores");
		}
}
public ConexionMySQL() throws SQLException{
	try{
		Class.forName("com.mysql.jdbc.Driver");
		conexion=DriverManager.getConnection("jdbc:mysql://localhost/birdbeep","root","");
		}catch(ClassNotFoundException e){
			throw new SQLException("No se pueden cargar los controladores");
		}
}

public Connection getConexion(){
	return conexion;
}
}