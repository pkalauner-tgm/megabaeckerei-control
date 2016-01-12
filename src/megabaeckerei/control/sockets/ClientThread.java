package megabaeckerei.control.sockets;

import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by Paul on 01.12.15.
 */
public class ClientThread extends Thread {

    private Socket socket;
    private SocketHandler sh;
    private PrintWriter out;
    private BufferedReader in;
    private ClientType type;

    public ClientThread(SocketHandler sh, Socket socket) {
        System.out.println("New Client connected: " + socket.getInetAddress());
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
                    this.type = sh.identifyCallback(value, this);
                    break;
                case "level_water":
                    Platform.runLater(() -> sh.controller.updateLevelWater(Integer.parseInt(value)));
                    break;
                case "water_to_mischer":
                    sh.sendToSocket(ClientType.MIXER, "add_water" + value);
                    break;
                case "mixtank_to_mixer":
                    sh.sendToSocket(ClientType.MIXER, "add_mix" + value);
                    break;
                case "level_mixtank":
                    Platform.runLater(() -> sh.controller.updateLevelMixtank(Integer.parseInt(value)));
                    break;
                case "level_mixer":
                    Platform.runLater(() -> sh.controller.updateLevelMixer(Integer.parseInt(value)));
                    break;
                case "level_oven":
                    Platform.runLater(() -> sh.controller.updateLevelOven(Integer.parseInt(value)));
                    break;
                case "mixer_to_oven":
                    sh.sendToSocket(ClientType.OVEN, "add_mixedMix" + value);
                    break;
                default:
                    System.out.println("Invalid command: " + cmd);
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
        } catch (SocketException se) {
            handleDisconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDisconnect() {
        switch (type) {
            case WATERTANK:
                sh.controller.changeLabelWater(false);
                break;
            case MIXTANK:
                sh.controller.changeLabelMixture(false);
                break;
            case MIXER:
                sh.controller.changeLabelMixer(false);
                break;
            case OVEN:
                sh.controller.changeLabelOven(false);
                break;

        }
        System.out.println("Client " + this.type + " disconnected");
    }
}
