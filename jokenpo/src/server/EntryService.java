package server;

public class EntryService extends Thread {
    protected  UserAgent uA;
    protected String pk;

    protected EntryService(UserAgent uA) {
        setUserAgent(uA);
        setPk();
    }

    protected String getPk() {
        return pk;
    }

    private void setPk() {
        this.pk = uA.getPk();
    }

    private void setUserAgent(UserAgent uA) {
        this.uA = uA;
    }

    protected UserAgent getUserAgent() {
        return uA;
    }

    @Override
    public void run() {
        System.out.println("Entry service for client " + uA.getPk() + " started");
        String name = uA.qABucle("Qual o seu nome?", null, null);
        if (!name.equals("sair")) {
            uA.setUserName(name);
            String[] validAnswers = {"1", "2"};
            String option = uA.qABucle("Jogar (1) contra a CPU ou (2) contra outro usuário?", validAnswers, null);
            switch (option) {
                case "sair": {
                    uA.send("");
                    break;
                }
                case "1": {
                    SoloService service = new SoloService(getUserAgent());
                    Server.soloServices.put(service.getPk(), service);                   
                    service.start();
                    break;
                }
                case "2": {                   
                    LobbyService service = new LobbyService(getUserAgent());
                    Server.lobbyServices.put(service.getPk(), service);
                    service.start();
                    break;
                }
            }
        } else {
            uA.send("");                
        }
        Server.entryServices.remove(getPk());
        System.out.println("Entry service for client " + uA.getPk() + " ended");
    }
}
