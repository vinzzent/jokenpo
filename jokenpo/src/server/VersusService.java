package server;

public class VersusService extends ChallengeService {

    protected VersusService(String pk, UserAgent uA, UserAgent oA) {
        super(pk, uA);
        setOponentAgent(oA);       
    }

    @Override
    public void run() {
        boolean running = true;
        int nInv = 0;
        int nTie = 0;
        int nUa = 0;
        int nOa = 0;
        while(running) {
            QAService uAPlayer = new QAService(uA, "PEDRA, PAPEL OU TESOURA? 5 segundos para responder.", JokenpoLogic.getValidWords(), null, (long) 5000);
            QAService oAPlayer = new QAService(oA, "PEDRA, PAPEL OU TESOURA? 5 segundos para responder.", JokenpoLogic.getValidWords(), null, (long) 5000);            
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
            if (uAWord != null && oAWord != null) {
                if (uAWord.equals("sair") && oAWord.equals("sair")) {
                    uA.send("");
                    oA.send("");
                    break;            
                } else if (uAWord.equals("sair")) {
                    oA.send("O usuário " + uA.getPk() + " : " + uA.getUserName() + " se desconectou.");
                    LobbyService lobbyService = new LobbyService(oA);
                    Server.lobbyServices.put(lobbyService.getPk(), lobbyService);
                    lobbyService.start();
                    break;
                } else if (oAWord.equals("sair")) {
                    uA.send("O usuário " + oA.getPk() + " : " + oA.getUserName() + " se desconectou.");
                    LobbyService lobbyService = new LobbyService(oA);
                    Server.lobbyServices.put(lobbyService.getPk(), lobbyService);
                    lobbyService.start();
                    break;
                } else {
                    uA.send("A palavra do usuário " + oA.getPk() + " : " + oA.getUserName() + " foi " + oAWord + ".");
                    oA.send("A palavra do usuário " + uA.getPk() + " : " + uA.getUserName() + " foi " + uAWord + ".");
                    String result = JokenpoLogic.checkResult(uAWord, oAWord);
                    if (result.equals("0")) {
                        nTie ++;
                        uA.send("Empate, você lançou a mesma palavra.");
                        oA.send("Empate, você lançou a mesma palavra.");                       
                    } else if (result.equals("1")) {
                        nUa ++;
                        uA.send("A sua palavra " + uAWord + " ganha de " + oAWord + ".");
                        oA.send("A sua palavra " + oAWord + " perde de " + uAWord + ".");
                    } else if (result.equals("2")) {
                        nOa ++;
                        oA.send("A sua palavra " + oAWord + " ganha de " + uAWord + ".");
                        uA.send("A sua palavra " + uAWord + " perde de " + oAWord + ".");
                    } else {                        
                    }
                }
            } else if (uAWord == null && oAWord == null) {
                nInv++;
                uA.send("O usuário " + oA.getPk() + " : " + oA.getUserName() + " e você demoraram muito. Rodada inválida.");
                oA.send("O usuário " + uA.getPk() + " : " + uA.getUserName() + " e você demoraram muito. Rodada inválida.");
            } else if (uAWord == null && oAWord != null) {
                nOa++;
                oA.send("O usuário " + uA.getPk() + " : " + uA.getUserName() + " demorou muito. Ponto para você!");
                uA.send("Você demorou muito. Ponto para o usuário " + oA.getPk() + " : " + oA.getUserName() + ", que lançou a palavra " + oAWord + ".");
            } else if (oAWord == null && uAWord != null) {
                nUa++;
                uA.send("O usuário " + oA.getPk() + " : " + oA.getUserName() + " demorou muito. Ponto para você!");
                oA.send("Você demorou muito. Ponto para o usuário " + uA.getPk() + " : " + uA.getUserName() + ": " + uAWord + ", que lançou a palavra " + uAWord + ".");
            }
            uA.send("PLACAR >>> Vitórias: " + nUa + " | Derrotas: " + nOa + " | Empates: " + nTie + " | Rodadas inválidas: " + nInv + " | Total de rodadas: " + (nUa + nOa + nTie + nInv) + ".");
            oA.send("PLACAR >>> Vitórias: " + nOa + " | Derrotas: " + nUa + " | Empates: " + nTie + " | Rodadas inválidas: " + nInv + " | Total de rodadas: " + (nUa + nOa + nTie + nInv) + ".");
        }            
    }
}
