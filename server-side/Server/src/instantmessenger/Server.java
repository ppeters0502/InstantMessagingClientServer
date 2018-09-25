package instantmessenger;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//Singleton design pattern for server to keep only one active server up at a time
//Also facade pattern to wrap ServerSocket in my own class
public class Server {
        private ServerSocket socketInstance;
        private int port;
        private static Server instance = new Server(5000);  
	private Server(int p){
            port = p;
            try{
            socketInstance = new ServerSocket(port);
            }catch(IOException i)
            {
                System.out.println("uhoh at constructor");
            }
        }
        
	public static Server getServer(){
            
		return instance;
	}
        public Socket start() throws IOException {
            return instance.socketInstance.accept();
        }
	
}

