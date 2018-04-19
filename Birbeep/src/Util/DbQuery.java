package Util;
/**
 * 
 * @author Birdbeep
 *
 *Se define la query y un método público devuelve su valor
 *
 */
public class DbQuery {
private static final String GETUSERID="select idUser,userName,password,nombre,apellidos,ip,ultimaConexion,activo from usuarios where idUser=?";
private static final String UPDATEUSERACTIVO="update usuarios set activo=true where idUser=?";
private static final String INSERTMSG="insert into mensajes idEmisor,fecha,texto values(?,?,?)";
private static final String INSERTCON="insert into conexiones idConexion, ip, ultima_actualizacion, usuario values(?,?,?)"; //Va a llevar autoincrement???????
private static final String GETLASTCON="select idConexion, ip, ultima_actualizacion, usario from conexiones where usuario=? order by 1 desc limit 1";
public static String getUser(){
	return GETUSERID;
}
public static String setUser(){
	return UPDATEUSERACTIVO;
}
public static String setMSG() {
	return INSERTMSG;
}
public static String insertConexion() {
	return INSERTCON;
}
public static String getLastCon() {
	return GETLASTCON;
}
}
