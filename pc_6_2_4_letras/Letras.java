package pc_6_2_4_letras;

import pc_0_aux.Auxiliares;
import messagepassing.MailBox;

public class Letras {
	private static MailBox mb_a = new MailBox();
	private static MailBox mb_b = new MailBox();
	private static MailBox mb_c = new MailBox();

	private static void procA(){
		for(int i = 1; i <= 5; i++){
			Auxiliares.sleepRandom(1000);
			System.out.println("A");
			mb_a.send("");
		}
	}
	
	private static void procB(){
		for(int i = 1; i <= 5; i++){
			Auxiliares.sleepRandom(1200);
			mb_a.receive();
			System.out.println("B");
			mb_b.send("");
			mb_c.receive();
		}
	}
	
	private static void procC(){
		Auxiliares.sleepRandom(1000);
		for(int i = 1; i <= 5; i++){
			mb_b.receive();
			System.out.println("C");
			mb_c.send("");
		}
	}
	
	
	public static void main(String[] args) {
		Thread t = new Thread(new Runnable(){
			 @Override public void run(){
				procA();
			 }
		 });
		t.start();
		
		Thread t2 = new Thread(new Runnable(){
			 @Override public void run(){
				procB();
			 }
		 });
		t2.start();
		
		Thread t3 = new Thread(new Runnable(){
			 @Override public void run(){
				procC();
			 }
		 });
		t3.start();
	}
	
	
}
