package pc_3_1_metro;

import java.util.concurrent.Semaphore;

import pc_0_aux.Auxiliares;

public class Subway {
	private static final int NUM_TRAMOS = 3;
	private static final int NUM_TRENES = 5; 
	private static final Thread[] hilos = new Thread[NUM_TRENES];
	private static Semaphore tramo[] = new Semaphore[NUM_TRAMOS];

	 public static void tren(int numTren) throws InterruptedException { 
		 for (int numTramo = 0; numTramo < NUM_TRAMOS; numTramo++) { 
			 recorrerTramo(numTren, numTramo);
		 } 
	 } 

	 private static void recorrerTramo(int numTren, int numTramo) throws InterruptedException {
		 tramo[numTramo].acquire();
		 Auxiliares.println("Tren"+numTren+" recorriendo el Tramo"+numTramo);
		 Auxiliares.sleepRandom(500);
		 Auxiliares.println("Tren"+numTren + " y ha terminado con el tramo "+ numTramo);
		 tramo[numTramo].release();
	 } 

	 public static void main(String args[]){ 
		 for(int i = 0; i < NUM_TRAMOS; i++){
			 tramo[i] = new Semaphore(1);
		 }
		 
		 for(int i=0; i<NUM_TRENES; i++){ 
			 createThread("tren", i); 
		 } 
		 startThreadsAndWait(); 
	 }
	 
	 
	 //----
	 private static void createThread(final String s, final int i){
		 Thread th = new Thread(new Runnable(){
			 @Override public void run(){
				System.out.println(s + "#" + i + " ready!");
				try {
					tren(i);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
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
