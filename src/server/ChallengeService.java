package server;

public class ChallengeService extends EntryService {

    protected UserAgent oA; // OponentAgent
    int nRounds = 0;

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

    protected int getNRounds() {
        return nRounds;
    }

    protected void setNRounds(int nRounds) {
        this.nRounds = nRounds;
    }

    @Override
    public void run() {
        boolean running = true;
        System.out.println("Challenge service " + getPk() + " for client " + uA.getPk() + " started");
        while (running) {
            if (nRounds == 0) {
                String roundsChoices[] = {"i", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21"};
                QAService uAQ = new QAService(uA, "Quantas rodadas propõe? Máximo 21 rodadas. Insira a letra 'i' para indefinidas. Responda dentro dos próximos 10 segundos, caso contrário, será proposta uma partida sem limíte de rodadas.", roundsChoices, null, (long) 10000);
                uAQ.start();
                try {
                    uAQ.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String uAAnswer = uAQ.getAnswer();
                if (uAAnswer != null) {
                    switch (uAAnswer) {
                        case "sair":
                            if (oA != null) {
                                oA.send("Foi desafiado pelo usuário " + uA.getPk() + " : " + uA.getUserName() + ", porém se desconectou.");
                                LobbyService lS = new LobbyService(oA);
                                Server.lobbyServices.put(lS.getPk(), lS);
                                lS.start();
                            }
                            uA.send("");
                            running = false;
                            break;
                        case "i":
                            setNRounds(-1);
                            break;
                        default:
                            int n = Integer.parseInt(uAAnswer);
                            setNRounds(n);
                            break;
                    }
                } else {
                    setNRounds(-1);                    
                }
            }
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
                String rounds;
                if (getNRounds() == -1) {
                    rounds = "Indefinidas";                    
                } else {
                    rounds = Integer.toString(getNRounds());
                }
                String[] validAnswers = {"s", "n"};                
                QAService oAQ = new QAService(oA, "Foi desafiado pelo usuário: " + uA.getPk() + " : " + uA.getUserName() + ". Número de rodadas: " + rounds + ". Aceita? (s/n)", validAnswers, null, (long) 5000);
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
                        case "n": {
                            uA.send("O usuário " + oA.getPk() + " : " + oA.getUserName() + " rejeitou o desafio.");
                            LobbyService uAlS = new LobbyService(uA);
                            LobbyService oAlS = new LobbyService(oA);
                            Server.lobbyServices.put(uAlS.getPk(), uAlS);
                            Server.lobbyServices.put(oAlS.getPk(), oAlS);
                            uAlS.start();
                            oAlS.start();
                            running = false;
                            break;
                        }
                        default:
                            uA.send("O usuário " + oA.getPk() + " : " + oA.getUserName() + " aceitou o desafio.");
                            VersusService vS = new VersusService(getPk(), uA, oA, nRounds);
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