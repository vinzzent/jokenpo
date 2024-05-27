package client;

import util.Comunication;


public class UserInputHandler extends Thread {
    private Comunication comunication;
    private UserInput userInput;

    public UserInputHandler(Comunication comunication){
        setComunication(comunication);
        setUserInput();
    }
    
    private void setComunication(Comunication comunication) {
        this.comunication = comunication;
    }

    private void setUserInput() {
        this.userInput = new UserInput();
    }

    public UserInput getUserInput() {
        return userInput;
    }

    private void send(String message) {
        this.comunication.send(message);
    }

    @Override
    public void run() {  
        while (Client.isRunning()) {
            String uInput = getUserInput().word();
            if (uInput == null) {
                Client.setRunning(false);
            } else if (uInput.toLowerCase().trim().equals("sair")) {
                send(uInput);             
                Client.setRunning(false);
            } else {
                send(uInput);
            }            
        } 
        System.out.println("Ending user input handler");    
    }
}
