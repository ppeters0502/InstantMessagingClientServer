package sockets;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author unouser
 */
public class ThreadedSocket implements Runnable {

    private final Socket clientSocket;

    public ThreadedSocket(Socket clientSocket) {

        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            PrintStream out = new PrintStream(clientSocket.getOutputStream());
            Scanner scanner = new Scanner(clientSocket.getInputStream());

            try{
            String line = scanner.nextLine();

            String[] httpArgs = line.split(" ");
            
            String url = httpArgs[1];
            for(int i = 0; i < url.length(); i++)
            {
                char c = url.charAt(i);
                if(c == '.')
                {
                    throw new Exception("Stop trying to hack!");
                }
            }

            if (httpArgs[1].equals("/")) {
                
                String pageContents = new String(Files.readAllBytes(Paths.get("index.html")));
                    
                out.println("HTTP/1.0 200 OK\n\n" + pageContents);
            } else {
                String[] getArgs = httpArgs[1].split("\\?");
                String[] specificArgs = getArgs[1].split("&");

                //?edit=JustCallSaul&This is a TV show.
                //?read=JustCallSaul
                if (specificArgs[0].startsWith("edit")) {
                    String edit = specificArgs[0];
                    String editPage = edit.split("=")[1];
                    String pageContent = specificArgs[1];
                    
                    pageContent = pageContent.replace("%20", " ");
                    
                    
                    Files.write(Paths.get(editPage + ".txt"), pageContent.getBytes());
                    
                    String pageContents = new String(Files.readAllBytes(Paths.get("edit.html")));
                
                    pageContents = pageContents.replace("@", pageContent);
                    
                    out.println("HTTP/1.0 200 OK\n\n" + pageContents);

                } else if (specificArgs[0].startsWith("read")) {
                    String read = specificArgs[0];
                    String readPage = read.split("=")[1];
                    
                    
                    String pageContents = new String(Files.readAllBytes(Paths.get(readPage + ".txt")));
                    
                    String pageContentsRead = new String(Files.readAllBytes(Paths.get("read.html")));
                
                    pageContentsRead = pageContentsRead.replace("@", pageContents);
                    
                    out.println("HTTP/1.0 200 OK\n\n" + pageContentsRead);

                }
            }
            }catch(Exception e){
                out.println("HTTP/1.0 500 NOT OK\n\n" + e.getMessage());
                e.printStackTrace();
            }
            System.out.println(clientSocket.getInetAddress() );

            //Thread.sleep(10000);
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ThreadedSocket.class.getName()).log(Level.SEVERE, null, ex);
        }catch(Exception e)
        {
            
        }
    }

}
