package Client3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ClientListenUDP implements Runnable {
	private static final int PORT = 1234;
	@Override
	public void run() {
		DatagramSocket datagramSocket=null;
		try {
			datagramSocket=new DatagramSocket(PORT);
			DatagramPacket inpacket =new DatagramPacket(new byte[256],256);
			datagramSocket.receive(inpacket);
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}

}
