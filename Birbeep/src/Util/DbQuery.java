package Util;
/**
 * 
 * @author Birdbeep
 *
 *Se define la query y un m�todo p�blico devuelve su valor
 *
 */
public class DbQuery {
private static final String GETUSERID="select idUser,userName,password,nombre,apellidos,ip,ultimaConexion,activo from usuarios where idUser=?";
private static final String UPDATEUSERACTIVO="update usuarios set activo=true where idUser=?";
public static String getUser(){
	return GETUSERID;
}
public static String setUser(){
	return UPDATEUSERACTIVO;
}
}
