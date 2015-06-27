package pc_6_3_2_filosofos;

import messagepassing.Channel;
import messagepassing.Selector;

public class Ej_Filosofos {
	private static final int NUM_FILOSOFOS = 5;

    private static final String WAITING = "waiting";
    private static final String EATING = "eating";

    private static Channel[] outbox = new Channel[NUM_FILOSOFOS];
    private static Channel[] inbox = new Channel[NUM_FILOSOFOS];
    private static boolean[] forksAvailable = new boolean[NUM_FILOSOFOS];
    private static String[] philosophers = new String[NUM_FILOSOFOS];

    public static void main(String[] args) {

        Selector sel = new Selector();
        initializeChannels(sel);
        initializeForks();
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            philosophers[i] = WAITING;
        }

        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            Filosofo philosopher = new Filosofo(i, outbox[i], inbox[i]);
            new Thread(philosopher).start();
        }

        while (true) {

            updateGuardsOutbox();
            updateGuardsInbox();

            int selectedChannelId = sel.selectOrBlock() - 1;

            if (isOutboxProcess(selectedChannelId)) {

                philosophers[selectedChannelId] = EATING;

                forksAvailable[selectedChannelId] = false;
                forksAvailable[(selectedChannelId + 1) % NUM_FILOSOFOS] = false;

                outbox[selectedChannelId].send(new String());

            } else {
                selectedChannelId -= NUM_FILOSOFOS;
                philosophers[selectedChannelId] = WAITING;

                forksAvailable[selectedChannelId] = true;
                forksAvailable[(selectedChannelId + 1) % NUM_FILOSOFOS] = true;
                inbox[selectedChannelId].receive();
            }
        }

    }

    protected static boolean isOutboxProcess(int selectedChannelId) {
        return selectedChannelId < NUM_FILOSOFOS;
    }

    private static void updateGuardsInbox() {
        for (int i = 0; i < NUM_FILOSOFOS; i++) {

            String state = philosophers[i];
            inbox[i].setGuardValue(state == EATING);
        }
    }

    private static void updateGuardsOutbox() {
        for (int i = 0; i < NUM_FILOSOFOS; i++) {

            int leftIndex = i;
            int rightIndex = (i + 1) % NUM_FILOSOFOS;

            boolean bothForksAvailable = forksAvailable[leftIndex] && forksAvailable[rightIndex];
            outbox[i].setGuardValue(bothForksAvailable);
        }
    }

    protected static void initializeForks() {
        for (int i = 0; isOutboxProcess(i); i++) {
            forksAvailable[i] = true;
        }
    }

    private static void initializeChannels(Selector sel) {

        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            outbox[i] = new Channel();
            sel.addSelectable(outbox[i], true);

        }

        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            inbox[i] = new Channel();
            sel.addSelectable(inbox[i], false);
        }
    }
}
