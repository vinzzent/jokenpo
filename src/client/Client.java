package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private Socket socket;
    private UserInputHandler userInputHandler;
    private ServerListener serverListener;
    private static boolean running = false;

    public Client(String host, int port) {
        setSocket(host, port);
        Client.setRunning(true);
    }

    public Client(InetAddress address, int port) {
        setSocket(address, port);
        Client.setRunning(true);
    }
    public void setUserInputHandler(UserInputHandler userInputHandler) {
        this.userInputHandler = userInputHandler;
    }

    public void setServerListener(ServerListener serverListener) {
        this.serverListener = serverListener;
    }

    public UserInputHandler getUserInputHandler() {
        return userInputHandler;
    }

    public ServerListener getServerListener() {
        return serverListener;
    }

    protected static void setRunning(boolean running) {
        Client.running = running;
    }

    protected static boolean isRunning() {
        return Client.running;
    }

    private void setSocket(InetAddress address, int port) {
        try {
            socket = new Socket(address, port);
        } catch (IOException e) {

        }
    }

    private void setSocket(String host, int port) {
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {

        }
    }

    public Socket getSocket() {
        return socket;
    }    
}