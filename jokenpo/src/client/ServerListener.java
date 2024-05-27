package client;

import util.Comunication;

public class ServerListener extends Thread {
    private Comunication comunication;

    public ServerListener(Comunication comunication) {
        setComunication(comunication);
    }

    private void setComunication(Comunication comunication) {
        this.comunication = comunication;
    }

    private String receive() {   
        return (String) this.comunication.receive();
    }

    @Override
    public void run() {
        while (Client.isRunning()) { 
            String serverMsg = receive();            
        if (serverMsg == null) {
                Client.setRunning(false);     
            } else {
                System.out.println(serverMsg);            
            }
        }
        System.out.println("Ending server listener");  
    }
 
}

