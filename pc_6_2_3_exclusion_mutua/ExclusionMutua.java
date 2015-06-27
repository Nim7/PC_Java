package pc_6_2_3_exclusion_mutua;

import pc_0_aux.Auxiliares;
import messagepassing.MailBox;

/*
 * Resolver el problema de la exclusión mutua entre N procesos suponiendo las
 * siguientes restricciones: la comunicación en el buzón que controla la sección crítica es 
 * unidireccional y sólo se permiten relaciones muchos a uno. 
 * 
 * Sugerencia: Utilizar un buzón de entrada a la sección crítica y otro de salida. 
 * Implementar un proceso controlador que indique a través de estos buzones a qué 
 * proceso le toca entrar en la sección crítica. 
 * 
 * El proceso controlador vigila si los procesos que quieren entrar a la sección crítica 
 * pueden o no entrar. Para eso se tiene un buzón de entrada y otro de salida de la 
 * sección crítica en los que los procesos indican su intención de entrar en la sección 
 * crítica o su salida de la sección crítica. Cuando un proceso ha entrado, el controlador 
 * le da paso. Hasta que dicho proceso no indica en el buzón de salida que ha salido de 
 * la sección crítica el controlador no da paso a otro proceso a dicha sección. Por otra 
 * parte cada proceso tiene un buzón particular en el que el controlador dice a los 
 * procesos si pueden entrar o no en la sección crítica. 
 * 
 * Cuando un proceso quiere entrar en la sección crítica hace send al buzón de entrada. 
 * Cuando sale de la sección crítica hace send al buzón de salida de la sección crítica. El 
 * proceso controlador hace receive del buzón de entrada para saber si algún proceso 
 * pidió la SC. Si la SC está vacía le envía un mensaje al buzón particular de dicho 
 * proceso, y hace receive sobre el buzón de salida a la espera de que el proceso salga 
 * de la SC.
 */


public class ExclusionMutua {
	private static final int MAX_PROCESOS = 10;
	private static MailBox mb_entrada 	 = new MailBox(MAX_PROCESOS);
	private static MailBox mb_salida 	 = new MailBox(1);
	private static MailBox[] mb_privados = new MailBox[MAX_PROCESOS];
	
	private static void controlador(){
		while(true){
			int id_proceso = (int)mb_entrada.receive();
			mb_privados[id_proceso].send("");
			mb_salida.receive();
		}
	}
	
	private static void proceso(int id){
		while(true){
			Auxiliares.sleepRandom(500);
			System.out.println(id + ": ¡Quiero entrar!");
			mb_entrada.send(id);
			mb_privados[id].receive();
			//SC
			System.out.println(id + ": entra en la sección crítica.");
			Auxiliares.sleepRandom(1900);
			System.out.println(id + ": sale de la sección crítica.");
			//Fin SC
			
			mb_salida.send(id);
		}
		
	}
	
	public static void main(String[] args) {
		for(int i = 0; i < MAX_PROCESOS; i++){
			mb_privados[i] = new MailBox(1);
		}
		for(int i = 0; i < MAX_PROCESOS; i++){
			final int id = i;
			Thread th = new Thread(new Runnable(){
				 @Override public void run(){
					proceso(id);
				 }
			 });
			th.start();
		}
		
		Thread controlador = new Thread(new Runnable(){
			 @Override public void run(){
				controlador();
			 }
		 });
		controlador.start();
	}
	
}
