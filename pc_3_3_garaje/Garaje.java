package pc_3_3_garaje;

import java.util.concurrent.Semaphore;

import pc_0_aux.Auxiliares;

public class Garaje {
	private static final int NUM_COCHES = 10;
	private static volatile int pendientesSubida = 0;
	private static volatile int pendientesBajada = 0;
	private static Thread[] hilos = new Thread[NUM_COCHES];
	private static Semaphore rampa =  new Semaphore(1);
	private static Semaphore subida = new Semaphore(1);
	private static Semaphore bajada = new Semaphore(1);
	
	//semaforo para rampa (que no haya más de un coche en la rampa)
	//semáforo para permiso exclusivo de subida; semáforo distinto para permiso de bajada
	//contar cuando vayan sub/bajando; cuando no queden más que sub/bajar, se cambia el permiso
	
	public static void main(String args[]){
		for(int i = 0; i < NUM_COCHES; i++){
			createThread("coche", i);
		}
		startThreadsAndWait();
	}
	
	private static void subir(int numCoche){					
		try {
			subida.tryAcquire();
			rampa.acquire();
		} catch (InterruptedException e1) {}
		System.out.println("Coche " + numCoche + " subiendo.");
		Auxiliares.sleepRandom(1200);
		pendientesSubida--;
		rampa.release();
		if(pendientesSubida == 0){
			subida.release();
		}
	}

	private static void bajar(int numCoche){
		try {
			bajada.tryAcquire();
			rampa.acquire();
		} catch (InterruptedException e1) {}
		System.out.println("Coche " + numCoche + " bajando.");
		Auxiliares.sleepRandom(1200);
		pendientesBajada--;
		rampa.release();
		if(pendientesBajada == 0){
			bajada.release();
		}
			
	}
	
	
	//----
	 private static void createThread(final String s, final int i){
		 Thread th = new Thread(new Runnable(){
			 @Override public void run(){
				int random_number = (int) Math.round(Math.random()*i);
				if (random_number%2 == 0){
					Auxiliares.sleepRandom(500);
					pendientesSubida++;
					subir(i);
				}
				else{
					Auxiliares.sleepRandom(500);
					pendientesBajada++;
					bajar(i);
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
