import java.io.*;
import java.net.*;
import java.util.Set;

/**
 * Write a description of class Client here.
 *
 * @author (M_R)
 * @version (a version number or a date)
 */
public class Client
{
    // instance variables - replace the example below with your own
    private Socket socket            = null;
    private DataInputStream  input   = null;
    private DataInputStream  in   = null;
    private DataOutputStream out     = null;

    /**
     * Constructor for objects of class Client
     */
    public Client(String address, String port) {
        // initialise instance variables
        System.out.println("Please enter your name:");
        try{
            socket = new Socket(address, Integer.parseInt(port));
            System.out.println("Connected");
            input = new DataInputStream(System.in);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            if(socket != null){
                Thread sendToServer = new Thread(new SendToServer());
                sendToServer.start();
                info(sendToServer, "sendToServer");
                //sendToServer.join();
                info(sendToServer, "sendToServer");
                Thread listenToServer = new Thread(new ListenToServer());
                listenToServer.start();
                info(listenToServer, "listenToServer");
            }
        }catch(IOException io){
            System.out.println(io);
        }
        /*
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        */
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        for (Thread t: threadSet) {
            System.out.println(t.getName());
        }
    }

    private class SendToServer implements Runnable{
        public void run(){
            String line = "";
            while(true){
                try{

                    line = input.readLine();
                    out.writeUTF(line);
                    out.flush();

                }catch(IOException io){
                    System.out.println(io);
                }
            }
        }
    }

    private class ListenToServer implements Runnable{
        public void run(){
            String line = "";
            while(true){
                try{

                    line = in.readUTF();
                    System.out.println(line);

                }catch(IOException io){
                    System.out.println(io);
                }
            }
        }
    }

    private void info(Thread thread, String name){
        System.out.println(name + " ID: " + thread.getId());
        System.out.println(name + " Name: " + thread.getName());
        System.out.println(name + " Alive: " + thread.isAlive());
        System.out.println(name + " State: " + thread.getState().name());
    }
}