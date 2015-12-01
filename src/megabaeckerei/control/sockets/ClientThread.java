package megabaeckerei.control.sockets;

import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Paul on 01.12.15.
 */
public class ClientThread extends Thread {

    private Socket socket;
    private SocketHandler sh;
    private PrintWriter out;
    private BufferedReader in;

    public ClientThread(SocketHandler sh, Socket socket) {
        System.out.println("New Client connected");
        this.sh = sh;
        this.socket = socket;
        this.initIO();
    }

    public void sendCommand(String cmd) {
        this.out.println(cmd);
    }

    private void initIO() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleCommand(String message) {
        System.out.println(message);
        try {
            String[] arr = message.split(" ");
            String cmd = arr[0];
            String value = arr[1];

            switch (cmd) {
                case "identify":
                    sh.identifyCallback(value, this);
                    break;
                case "level_water":
                    Platform.runLater(() -> sh.controller.updateLevelWater(Integer.parseInt(value)));
                    break;
                case "water_to_mischer":
                    sh.sendToSocket(ClientType.MIXER, "add_water " + value);
                    break;
                default:
                    System.out.println("Invalid command");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equalsIgnoreCase("disconnect"))
                    break;
                this.handleCommand(inputLine);
            }
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
