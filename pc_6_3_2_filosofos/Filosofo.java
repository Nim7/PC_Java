package pc_6_3_2_filosofos;

import pc_0_aux.Auxiliares;
import messagepassing.Channel;

public class Filosofo implements Runnable {

    private int id;
    private Channel incoming, outbox;

    public Filosofo(int id, Channel inbox, Channel outbox) {
        super();
        this.id = id;
        this.incoming = inbox;
        this.outbox = outbox;
    }

    @Override
    public void run() {

        while (true) {
            incoming.receive();
			System.out.println("Filósofo " + id + " comiendo");
			Auxiliares.sleepRandom(2000);
			System.out.println("Filósofo " + id + " terminó de comer");
			outbox.send(new String());

        }
    }

}
