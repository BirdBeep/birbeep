import java.net.DatagramPacket;
import java.sql.SQLException;

import DAO.UsuarioDAO;
import Entidades.ConexionMySQL;
import Entidades.Usuario;

/**
 * 
 * @author BirBeep
 * 
 * Se encarga de manejar la info del token enviado por un cliente
 *
 */
public class TokenHandler extends Thread {
	ConexionMySQL con;
	String id;
	UsuarioDAO serv;
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
