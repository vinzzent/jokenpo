package server;

import java.util.HashMap;
import java.util.Map;

public class LobbyService extends EntryService {
    private boolean challengeSender = false;

    protected LobbyService(UserAgent uA) {
        super(uA);

    }

    private boolean isInLobby() {
        return Server.lobbyServices.containsKey(getPk());
    }

    private synchronized void removeUserFromLobby(String pk) {
        Server.lobbyServices.remove(pk);
    }

    private Map<String, String> getUsersInLobby() {
        Map<String, String> resultMap = new HashMap<>();
        int n = 0;
        for (String k : Server.lobbyServices.keySet()) {
            if (!k.equals(getPk())) {
                resultMap.put("" + n, k);
                n++;
            }
        }
        return resultMap;
    }

    private void displayUsersInLobby(Map<String, String> usersInLobby) {
        for (String k : usersInLobby.keySet()) {
            String msg = k + " - " + usersInLobby.get(k) + " : " + Server.lobbyServices.get(usersInLobby.get(k)).uA.getUserName();
            uA.send(msg);
        }
    }

    protected boolean isChallengeSender() {
        return challengeSender;
    }

    private void setChallengeSender(boolean challengeSender) {
        this.challengeSender = challengeSender;
    }


    @Override
    public void run() {
        boolean running = true;
        System.out.println("Lobby service for client " + uA.getPk() + " started");
        while (running) {
            if (isInLobby()) {
                if ((Server.lobbyServices.size() - 1) == 0) {
                    String[] validOptions = {"a"};
                    String option = uA.qABucleForAWhile("Só vocë está conectado. Pressione a tecla 'a' para atualizar a lista de usuários e receber desafíos. Será feita uma atualização automática em 5,00 segundos.", validOptions, null, (long) 5000);
                    if (null == option) {
                    } else {
                        switch (option) {
                            case "sair":
                                uA.send("");
                                running = false;
                                break;
                            case "a":
                                break;
                        }
                    }
                } else {
                    Map<String, String> usersInLobby = getUsersInLobby();
                    int n = usersInLobby.size() + 1;
                    double t = Math.log10(n)*20000;
                    uA.send("Número de usuários no lobby: " + n + ". " + "Tempo até a proxima atualização automática: " + String.format("%,.2f", t/1000) + " segundos.");
                    displayUsersInLobby(usersInLobby);
                    String option = uA.qABucleForAWhile("Selecione o jogador a quem deseja desafiar ou pressione a tecla 'a' para atualizar a lista de usuários.", usersInLobby, "a", null, (long) t);
                    if (option != null) {                       
                        switch (option) {
                            case "sair":                               
                                uA.send("");
                                running = false;                                
                                break;
                            case "a":
                                break;
                            default:                                
                                if (isInLobby()) {
                                    LobbyService oponentService = Server.lobbyServices.get(option);
                                    if (oponentService != null) {                                        
                                        if(!oponentService.isChallengeSender()) {
                                            running = false;                              
                                            removeUserFromLobby(oponentService.getPk());
                                            setChallengeSender(true);
                                            ChallengeService challenge = new ChallengeService(oponentService.getPk(), getUserAgent());
                                            Server.challengeServices.put(challenge.getPk(), challenge);
                                            challenge.start();                                                                                       
                                        } else {
                                            uA.send("O usuário selecionado não está disponível neste momento.");                                           
                                        }                                        
                                    } else {
                                        uA.send("O usuário selecionado não está disponível neste momento.");
                                    }
                                } else {
                                    uA.send("Um usuário desafiou você.");
                                }
                                break;
                            
                        }
                    } else { // Do nothing, it's Ok.
                    }
                }
            } else {
                if (!isChallengeSender()) {
                    ChallengeService challenge = Server.challengeServices.get(getPk());
                    if (challenge != null) {                       
                        challenge.setOponentAgent(getUserAgent());
                        running = false;
                        System.out.println("Client " + getPk() + " entered to the challenge service " + getPk());
                    } else {
                        uA.send("Um usuário desafiou você mas o desafío não foi encontrado.");
                        if(!Server.lobbyServices.containsKey(getPk())) {
                            Server.lobbyServices.put(getPk(), this);
                        }
                    }
                } else {
                    uA.send("Alguma coisa deu errada. Não está no lobby e enviou o desafío, porém não saiu da thread lobby. Continuará no lobby.");
                    if(!Server.lobbyServices.containsKey(getPk())) {
                        Server.lobbyServices.put(getPk(), this);
                    }
                }
            }
        }
        removeUserFromLobby(getPk());        
        System.out.println("Lobby service for client " + uA.getPk() + " ended");
    }
}