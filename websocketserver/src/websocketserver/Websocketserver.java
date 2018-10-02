/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websocketserver;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;
import org.json.JSONException;

import java.net.InetSocketAddress;
import java.util.*;

public class Websocketserver extends WebSocketServer{

    private static Map<Integer,Set<WebSocket>> Rooms = new HashMap<>();
    private int myroom;
    
    public Websocketserver(){
        super(new InetSocketAddress(30001));
    }
    @Override
    public void onStart()
    {
        
    }
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake){
        System.out.println("New client connected: "+conn.getRemoteSocketAddress()+ " hash "+conn.getRemoteSocketAddress().hashCode());
    }
    
    @Override
    public void onMessage(WebSocket conn, String message){
        Set<WebSocket> s;
        try {
            JSONObject obj = new JSONObject(message);
            String msgtype = obj.getString("type");
            switch(msgtype){
                case "GETROOM":
                    myroom = generateRoomNumber();
                    s = new HashSet<>();
                    s.add(conn);
                    Rooms.put(myroom, s);
                    System.out.println("Generated new room: "+myroom);
                    conn.send("{\"type\":\"GETROOM\",\"value\":" + myroom + "}");
                    break;
                case "ENTERROOM":
                    myroom = obj.getInt("value");
                    System.out.println("New client entered room " +myroom);
                    s = Rooms.get(myroom);
                    s.add(conn);
                    Rooms.put(myroom, s);
                    break;
                default:
                    sendToAll(conn, message);
                    break;
                }
            }catch(JSONException e)
                {
                    e.printStackTrace();
                }
        System.out.println();
        }
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote){
        System.out.println("Client disconnected: "+reason);
    }
    
    @Override
    public void onError(WebSocket conn, Exception exc){
        System.out.println("Error happened: "+ exc);
    }
    
    private int generateRoomNumber(){
        return new Random(System.currentTimeMillis()).nextInt();
    }
    
    private void sendToAll(WebSocket conn, String message){
        Iterator it = Rooms.get(myroom).iterator();
        while(it.hasNext()){
            WebSocket c = (WebSocket)it.next();
            if(c != conn) c.send(message);
        }
    }
    public static void main(String[] args) {
        Websocketserver server = new Websocketserver();
        server.start();
    }
}
    
   
    

