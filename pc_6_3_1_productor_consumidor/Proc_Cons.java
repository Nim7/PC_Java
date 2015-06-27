package pc_6_3_1_productor_consumidor;

import java.util.ArrayList;

import pc_0_aux.Auxiliares;
import messagepassing.Channel;
import messagepassing.Selector;

public class Proc_Cons {
	private static final int MAX_OBJ = 10;
	private static final int NUM_HILOS_POR_PROCESO = 6;
	private static Channel[] canales_prod = new Channel[NUM_HILOS_POR_PROCESO];
	private static Channel[] canales_consum = new Channel[NUM_HILOS_POR_PROCESO];
	
	private static ArrayList<Integer> buffer = new ArrayList<Integer>();
	
	
	//se bloquean cuando el buffer está lleno
	private static void productor(int id){
		while(true){
			Auxiliares.sleepRandom(1000);
			int dato = (int) Math.round(Math.random()*MAX_OBJ + 6);
			Integer d = new Integer(dato);
			canales_prod[id].send(d);
		}
		
	}
	
	//Se bloquean cuando el buffer está vacío
	private static void consumidor(int id){
		while(true){
			Auxiliares.sleepRandom(6000);
			canales_consum[id].send(id);
//			System.out.println(id + ": " + dato);
		}
		
	}
	
	
	
	public static void main(String[] args) {
		Selector sel = new Selector();
		
		for(int i = 0; i < NUM_HILOS_POR_PROCESO; i++){
			canales_prod[i] = new Channel();
			sel.addSelectable(canales_prod[i], false);
		}
		for(int i = 0; i < NUM_HILOS_POR_PROCESO; i++){
			canales_consum[i] = new Channel();
			sel.addSelectable(canales_consum[i], false);
		}
		
		for(int i = 0; i < NUM_HILOS_POR_PROCESO; i++){
			final int id = i;
			Thread tprod = new Thread(new Runnable(){
				 @Override public void run(){
					productor(id);
				 }
			 });
			tprod.start();
			Thread tcons = new Thread(new Runnable(){
				@Override public void run(){
					consumidor(id);
				 }
			});
			tcons.start();
		}
		
		while(true){
			//se añaden las guardas a cada canal
			for(int i = 0; i < NUM_HILOS_POR_PROCESO; i++){
				canales_prod[i].setGuardValue(buffer.size() < MAX_OBJ);
				canales_consum[i].setGuardValue(!buffer.isEmpty());
			}
				/*
				 * Los canales de cada "Selector" se guardan desde 1 hasta N.
				 * Se han añadido los "Selectables", primero todos los de productores, y después todos
				 * los de consumidores, así:
				 * __________
				 * #1		Prod0
				 * #2		Prod1
				 * ...
				 * #NHP		ProdNHP-1
				 * #NHP+1	Cons 0
				 * ...
				 * _____________
				
				 */
				int sob = sel.selectOrBlock();
				if(sob >= 1 && sob <= NUM_HILOS_POR_PROCESO){
					int i = sob % NUM_HILOS_POR_PROCESO;
					Integer d = (Integer)canales_prod[i].receive();
					buffer.add(d);
					System.out.println("Productor " + i + ": He creado un " + d + ", y ahora el buffer mide " + buffer.size());
				}
				else{
					int j = sob % NUM_HILOS_POR_PROCESO;
					canales_consum[j].receive();
					Integer obj = buffer.remove(buffer.size()-1);
					System.out.println("Consumidor " + (j) +  ": me he comido un " + obj + ", ahora el buffer mide " + buffer.size());
				}
		}
		
		
	}
	
}
