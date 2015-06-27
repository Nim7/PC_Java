package pc_4_2_a_lock_museo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


import pc_0_aux.Auxiliares;
import pc_4_2_a_sync_museo.Visitante;
public class Museo {
	private static Lock xLock = new ReentrantLock();
	private static int dentro = 0;
	private static Visitante v = new Visitante();
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
						xLock.lock();
						try {
							haySitio = dentro < MAX_VISITANTES;
						}finally{
							xLock.unlock();
						}
						if(haySitio){
							xLock.lock();
							try{
								dentro++;
							}finally{
								xLock.unlock();
							}
							v.visita(nombre);
							Auxiliares.sleepRandom(2000);
							xLock.lock();
							try{
								dentro--;
							}finally{
								xLock.unlock();
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
