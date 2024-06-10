
import client.Client;
import client.ServerListener;
import client.UserInput;
import client.UserInputHandler;
import java.io.IOException;
import util.Comunication;

public class RunClient {

    public static void main(String[] args) {
        UserInput userInput = new UserInput();
        System.out.println("Qual o endereço do servidor? (localhost = 'h')");
        String word = userInput.word();
        if (word.toLowerCase().trim().equals("h")) {
            word = "localhost";
        }
        String host = word;
        int port = 6666;
        Client client = new Client(host, port);
        try {
            client.getSocket().isConnected();
        } catch (NullPointerException e) {
            System.out.println("Servidor não encontrado em " + host + ":" + port);
            System.exit(1);
        }
        Comunication com = new Comunication(client.getSocket());
        client.setUserInputHandler(new UserInputHandler(com));
        client.setServerListener(new ServerListener(com));
        client.getUserInputHandler().start();
        client.getServerListener().start();
        try {
            client.getServerListener().join();
            client.getUserInputHandler().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            client.getSocket().close();
        } catch (IOException e) {
            System.out.println("Erro ao encerrar o socket");
            e.printStackTrace();
        }
    }

}
