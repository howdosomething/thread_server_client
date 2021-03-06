import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Write a description of class Client here.
 *
 * @author (M_R)
 * @version (a version number or a date)
 */
public class Robot {
    // instance variables - replace the example below with your own
    private Socket socket = null;
    private DataInputStream input = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;

    /**
     * Constructor for objects of class Client
     */
    public Robot(String address, String port) {
        // initialise instance variables
        System.out.println("Please enter your name:");
        try {
            socket = new Socket(address, Integer.parseInt(port));
            System.out.println("Connected");
            input = new DataInputStream(System.in);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            if (socket != null) {
                Thread sendToServer = new Thread(new SendToServer());
                sendToServer.start();
                Thread listenToServer = new Thread(new ListenToServer());
                listenToServer.start();
            }
        } catch (IOException io) {
            System.out.println(io);
        }
    }

    private class SendToServer implements Runnable {
        public void run() {
            String line = "";
            while (true) {
                try {

                    line = "---I'm bad Robot---";
                    out.writeUTF(line);
                    out.flush();

                } catch (IOException io) {
                    System.out.println(io);
                }
            }
        }
    }

    private class ListenToServer implements Runnable {
        public void run() {
            String line = "";
            while (true) {
                try {

                    line = in.readUTF();
                    System.out.println(line);

                } catch (IOException io) {
                    System.out.println(io);
                }
            }
        }
    }
}