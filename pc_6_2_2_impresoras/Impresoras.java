package pc_6_2_2_impresoras;

import pc_0_aux.Auxiliares;
import messagepassing.MailBox;

/*
 * Suponer n procesos que comparten tres impresoras. Cuando un proceso desea
 * imprimir, solicita una impresora. El sistema le contesta con el identificador de la 
 * impresora asignada. Cuando un proceso termina de imprimir libera la impresora que
 * se le asignó. Resolver el problema usando paso de mensajes asíncrono.
 */

public class Impresoras {
	private static final int MAX_IMPRESORAS = 3;
	private static final int MAX_PROCESOS = 10;
	private static MailBox mailbox = new MailBox(MAX_IMPRESORAS);
	
	public static void main(String[] args) {
		for(int i = 0; i < MAX_PROCESOS; i++){
			Thread th = new Thread(new Runnable(){
				 @Override public void run(){
					peticion();
				 }
			 });
			th.start();
		}
		
		for(int j = 0; j < MAX_IMPRESORAS; j++){
			final int id = j;
			Thread th = new Thread(new Runnable(){
				 @Override public void run(){
					impresion(id);
				 }
			 });
			th.start();
		}
		
	}

	protected static void impresion(int id) {
		while(true){
			Auxiliares.sleepRandom(5000);
			System.out.println("Impresora " + id + " ya :)");
			mailbox.send(id); //la impresora envía un mensaje con su ID cuando está disponible
		}
	}

	protected static void peticion() {
		while(true){
			Auxiliares.sleepRandom(8000);
			System.out.println(Thread.currentThread().getName() + ": ¡Quiero imprimir!");
			int id = (int) mailbox.receive();
			System.out.println(Thread.currentThread().getName() + ": Imprimo en la impresora " + id);
		}
	}
	

}
