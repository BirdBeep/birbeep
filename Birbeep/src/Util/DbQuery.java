package Util;
/**
 * 
 * @author Birdbeep
 *
 *Se define la query y un método público devuelve su valor
 *
 */
public class DbQuery {
private static final String GETUSERID="select idUser,password,nombre,apellidos,email,ultima_conexion from usuarios where idUser=?";
private static final String GETUSEREMAIL="select idUser,password,nombre,apellidos,email,ultima_conexion from usuarios where email=?";
private static final String INSERTMSG="insert into mensajes (idMensaje,emisor,receptor,texto,conversacion,fecha) values(0,?,?,?,?,now())";//Alomejor hay que pasar id vacio y fecha que tiene autoincrement
private static final String INSERTCON="insert into conexiones (idConexion, ip, usuario, ultima_actualizacion) values(0,?,?,?)";
private static final String GETLASTCON="select idConexion, ip, usuario, ultima_actualizacion from conexiones where usuario=? order by 1 desc limit 1";
private static final String INSERTCONVER="insert into conversaciones values(?)";
private static final String GETUSCONVER="select user, idConversacion from participantes where user=?";
private static final String GETCONVER="select idConversacion from conversaciones where idConversacion=?";
private static final String INSERTUSCONVER="insert into participantes (user, idConversacion) values(?,?)";
private static final String GETMENSAJES="select emisor, receptor, texto, conversacion, fecha from mensajes where emisor=?";
private static final String GETALLUSERS="select distinct idUser, nombre, apellidos from usuarios";
public static String getUser(){
	return GETUSERID;
}
public static String getUserEmail(){
	return GETUSEREMAIL;
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
public static String setConver(){
	return INSERTCONVER;
}
public static String getUsersConver(){
	return GETUSCONVER;
}
public static String getConver(){
	return GETCONVER;
}
public static String setUsersConver(){
	return INSERTUSCONVER;
}
public static String getMensajes(){
	return GETMENSAJES;
}
public static String getAllUsers(){
	return GETALLUSERS;
}

}