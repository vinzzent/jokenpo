package server;

public class ChallengeService extends EntryService {
    protected UserAgent oA; // OponentAgent

    protected ChallengeService(String pk, UserAgent uA) {
        super(uA);
        setPk(pk);
    }

    public boolean isComplete() {
        return uA != null && oA != null;
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
            if (!isComplete()) {
                uA.qABucleForAWhile("Aguardando resposta do oponente...", null, null, (long) 2000);
                if (!isComplete() && oA != null) {
                    oA.send("Um usuário desafiou você mas já não está mais disponível.");
                    LobbyService service = new LobbyService(oA);
                    Server.lobbyServices.put(service.getPk(), service);
                    service.start();
                    break;
                }
            } else {
                uA.qABucleForAWhile("Aguardando resposta do oponente...", null, null, (long) 2000);
                String[] validAnswers = {"sim", "nao"};
                String response = oA.qABucleForAWhile("Foi desafiado pelo usuário: " + uA.getPk()+" : "+uA.getUserName()+". Aceita? (sim/nao)", validAnswers, null, (long) 2000);
                if (response != null) {
                    switch (response) {
                        case "sair":
                            running = false;
                            uA.send("O usuário " + oA.getPk() + " : " + oA.getUserName() + " se desconectou.");
                            LobbyService service = new LobbyService(uA);
                            Server.lobbyServices.put(service.getPk(), service);
                            service.start();
                            oA.send("");                            
                            break;
                        case "sim":
                        running = false;
                            uA.send("O usuário " + oA.getPk() + " : " + oA.getUserName() + " aceitou o desafío");
                            // criar objetos e inicializar serviço de game versus                            
                            break;
                        case "nao":
                            uA.send("O usuário " + oA.getPk() + " : " + oA.getUserName() + " rejeitou o desafío");
                            running = false;
                            LobbyService uAService = new LobbyService(uA);
                            Server.lobbyServices.put(uAService.getPk(), uAService);
                            LobbyService oAService = new LobbyService(oA);
                            Server.lobbyServices.put(oAService.getPk(), oAService);
                            uAService.start();
                            oAService.start();                            
                            break;
                        default:
                            break;
                    }
                } else {                    
                }
            }
        }
        System.out.println("Challenge service " + getPk() + " for client " + uA.getPk() + " ended");    
    }
}
