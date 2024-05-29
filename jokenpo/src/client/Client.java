package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import util.Comunication;

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
    private void setUserInputHandler(UserInputHandler userInputHandler) {
        this.userInputHandler = userInputHandler;
    }

    private void setServerListener(ServerListener serverListener) {
        this.serverListener = serverListener;
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

    private Socket getSocket() {
        return socket;
    }    

    public static void main(String[] args) {        
        UserInput userInput = new UserInput();
        System.out.println("Servidor?");
        String host = userInput.word();
        System.out.println("Porta?");
        int port = userInput.number();
        Client client = new Client(host, port);        
        try {
           client.socket.isConnected();
        } catch (NullPointerException e) {
            System.out.println("Servidor não encontrado em " + host + ":" + port);
            System.exit(1);
        }
        Comunication com = new Comunication(client.getSocket());
        client.setUserInputHandler(new UserInputHandler(com));
        client.setServerListener(new ServerListener(com));
        client.userInputHandler.start();
        client.serverListener.start();
        try {
            client.serverListener.join();
            client.userInputHandler.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            client.socket.close();
        } catch (IOException e) {
            System.out.println("Erro ao encerrar o socket");
            e.printStackTrace();
        }
    }
}