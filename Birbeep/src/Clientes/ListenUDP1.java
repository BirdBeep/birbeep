package Clientes;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.sql.SQLException;

import DAO.UsuarioDAO;
import Entidades.ConexionMySQL;
import Entidades.Usuarios;
/**
 * 
 * @author Birdbeep
 * Escucha en un puerto UDP y una nueva clase (en este mismo fichero) se encarga hacer lo que corresponda
 *
 */

public class ListenUDP1 extends Thread {
	private static DatagramSocket dtgs;
	private static DatagramPacket input;
	public void run(){
		try {
			byte[] buffer=new byte[4];
			input=new DatagramPacket(buffer,buffer.length);
			dtgs=new DatagramSocket(60000);
			dtgs.receive(input);
			TokenHandler1 tkh=new TokenHandler1(input);
			tkh.start();
		} catch (IOException | SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
/**
 * 
 * @author BirBeep
 * 
 * Se encarga de manejar la info del token enviado desde otro cliente.
 * PROVISIONAL!!!!!!!!!!!!!!!!!!!!!!!
 * 
 *
 */
class TokenHandler1 extends Thread {
	ConexionMySQL con;
	String Token;
	UsuarioDAO serv;
	/**
	 * Constructor que inicializa: un String que es el aviso del server que lo obtiene del param "in" y un servicio para interactuar con la capa DAO 
	 * @param in es el paquete recibido en el server
	 * @throws SQLException que tratar� "run()"
	 */
	public TokenHandler1(DatagramPacket in) throws SQLException{
		con=new ConexionMySQL();
		Token = new String(in.getData(),0,in.getLength());
		serv=new UsuarioDAO(con.getConexion());
	}
	public void run(){
		if (Token.equals("true")){
			//El server debe mandar los mensajes de client1 
		}
		/*
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
		}*/
	}
}
