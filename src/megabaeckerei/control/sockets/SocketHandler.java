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
        System.out.println("Init ServerSocket");
        try {
            this.serverSocket = new ServerSocket(12345);
            for (int i = 0; i < 4; i++) {
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

            ct.sendCommand("get_level");
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
