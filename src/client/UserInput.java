package client;

import java.util.Scanner;

public class UserInput {
    private Scanner scanner;

    public UserInput() {
        setScanner();
    }
    
    private Scanner getScanner() {
        return scanner;
    }

    private void setScanner() {
        this.scanner = new Scanner(System.in);
    }

    protected String word() {
        return getScanner().next();
    }

    protected int number() {
       return fromStringToInteger(word());
    }

    private static int fromStringToInteger(String word) {
        int number  = 0;
        try {
            number = Integer.parseInt(word);            
        } catch (NumberFormatException e) {
                System.out.println("Invalid integer input");
                e.printStackTrace();
                System.exit(1);        
        }   
        return number;
    }
}