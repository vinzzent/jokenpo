package server;

public class ChallengeService extends EntryService {

    protected UserAgent oA; // OponentAgent

    protected ChallengeService(String pk, UserAgent uA) {
        super(uA);
        setPk(pk);
    }

    private void setPk(String pk) {
        this.pk = pk;
    }

    protected void setOponentAgent(UserAgent oA) {
        this.oA = oA;
    }

    @Override
    public void run() {
        boolean running = true;
        System.out.println("Challenge service " + getPk() + " for client " + uA.getPk() + " started");
        while (running) {
            if (uA != null && oA == null) {
                QAService uAQ = new QAService(uA, "Aguardando que o usuário selecionado receba o desafío...", null, null, (long) 3000);
                uAQ.start();
                try {
                    uAQ.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String uAAnswer = uAQ.getAnswer();
                if (uAAnswer != null) {
                    if (uAAnswer.equals("sair")) {                                                           
                        if (oA != null) {
                            oA.send("Foi desafiado pelo usuário " + uA.getPk() + " : " + uA.getUserName() + ", porém se desconectou.");
                            LobbyService lS = new LobbyService(oA);
                            Server.lobbyServices.put(lS.getPk(), lS);
                            lS.start();
                        }
                        uA.send(""); 
                        break;
                    }
                }
            } else if (uA != null && oA != null) {
                QAService uAQ = new QAService(uA, "Aguardando resposta do usuário...", null, null, (long) 3000);
                String[] validAnswers = {"sim", "nao"};
                QAService oAQ = new QAService(oA, "Foi desafiado pelo usuário: " + uA.getPk() + " : " + uA.getUserName() + ". Aceita? (sim/nao)", validAnswers, null, (long) 3000);
                uAQ.start();
                oAQ.start();
                try {
                    uAQ.join();
                    oAQ.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String uAAnswer = uAQ.getAnswer();
                String oAAnswer = oAQ.getAnswer();
                if (uAAnswer != null && oAAnswer != null) {
                    if (uAAnswer.equals("sair") && oAAnswer.equals("sair")) {
                        uA.send("");
                        oA.send("");
                        break;
                    }
                } else if (uAAnswer != null && oAAnswer == null) {
                    if (uAAnswer.equals("sair")) {
                        oA.send("O usuário " + uA.getPk() + " : " + uA.getUserName() + " se desconectou.");
                        uA.send("");
                        LobbyService lS = new LobbyService(oA);
                        Server.lobbyServices.put(lS.getPk(), lS);
                        lS.start();
                        break;
                    }
                } else if (oAAnswer != null && uAAnswer == null) {
                    switch (oAAnswer) {
                        case "sair": {
                            uA.send("O usuário " + oA.getPk() + " : " + oA.getUserName() + " se desconectou.");
                            oA.send("");
                            LobbyService lS = new LobbyService(uA);
                            Server.lobbyServices.put(lS.getPk(), lS);
                            lS.start();
                            running = false;
                            break;
                        }
                        case "nao": {
                            uA.send("O usuário " + oA.getPk() + " : " + oA.getUserName() + " rejeitou o desafio.");
                            LobbyService lS = new LobbyService(uA);
                            Server.lobbyServices.put(lS.getPk(), lS);
                            lS.start();
                            running = false;
                            break;
                        }
                        default:
                            uA.send("O usuário " + oA.getPk() + " : " + oA.getUserName() + " aceitou o desafio.");
                            VersusService vS = new VersusService(getPk(), uA, oA);
                            Server.versusServices.put(vS.getPk(), vS);
                            vS.start();
                            running = false;
                            break;
                    }
                } else {
                }
            }            
        }
        Server.challengeServices.remove(getPk());
        System.out.println("Challenge service " + getPk() + " ended");
    }
}