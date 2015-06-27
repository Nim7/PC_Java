package pc_4_2_a_sync_museo;

import pc_0_aux.*;

public class Museo {
	private static int dentro = 0;
	private static Visitante v = new Visitante();
	private static Object xLock = new Object();
	private static final int MAX_VISITANTES = 1;
	private static final int NUM_PERSONAS = 2;
	private static  Thread[] hilos = new Thread[NUM_PERSONAS];

	
	public static void main(String[] args) {
		
		for (int i = 0; i < NUM_PERSONAS; i++) {
			Thread t = new Thread(new Runnable() {
				public void run() { 
					String nombre = Thread.currentThread().getName();
					while(true){
						Auxiliares.sleepRandom(2000);
						boolean haySitio = false;
						synchronized (xLock) {
							haySitio = dentro < MAX_VISITANTES;
						}
						if(haySitio){
							synchronized (xLock) {
								dentro++;
							}
							v.visita(nombre);
							Auxiliares.sleepRandom(2000);
							synchronized (xLock) {
								dentro--;
							}
							System.out.println(nombre + ": Voy a dar un paseo...");
							Auxiliares.sleepRandom(5000);
							System.out.println(nombre + ": ¡No! ¡Me ha encantado! ¡Quiero repetir la visita!");
						}
						else{
							System.out.println(nombre + ": No entro ._.");
						}
					}
				}
			});
			hilos[i] = t;
		}
		for(int i = 0; i < NUM_PERSONAS; i++)
			hilos[i].start();
	}
	
}
