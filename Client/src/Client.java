package chatclient;
import java.net.*;
import java.io.IOException;
import java.util.Scanner;

public class Client implements SocketClient {
    
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private InetAddress IPAddress;
    private int ServerPort;
    private Socket s;
    private Thread readMessage;
    private Thread sendMessage;
    public Scanner sc;

    public Client(InetAddress ip, int sp, Scanner s){
        this.IPAddress = ip;
        this.ServerPort = sp;
        this.Socket = new Socket(IPAddress, ServerPort);
        this.inputStream = new DataInputStream(s.getInputStream());
        this.outputStream = new DataOutputStream(s.getOutputStream());
        this.sc = s;
    }
    
    @Override
    public void startReceive(){
         readMessage = new Thread(new Runnable(){
            @Override
            public void run(){
                while(true){
                    try{
                        //read the message sent to this client
                        String msg = inputStream.readUTF();
                        System.out.println(msg);
                    } catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });
        readMessage.start(); 
    }

    @Override
    public void startSend(){
        sendMessage = new Thread(new Runnable(){
            @Override
            public void run(){
                while(true)
                {
                    String msg = sc.nextLine();
                    try{
                        outputStream.writeUTF(msg);
                    } catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
        sendMessage.start();
    }
}

