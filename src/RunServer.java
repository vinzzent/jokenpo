
import server.Server;

public class RunServer {

    public static void main(String[] args) {
        Server server = new Server(6666);
        while (true) {
            System.out.println("Waiting client...");
            server.startEntryService();
        }
    }
}
