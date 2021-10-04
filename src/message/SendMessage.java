package message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SendMessage {
	
	public void sendReq(Socket socket, byte[] message) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(message);
//			synchronized (socket) {
//				out.writeObject(message);
//			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}

}
