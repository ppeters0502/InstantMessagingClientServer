package client;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;


public class ClientFactory{
    //use getType to double check it is a Client we want to build
    public static Client getClient(String clientType, String userName, InetAddress ip, int sp, Scanner s) throws IOException{
        if(clientType == null){
            return null;
        }

        if (clientType.equalsIgnoreCase("CLIENT")){
            return new Client(ip,sp,userName,s);
        }

        return null;
    }

}
