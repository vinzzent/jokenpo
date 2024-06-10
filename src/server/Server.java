package server;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import util.Comunication;

public class Server {
    private ServerSocket serverSocket = null;
    protected static Map<String, EntryService> entryServices;
    protected static Map<String, SoloService> soloServices;
    protected static Map<String, LobbyService> lobbyServices;
    protected static Map<String, ChallengeService> challengeServices;
    protected static Map<String, VersusService> versusServices;

    public Server(int port) {
        setServerSocket(port);
        setEntryService();
        setSoloServices();
        setLobbyServices();
        setChallengeServices();
        setVersusServices();
    }

    private void setEntryService() {
        entryServices = new HashMap<>();
    }

    private void setSoloServices() {
        soloServices = new HashMap<>();
    }

    private void setLobbyServices() {
        lobbyServices = new HashMap<>();
    }

    private void setChallengeServices() {
        challengeServices = new HashMap<>();
    }

    private void setVersusServices() {
        versusServices = new HashMap<>();
    }

    private void setServerSocket(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (BindException e) {

        } catch (IOException e) {
            
        }
    }

    public void startEntryService() {      
        try {
            Socket socket = this.serverSocket.accept();
            Comunication com = new Comunication(socket);
            UserAgent uA = new UserAgent(com);
            EntryService service = new EntryService(uA);
            entryServices.put(service.getPk(), service);
            service.start();            
        } catch (IOException e) {
            System.out.println("Error at adding entry service: " + e.getMessage());
        }        
    }    
}