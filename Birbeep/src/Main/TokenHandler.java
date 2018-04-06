package Main;
import java.net.DatagramPacket;
import java.sql.SQLException;

import DAO.UsuarioDAO;
import Entidades.ConexionMySQL;
import Entidades.Usuario;

/**
 * 
 * @author BirBeep
 * 
 * Se encarga de manejar la info del token enviado por un cliente, la cual es indicativo 
 * de que está activo el cliente correspondiente, aquí se pone su atributo activo a "true" o
 * se envian sus mensajes almacenados pendientes
 *
 */
public class TokenHandler extends Thread {
	ConexionMySQL con;
	String id;
	UsuarioDAO serv;
	/**
	 * Constructor que inicializa: un identificador que lo obtiene del param "in" y un servicio para interactuar con la capa DAO 
	 * @param in es el paquete recibido en el server
	 * @throws SQLException que tratará "run()"
	 */
	public TokenHandler(DatagramPacket in) throws SQLException{
		con=new ConexionMySQL();
		id = new String(in.getData(),0,in.getLength());
		serv=new UsuarioDAO(con.getConexion());
	}
	public void run(){
		Usuario user = new Usuario();
		user.setId(id);
		try {
			Usuario actual=serv.recuperarUsuario(user);
			if(!actual.isActivo()){
				serv.activarUsuario(actual);
			}else{
				//enviar los mensajes que tenga pendientes
			}
			
		} catch (SQLException e) {//para la conexion, va a tratar tambien en el caso que se propague desde DAO
			System.out.println(e.getMessage());
		}
	}
}
