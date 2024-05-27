package server;

import java.util.Map;
import util.Comunication;

public class UserAgent {
    private Comunication com;
    private String userName;
    private String pk;

    public UserAgent(Comunication com) {
        setComunication(com);
        setPk(); 
    }

    private Comunication getComunication() {
        return com;
    }

    private void setComunication(Comunication com) {
        this.com = com;
    }

    protected String getUserName() {
        return userName;
    }

    protected void setUserName(String userName) {
        this.userName = userName;
    }

    protected String getPk() {
        return pk;
    }

    private void setPk() {
        this.pk = getComunication().getSocket().getInetAddress() + ":" + getComunication().getSocket().getPort();
    }

    protected void send(String msg) {
        this.com.send(msg);
    }

    protected String receive() {
        return (String) this.com.receiveString();
    }

    protected String qABucle(String question, String[] validAnswers, String exitWord) {
        return com.qABucle(question, validAnswers, exitWord);
    }
    
    protected String qABucleForAWhile(String question, String[] validAnswers, String exitWord, Long milliseconds) {
        return com.qABucleForAWhile(question, validAnswers, exitWord, milliseconds);
    }

    protected String qABucleFromMapForAWhile(String question, Map<String, String> mapOptions, String extraWord, String exitWord, long milliseconds) {
        return com.qABucleFromMapForAWhile(question, mapOptions, extraWord, exitWord, milliseconds);
    }
}



