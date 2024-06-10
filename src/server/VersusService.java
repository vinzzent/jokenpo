package server;

public class VersusService extends ChallengeService {

    protected VersusService(String pk, UserAgent uA, UserAgent oA, int nRounds) {
        super(pk, uA);
        setOponentAgent(oA);
        setNRounds(nRounds);   
    }

    @Override
    public void run() {
        boolean running = true;
        int nInv = 0;
        int nTie = 0;
        int nUa = 0;
        int nOa = 0;
        int n = 0;
        uA.send("");
        oA.send("");
        uA.send("JOKENPÔ " + uA.getPk() + " : " + uA.getUserName() + " V/S " + oA.getPk() + " : " + oA.getUserName());
        oA.send("JOKENPÔ " + uA.getPk() + " : " + uA.getUserName() + " V/S " + oA.getPk() + " : " + oA.getUserName());
        while(running) {
            n++;
            uA.send("");
            oA.send("");           
            QAService uAPlayer = new QAService(uA, n + ". PEDRA, PAPEL OU TESOURA? 10 segundos para responder.", JokenpoLogic.getValidWords(), null, (long) 10000);
            QAService oAPlayer = new QAService(oA, n + ". PEDRA, PAPEL OU TESOURA? 10 segundos para responder.", JokenpoLogic.getValidWords(), null, (long) 10000);            
            uAPlayer.start();
            oAPlayer.start();
            try {
                uAPlayer.join();
                oAPlayer.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String uAWord = uAPlayer.getAnswer();
            String oAWord = oAPlayer.getAnswer();
            uA.send("");
            oA.send("");   
            if (uAWord != null && oAWord != null) {
                if (uAWord.equals("sair") && oAWord.equals("sair")) {
                    uA.send("PLACAR FINAL >>> Vitórias: " + nUa + " | Derrotas: " + nOa + " | Empates: " + nTie + " | Rodadas inválidas: " + nInv + " | Total de rodadas: " + (nUa + nOa + nTie + nInv) + ".");
                    oA.send("PLACAR FINAL >>> Vitórias: " + nOa + " | Derrotas: " + nUa + " | Empates: " + nTie + " | Rodadas inválidas: " + nInv + " | Total de rodadas: " + (nUa + nOa + nTie + nInv) + ".");
                    break;            
                } else if (uAWord.equals("sair")) {
                    uA.send("PLACAR FINAL >>> Vitórias: " + nUa + " | Derrotas: " + nOa + " | Empates: " + nTie + " | Rodadas inválidas: " + nInv + " | Total de rodadas: " + (nUa + nOa + nTie + nInv) + ".");
                    oA.send("PLACAR FINAL >>> Vitórias: " + nOa + " | Derrotas: " + nUa + " | Empates: " + nTie + " | Rodadas inválidas: " + nInv + " | Total de rodadas: " + (nUa + nOa + nTie + nInv) + ".");
                    oA.send("O usuário " + uA.getPk() + " : " + uA.getUserName() + " se desconectou.");
                    LobbyService lobbyService = new LobbyService(oA);
                    Server.lobbyServices.put(lobbyService.getPk(), lobbyService);
                    lobbyService.start();
                    break;
                } else if (oAWord.equals("sair")) {
                    uA.send("PLACAR FINAL >>> Vitórias: " + nUa + " | Derrotas: " + nOa + " | Empates: " + nTie + " | Rodadas inválidas: " + nInv + " | Total de rodadas: " + (nUa + nOa + nTie + nInv) + ".");
                    oA.send("PLACAR FINAL >>> Vitórias: " + nOa + " | Derrotas: " + nUa + " | Empates: " + nTie + " | Rodadas inválidas: " + nInv + " | Total de rodadas: " + (nUa + nOa + nTie + nInv) + ".");
                    uA.send("O usuário " + oA.getPk() + " : " + oA.getUserName() + " se desconectou.");
                    LobbyService lobbyService = new LobbyService(uA);
                    Server.lobbyServices.put(lobbyService.getPk(), lobbyService);
                    lobbyService.start();
                    break;
                } else {
                    uA.send("A palavra do usuário " + oA.getPk() + " : " + oA.getUserName() + " foi " + oAWord + ".");
                    oA.send("A palavra do usuário " + uA.getPk() + " : " + uA.getUserName() + " foi " + uAWord + ".");
                    String result = JokenpoLogic.checkResult(uAWord, oAWord);
                    switch (result) {
                        case "0":
                            nTie ++;
                            uA.send("Empate, você lançou a mesma palavra.");
                            oA.send("Empate, você lançou a mesma palavra.");
                            break;
                        case "1":
                            nUa ++;
                            uA.send("A sua palavra " + uAWord + " ganha de " + oAWord + ".");
                            oA.send("A sua palavra " + oAWord + " perde de " + uAWord + ".");
                            break;
                        case "2":
                            nOa ++;                        
                            oA.send("A sua palavra " + oAWord + " ganha de " + uAWord + ".");
                            uA.send("A sua palavra " + uAWord + " perde de " + oAWord + ".");
                            break;
                        default:
                            break;
                    }
                }
            } else if (uAWord == null && oAWord == null) {
                nInv++;
                uA.send("O usuário " + oA.getPk() + " : " + oA.getUserName() + " e você demoraram muito. Rodada inválida.");
                oA.send("O usuário " + uA.getPk() + " : " + uA.getUserName() + " e você demoraram muito. Rodada inválida.");
            } else if (uAWord == null && oAWord != null) {
                if (oAWord.equals("sair")) {
                    uA.send("PLACAR FINAL >>> Vitórias: " + nUa + " | Derrotas: " + nOa + " | Empates: " + nTie + " | Rodadas inválidas: " + nInv + " | Total de rodadas: " + (nUa + nOa + nTie + nInv) + ".");
                    oA.send("PLACAR FINAL >>> Vitórias: " + nOa + " | Derrotas: " + nUa + " | Empates: " + nTie + " | Rodadas inválidas: " + nInv + " | Total de rodadas: " + (nUa + nOa + nTie + nInv) + ".");
                    uA.send("O usuário " + oA.getPk() + " : " + oA.getUserName() + " se desconectou.");
                    LobbyService lobbyService = new LobbyService(uA);
                    Server.lobbyServices.put(lobbyService.getPk(), lobbyService);
                    lobbyService.start();
                    break;
                } else {
                    nOa++;
                    oA.send("O usuário " + uA.getPk() + " : " + uA.getUserName() + " demorou muito. Ponto para você!");
                    uA.send("Você demorou muito. Ponto para o usuário " + oA.getPk() + " : " + oA.getUserName() + ", que lançou a palavra " + oAWord + ".");
                }
            } else if (oAWord == null && uAWord != null) {
                if (uAWord.equals("sair")) {
                    uA.send("PLACAR FINAL >>> Vitórias: " + nUa + " | Derrotas: " + nOa + " | Empates: " + nTie + " | Rodadas inválidas: " + nInv + " | Total de rodadas: " + (nUa + nOa + nTie + nInv) + ".");
                    oA.send("PLACAR FINAL >>> Vitórias: " + nOa + " | Derrotas: " + nUa + " | Empates: " + nTie + " | Rodadas inválidas: " + nInv + " | Total de rodadas: " + (nUa + nOa + nTie + nInv) + ".");
                    oA.send("O usuário " + uA.getPk() + " : " + uA.getUserName() + " se desconectou.");
                    LobbyService lobbyService = new LobbyService(oA);
                    Server.lobbyServices.put(lobbyService.getPk(), lobbyService);
                    lobbyService.start();
                    break;
                } else {
                    nUa++;
                    uA.send("O usuário " + oA.getPk() + " : " + oA.getUserName() + " demorou muito. Ponto para você!");
                    oA.send("Você demorou muito. Ponto para o usuário " + uA.getPk() + " : " + uA.getUserName() + ": " + uAWord + ", que lançou a palavra " + uAWord + ".");
                }                
            }
            uA.send("PLACAR >>> Vitórias: " + nUa + " | Derrotas: " + nOa + " | Empates: " + nTie + " | Rodadas inválidas: " + nInv + " | Total de rodadas: " + (nUa + nOa + nTie + nInv) + ".");
            oA.send("PLACAR >>> Vitórias: " + nOa + " | Derrotas: " + nUa + " | Empates: " + nTie + " | Rodadas inválidas: " + nInv + " | Total de rodadas: " + (nUa + nOa + nTie + nInv) + ".");
            if (getNRounds() > 0 && (nUa + nOa + nTie + nInv) >= getNRounds()) {
                uA.send("PLACAR FINAL >>> Vitórias: " + nUa + " | Derrotas: " + nOa + " | Empates: " + nTie + " | Rodadas inválidas: " + nInv + " | Total de rodadas: " + (nUa + nOa + nTie + nInv) + ".");
                oA.send("PLACAR FINAL >>> Vitórias: " + nOa + " | Derrotas: " + nUa + " | Empates: " + nTie + " | Rodadas inválidas: " + nInv + " | Total de rodadas: " + (nUa + nOa + nTie + nInv) + ".");
                if (uA != null) {
                    LobbyService uAlS = new LobbyService(uA);
                    Server.versusServices.remove(uAlS.getPk());
                    uAlS.start();
                }
                if (oA != null) {
                    LobbyService oAlS = new LobbyService(oA);
                    Server.lobbyServices.put(oAlS.getPk(), oAlS);
                    oAlS.start();
                }
                break;
            }            
        }        
        Server.versusServices.remove(getPk());
        System.out.println("Versus service " + getPk() + " ended");   
    }
}
