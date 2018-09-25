package chatclient;
import java.util.Scanner;
import java.net.*;

public class ChatClient{
    final static int ServerPort = 5000;

    public static void main(String args[]) throws UnknownHostException
    {
        Scanner sc = new Scanner(System.in);
        inetAddress ip = InetAddress.getByName("localhost");
        SocketClient chatClient = ClientFactory.getClient("CLIENT", ip, ServerPort, sc);
        chatClient.startReceive();
        chatClient.startSend();        
    }
}
