
import java.net.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Write a description of class Server here.
 *
 * @author (M_R)
 * @version (a version number or a date)
 */
public class Server {
    // instance variables - replace the example below with your own
    private ArrayList<Socket> socket = null;
    private ArrayList<DataOutputStream> outputStream = new ArrayList<>();
    private ServerSocket server = null;
    private  ArrayList<String> message = null;
    /**
     * Constructor for objects of class Server
     */
    public Server(String port) {
        // initialise instance variables
        try {
            server = new ServerSocket(Integer.parseInt(port), 0, InetAddress.getByName(null));
            if (server != null) {
                socket = new ArrayList<Socket>();
                message = new ArrayList<String>();
                System.out.println("Server started." + server.getLocalPort());
                Thread acceptListener = new Thread(new acceptToClient());
                info(acceptListener , "acceptListener");
                acceptListener.start();
                info(acceptListener, "acceptListener");
            }
        } catch (IOException io) {
            System.out.println(io);
        }
    }

    private class acceptToClient implements Runnable {
        private DataInputStream in = null;
        public void run() {
            while (true) try {
                Socket client = null;
                client = server.accept();
                socket.add(client);
                outputStream.add(new DataOutputStream(
                    new BufferedOutputStream(client.getOutputStream())));
                in = new DataInputStream(new BufferedInputStream(
                        client.getInputStream()));
                String name  = in.readUTF();
                Thread listenToClient = new Thread(new ListenToClient(client, name));
                listenToClient.start();
                info(listenToClient, "listenToClient");
            } catch (IOException io) {
                System.out.println(io);
            }
        }
    }

    private class ListenToClient implements Runnable {
        private Socket client;
        private DataInputStream in = null;
        private DataOutputStream out = null;
        private String name = "";

        public ListenToClient(Socket socket, String name) {
            this.client = socket;
            this.name = name;
            Thread welcomToClient = new Thread(new WelcomUser(this.name));
            welcomToClient.start();
            Thread messageShow = new Thread(new MessageShow(message, client));
            messageShow.start();
            info(welcomToClient , "welcomToClient");
            info(messageShow, "messageShow");
        }

        public void run() {
            try {
                in = new DataInputStream(
                        new BufferedInputStream(client.getInputStream()));
                out = new DataOutputStream(
                        new BufferedOutputStream(client.getOutputStream()));
                while (true) {
                    String line = "";
                    while (!line.equals("Over")) {
                        try {
                            line = in.readUTF();
                            message.add(name + ": "+ line);
                            for (int i = 0; i < outputStream.size(); i++) {
                                if(socket.indexOf(client) != i) {
                                    outputStream.get(i).writeUTF(name+": "+line);

                                    outputStream.get(i).flush();
                                }
                            }
                            System.out.println(line);
                        } catch (IOException io) {
                            System.out.println(io);
                        }
                    }
                }
            }catch (IOException io){
                System.out.println(io);
            }
        }
    }

    public class WelcomUser implements Runnable{
        private String name;
        public WelcomUser(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            for (int i = 0; i < outputStream.size(); i++) {
                try {
                    outputStream.get(i).writeUTF("Welcom to: "+ name +
                            ":"+socket.get(socket.size() - 1).getPort());
                    outputStream.get(i).flush();
                }catch(IOException io){
                    System.out.println(io);
                }
            }
        }
    }

    public class MessageShow implements Runnable{
        private  ArrayList<String> message;
        private  Socket client;
        public MessageShow(ArrayList<String> message, Socket client) {
            this.message = message;
            this.client = client;
        }

        @Override
        public void run() {
            DataOutputStream out =outputStream.get(socket.indexOf(client));
            for (int i = 0; i < message.size(); i++) {
                try {
                    out.writeUTF(message.get(i));
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
