package InstantMessanger
import java.net.ServerSocket;

//Singleton design pattern for server to keep only one active server up at a time
public class Server {

	private static ServerSocket instance = new ServerSocket(5000);
	private Server(){}
	public ServerSocket getServer(){
		return ServerSocket;
	}
	
}

