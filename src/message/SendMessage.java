package message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SendMessage {
	
	public String sendReq(Socket socket, byte[] message){
		String response = "";
		try {
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(message);
//			synchronized (socket) {
//				out.writeObject(message);
//			}
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			byte[] resp = null;
			resp = (byte[]) in.readObject();
			String str = new String(resp, StandardCharsets.UTF_8);
			return str;
		} 
//		catch (IOException e) {
//			System.err.println(e);
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
		catch (Exception e) {
			System.out.println(e);
		}
		return response;
	}

}
