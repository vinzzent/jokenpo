package server;

public class SoloService extends EntryService {

    public SoloService(UserAgent uA) {
        super(uA);
    }
    
    @Override
    public void run() {
        boolean running = true;
        int nClientWon = 0;
        int nCpuWon = 0;
        int nTie = 0;
        System.out.println("Solo service for client " + uA.getPk() + " started");
        while (running) {
            String clientWord = uA.qABucle("Pedra, papel ou tesoura?", JokenpoLogic.getValidWords(), null);
            if (clientWord.equals("sair")) {
                break;
            }
            String cpuWord = JokenpoLogic.randomWeapon();
            String roundResult = JokenpoLogic.checkResult(clientWord, cpuWord);
            switch (roundResult) {
                case "0": {
                    uA.send("EMPATE >>> cliente: " + clientWord + " - servidor: " + cpuWord);
                    nTie += 1;
                    break;
                }
                case "1": {
                    uA.send("O CLIENTE GANHOU >>> cliente: " + clientWord + " - servidor: " + cpuWord);
                    nClientWon += 1;
                    break;
                }
                case "2": {
                    uA.send("O SERVIDOR GANHOU >>> cliente: " + clientWord + " - servidor: " + cpuWord);
                    nCpuWon += 1;
                    break;
                }
            }
            uA.send("PLACAR >>> CLIENTE: " + nClientWon + " | SERVIDOR: " + nCpuWon + " | EMPATE: " + nTie + " | TOTAL RODADAS: " + (nClientWon + nCpuWon + nTie));
        }
        uA.send("PLACAR >>> CLIENTE: " + nClientWon + " | SERVIDOR: " + nCpuWon + " | EMPATE: " + nTie + " | TOTAL RODADAS: " + (nClientWon + nCpuWon + nTie));
        uA.send("Servidor encerra serviço contra a CPU: " + getPk());
        Server.soloServices.remove(getPk());
        System.out.println("Solo service for client " + uA.getPk() + " ended");
    }        
}