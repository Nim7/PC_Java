package pc_3_2_productor_consumidor;

import java.util.concurrent.Semaphore;

import pc_0_aux.Auxiliares;

public class Proc_Consum {
	private static int MAX_OBJ = 30;
	private static volatile int buffer[] = new int[MAX_OBJ];
	private static int last_insert, last_consum = 0;
	private static Semaphore smp[] = new Semaphore[MAX_OBJ];
	
	public static void main(String[] args) {
		for(int i = 0; i < MAX_OBJ; i++){
			smp[i] = new Semaphore(1);
		}
		
		Thread tprod = new Thread(new Runnable(){
			 @Override public void run(){
				productor();
			 }
		 });
		Thread tcons = new Thread(new Runnable(){
			@Override public void run(){
				consumidor();
			 }
		});
		tprod.start();
		tcons.start();
	}
	 
	static void productor(){
		while(last_insert < MAX_OBJ){
			try {
				smp[last_insert].acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			buffer[last_insert] = (int) Math.round(Math.random()*last_insert + 6);
			System.out.println("*Prod*Inserto en la posición " + last_insert + " un " + buffer[last_insert]);
			Auxiliares.sleep(900);
			smp[last_insert].release();
			last_insert++;
		}
	}
	
	static void consumidor(){
		while(last_consum < MAX_OBJ){
			try {
				smp[last_consum].acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.err.println(buffer[last_consum] + ", ¡qué rico!");
			smp[last_consum].release();
			Auxiliares.sleep(400);
			last_consum++;
		}
	}
}

