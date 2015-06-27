package pc_6_2_1_productor_consumidor;

import messagepassing.MailBox;
import pc_0_aux.Auxiliares;

public class Prod_Consum_Mailbox {

	private static final int MAX_OBJ = 30;
	private static final int NUM_PRODUCTORES = 1;
	private static final int NUM_CONSUMIDORES = 2;
	private static MailBox mailbox = new MailBox(MAX_OBJ);
	
	public static void main(String[] args) {
		for(int i = 0; i < NUM_PRODUCTORES; i++){
			Thread tprod = new Thread(new Runnable(){
				 @Override public void run(){
					productor();
				 }
			 });
			tprod.start();
		}
		for(int j = 0; j < NUM_CONSUMIDORES; j++){
			Thread tcons = new Thread(new Runnable(){
				@Override public void run(){
					consumidor();
				 }
			});
			tcons.start();
		}
	}
	 
	static void productor(){
		while(true){
			Auxiliares.sleepRandom(3500);
			int dato = (int) Math.round(Math.random()*MAX_OBJ + 6);
			mailbox.send(dato);
			System.out.println(Thread.currentThread().getName() + ": produzco " + dato);
		}
	}
	
	static void consumidor(){
		while(true){
			Auxiliares.sleepRandom(2000);
			int dato = (int) mailbox.receive();
			System.out.println(Thread.currentThread().getName() + ": " + dato + ", ¡ñom!");
		}
		
	}


}
