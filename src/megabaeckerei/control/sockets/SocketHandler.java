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
    void identifyCallback(String name, ClientThread ct) {
        try {
            ClientType type = ClientType.valueOf(name.toUpperCase());
            this.clientMap.put(type, ct);
            System.out.println("Client identified: " + name);

            switch (type) {
                case OVEN:
                    ct.sendCommand("get_temperature\0");
                    break;
                default:
                    ct.sendCommand("get_level");
            }

        } catch (IllegalArgumentException iae) {
            System.out.println("No CLient type with name " + name);
        }
    }
    public void sendToSocket(ClientType type, String cmd) {
        ClientThread client = clientMap.get(type);
        if (client != null)
            client.sendCommand(cmd);
    }
}
