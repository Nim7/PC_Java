/*
 * El encargado de un comedor quiere regular con semáforos el acceso de los alumnos a 
 * las mesas de dicho comedor. Cuando un alumno llega, busca una silla libre. Si no 
 * encuentra sitio, se va; si localiza una silla libre, la reserva poniendo su mochila en
 * ella. Una vez reservada la silla, se va a buscar una bandeja. 
 * 		* Si no hay bandejas libres, esperará haciendo cola hasta que haya disponible 
 * 		  alguna bandeja. 
 * 		* Si hay bandejas, se pondrá a la cola para que le sirvan la comida. 
 * 
 * El número de sillas del comedor es S y el de bandejas B.
 */
package pc_3_4_comedor;
import java.util.concurrent.Semaphore;

import pc_0_aux.Auxiliares;

public class Comedor {
	private static final int NUM_SILLAS = 7;
	private static final int NUM_BANDEJAS = 5;
	private static final int NUM_NINOS = 30;
	private static final Thread[] hilos = new Thread[NUM_NINOS];
	private static final Semaphore smp_sillas = new Semaphore(NUM_SILLAS);
	private static final Semaphore smp_bandejas = new Semaphore(NUM_BANDEJAS);
	
	public static void main(String[] args) {
		for(int i=0; i<NUM_NINOS; i++){ 
			 createThread("comer", i); 
		 } 
		 startThreadsAndWait(); 	
	}
	
	
	private static void comer(int numNino) {
		if(smp_sillas.tryAcquire()){
			System.out.println(numNino + " reserva silla.");
			Auxiliares.sleepRandom(700);
			try {
				smp_bandejas.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println(numNino + " come.");
			Auxiliares.sleepRandom(300);
			smp_bandejas.release();
			System.out.println(numNino + " deja la bandeja y se va.");
			smp_sillas.release();
		}
		else{
			System.out.println(numNino + ": 'No hay sitio :( me voy'. ");
		}
	}
	//----
	
	private static void createThread(final String s, final int i){
		 Thread th = new Thread(new Runnable(){
			 @Override public void run(){
				Auxiliares.sleepRandom(2000);
				comer(i);
			 }
		 });
		 hilos[i] = th;
	 }



	private static void startThreadsAndWait(){
		 for (int i = 0; i < hilos.length; i++) {
			hilos[i].start();
		}
	 }

}
