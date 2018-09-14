package client;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient{
    final static int ServerPort = 5000;

    public static void main(String args[]) throws UnknownHostException, IOException
    {
        Scanner sc = new Scanner(System.in);
        InetAddress ip = InetAddress.getByName("localhost");
        SocketClient chatClient = ClientFactory.getClient("CLIENT", ip, ServerPort, sc);
        chatClient.startReceive();
        chatClient.startSend();        
    }
}
