/*
 * Para incentivar las visitas, cuando una persona entre en el museo estando vacío,
 * será obsequiado con un regalo. Las personas, después de saludar, dicen si les han 
 * dado un regalo (“Tengo regalo”) o si no (“No tengo regalo”). Las personas deben 
 * permitir que otras personas hablen entre el saludo y el comentario sobre el regalo. 
 */
package pc_4_2_c_lock_museo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import pc_0_aux.Auxiliares;

public class Museo {
	private static int dentro = 0;
	private static Lock xLock = new ReentrantLock();
	private static final int MAX_VISITANTES = 3;
	private static final int NUM_PERSONAS = 30;
	private static  Thread[] hilos = new Thread[NUM_PERSONAS];

	
	public static void main(String[] args) {
		
		for (int i = 0; i < NUM_PERSONAS; i++) {
			Thread t = new Thread(new Runnable() {
				public void run() { 
					String nombre = Thread.currentThread().getName();
					Auxiliares.sleepRandom(1000);
					boolean haySitio, tieneRegalo = false;
					xLock.lock();
					try{
						haySitio = dentro < MAX_VISITANTES;
						tieneRegalo = dentro==0;
					}finally{
						xLock.unlock();
					}
					if(haySitio){
						xLock.lock();
						try{
							dentro++;
							System.out.println(nombre + ": ¡Hola!");	
						}finally{
							xLock.unlock();
						}
						if(tieneRegalo){
							System.out.println(nombre + ": ¡Me ha tocado un regalito!");
						}
						else{
							System.out.println(nombre + ": Jo, no tengo regalo.");
						}
						Auxiliares.sleepRandom(2000);
						xLock.lock();
						try{
							dentro--;
							System.out.println(nombre + ": Adiós!");
						}finally{
							xLock.unlock();
						}
					}
					else{
						System.out.println(nombre + ": No entro ._.");
					}
				}
			});
			hilos[i] = t;
		}
		for(int i = 0; i < NUM_PERSONAS; i++)
			hilos[i].start();
	}
	
}