/*
 * Considerar que caben n personas dentro del museo y que cada persona al entrar
 * tiene que saludar diciendo cuántas personas hay en el museo: “hola, somos n”.
 * Al despedirse tiene que decir “adiós” y el número de personas que quedan
 * (n‐1) tras irse. 
 */
package pc_4_2_b_lock_museo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import pc_0_aux.Auxiliares;

public class Museo {
	private static int dentro = 0;
	private static Lock xLock = new ReentrantLock();
	private static final int MAX_VISITANTES = 5;
	private static final int NUM_PERSONAS = 25;
	private static  Thread[] hilos = new Thread[NUM_PERSONAS];

	
	public static void main(String[] args) {
		
		for (int i = 0; i < NUM_PERSONAS; i++) {
			Thread t = new Thread(new Runnable() {
				public void run() { 
					String nombre = Thread.currentThread().getName();
					Auxiliares.sleepRandom(1000);
					boolean haySitio = false;
					xLock.lock();
					try{
						haySitio = dentro < MAX_VISITANTES;
					} finally{
						xLock.unlock();
					}
					if(haySitio){
						xLock.lock();
						try{
							dentro++;
							System.out.println(nombre + ": Como entro, ahora somos " + dentro);	
						}finally{
							xLock.unlock();
						}
						Auxiliares.sleepRandom(2000);
						xLock.lock();
						try{
							dentro--;
							System.out.println(nombre + ": Como me he ido, ahora somos " + dentro);
						} finally{
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
