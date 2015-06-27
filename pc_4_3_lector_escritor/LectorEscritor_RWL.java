package pc_4_3_lector_escritor;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import pc_0_aux.Auxiliares;

public class LectorEscritor_RWL {

	private static ArrayList<String> bd = new ArrayList<>();
	private static ReadWriteLock lock = new ReentrantReadWriteLock();
	private static Lock r = lock.readLock();
	private static Lock w = lock.writeLock();
	private static final int NUM_HILOS = 25;
	private static Thread[] hilos = new Thread[NUM_HILOS];
	
	public static void escribe(int i){
		String nombre = Thread.currentThread().getName();
		w.lock();
		Auxiliares.sleepRandom(3000);
		try{
			bd.add("#" + i);
			System.out.println(nombre + ": he escrito -> #" + i);
		}finally{
			w.unlock();
		}
	}
	
	public static void lee(){
		String nombre = Thread.currentThread().getName();
		r.lock();
		Auxiliares.sleepRandom(1000);
		try{
			if(bd.size()==0){
				System.out.println(nombre + ": ¡Pero esto está vacío!");
			} else{
				int i = bd.size()-1;
				System.out.println(nombre + ": leo [" + bd.get(i) + "]");
			}
		}finally{
			r.unlock();
		}
	}
	
	public static void main(String[] args) {
		for(int i = 0; i < NUM_HILOS; i++){
			createThread("hilo", i);
		}
		startThreadsAndWait();
	}
	
	 
	
	//----
		 private static void createThread(final String s, final int i){
			 Thread th = new Thread(new Runnable(){
				 @Override public void run(){
					int random_number = (int) Math.round(Math.random()*i);
					if (random_number%2 == 0){
						Auxiliares.sleepRandom(500);
						escribe(i);
					}
					else{
						Auxiliares.sleepRandom(500);
						lee();
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
