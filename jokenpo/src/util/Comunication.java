package util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Comunication {

    private Socket socket;
    private InputStream i;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    public Comunication(Socket socket) {
        setSocket(socket);
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            i = socket.getInputStream();
            in = new ObjectInputStream(i);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    private void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void send(Object object) {
        try {
            out.writeObject(object);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object receive() {
        try {
            return in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    public String receiveString() {
        try {
            return (String) in.readObject();
        } catch (IOException | ClassNotFoundException e) {            
            e.printStackTrace();
            return null;
        }

    }

    public String qABucle(String question, String[] validAnswers, String exitWord) {
        boolean running = true;
        boolean validAnswer = false;
        String answer = null;
        if (exitWord == null) {
            exitWord = "sair";
        }
        while (running) {
            send(question);
            String clientAnswer = receiveString();
            String trimmedClientAnswer = clientAnswer.trim();
            String loweredClientAnswer = trimmedClientAnswer.toLowerCase();
            if (loweredClientAnswer.equals(exitWord)) {
                answer = loweredClientAnswer;
                break;
            }
            if (validAnswers == null) {
                answer = trimmedClientAnswer;
                break;
            }
            for (String a : validAnswers) {
                if (loweredClientAnswer.equals(a)) {
                    validAnswer = true;
                    answer = loweredClientAnswer;
                    running = false;
                    break;
                }
            }
            if (!validAnswer) {
                send("Resposta invalida!");
            }
        }
        return answer;
    }

    public String qABucleForAWhile(String question, String[] validAnswers, String exitWord, long milliseconds) {
        boolean running = true;
        boolean validAnswer = false;
        String answer = null;
        if (exitWord == null) {
            exitWord = "sair";
        }
        while (running) {
            send(question);
            boolean isObjectInput = checkForObjectInput(milliseconds);
            if (isObjectInput == true) {
                String clientAnswer = receiveString();
                String trimmedClientAnswer = clientAnswer.trim();
                String loweredClientAnswer = trimmedClientAnswer.toLowerCase();
                if (loweredClientAnswer.equals(exitWord)) {
                    answer = loweredClientAnswer;
                    break;
                }
                if (validAnswers == null) {
                    answer = trimmedClientAnswer;
                    break;
                }
                for (String a : validAnswers) {
                    if (loweredClientAnswer.equals(a)) {
                        validAnswer = true;
                        answer = loweredClientAnswer;
                        running = false;
                        break;
                    }
                }
                if (!validAnswer) {
                    send("Resposta invalida!");
                }
            } else {
                break;
            }
        }
        return answer;
    }

    public String qABucleForAWhile(String question, Map<String, String> mapOptions, String extraWord, String exitWord, long milliseconds) {
        if (extraWord != null) {
            mapOptions.put(extraWord, extraWord);
        }
        if (exitWord == null) {
            exitWord = "sair";
        }
        Set<String> keys = mapOptions.keySet();
        String arr[] = new String[mapOptions.size()];
        keys.toArray(arr);
        String answer = qABucleForAWhile(question, arr, exitWord, milliseconds);        
        if (answer == null) {
            return answer;
        } 
        else if (answer.equals(exitWord)) {
            return answer;
        } else {
            return mapOptions.get(answer);
        }
    }

    private boolean checkForObjectInput(long milliseconds) {
        long startTime = System.currentTimeMillis();
        long elapsedTime = 0;
        boolean isObjectInput = false;
        while (elapsedTime < milliseconds) {
            try {                
                if (i.available() > 0) {
                    isObjectInput = true;
                    break;
                } else {
                    TimeUnit.MILLISECONDS.sleep(200);
                    elapsedTime = System.currentTimeMillis() - startTime;
                }
            } catch (IOException | InterruptedException e) {                
                e.printStackTrace();
            }
        }
        return isObjectInput;
    }
}