package chatclient
import java.net.*;
public class ClientFactory{
    //use getType to double check it is a Client we want to build
    public Client getClient(String clientType, inetAddress ip, int sp, Scanner s){
        if(clientType == null){
            return null;
        }

        if (clientType.equalsIgnoreCase("CLIENT")){
            return new Client(ip,sp,s);
        }

        return null;
    }

}
