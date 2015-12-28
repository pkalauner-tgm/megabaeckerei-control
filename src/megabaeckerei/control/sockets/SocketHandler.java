package megabaeckerei.control.sockets;

import megabaeckerei.control.gui.Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by Paul on 01.12.15.
 */
public class SocketHandler extends Thread{
    private Map<ClientType, ClientThread> clientMap;
    private ServerSocket serverSocket;
    Controller controller;

    public SocketHandler(Controller controller) {
        this.clientMap = new EnumMap<>(ClientType.class);
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            this.serverSocket = new ServerSocket(12345);
            while (true) {
                new ClientThread(this, serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    ClientType identifyCallback(String name, ClientThread ct) {
        try {
            ClientType type = ClientType.valueOf(name.toUpperCase());
            this.clientMap.put(type, ct);
            System.out.println("Client identified: " + name);

            switch (type) {
                case WATERTANK:
                    controller.changeLabelWater(true);
                    ct.sendCommand("get_level");
                    break;
                case MIXTANK:
                    controller.changeLabelMixture(true);
                    ct.sendCommand("get_level");
                    break;
                case MIXER:
                    controller.changeLabelMixer(true);
                    ct.sendCommand("get_level");
                    break;
                case OVEN:
                    controller.changeLabelOven(true);
                    ct.sendCommand("get_temperature\0");
                    break;
                default:
                    ct.sendCommand("get_level");
            }
            return type;

        } catch (IllegalArgumentException iae) {
            System.out.println("No Client type with name " + name);
            return null;
        }
    }
    public void sendToSocket(ClientType type, String cmd) {
        ClientThread client = clientMap.get(type);
        if (client != null)
            client.sendCommand(cmd);
    }
}
