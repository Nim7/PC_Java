package pc_6_3_3_letras;

import messagepassing.Channel;
import messagepassing.Selector;
import pc_0_aux.Auxiliares;

public class Letras {
	private static Channel c_a = new Channel();
	private static Channel c_b = new Channel();
	private static Channel c_c = new Channel();
	
	private static int num_a_printed = 0;
	private static int num_b_printed = 0;
	private static int num_c_printed = 0;
	private static boolean last_printed_was_b = false;
	private static boolean last_printed_was_c = false;

	private static void procA(){
		for(int i = 1; i <= 5; i++){
			Auxiliares.sleepRandom(1000);
			c_a.send("");
		}
	}
	
	private static void procB(){
		for(int i = 1; i <= 5; i++){
			Auxiliares.sleepRandom(1000);
			c_b.send("");
		}
	}
	
	private static void procC(){
		for(int i = 1; i <= 5; i++){
			Auxiliares.sleepRandom(1000);
			c_c.send("");
		}
	}
	
	
	public static void main(String[] args) {
		Selector sel = new Selector();
		sel.addSelectable(c_a, false);
		sel.addSelectable(c_b, false);
		sel.addSelectable(c_c, false);
		
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
	
		while(true){
			Auxiliares.sleepRandom(2000);
			c_a.setGuardValue(true);
			c_b.setGuardValue(num_b_printed <= num_a_printed && !last_printed_was_b);
			c_c.setGuardValue(num_c_printed <= num_a_printed && !last_printed_was_c);
			
			int sob = sel.selectOrBlock();
			
			switch (sob) {
				case 1: //proc a
					System.out.println("A");
					num_a_printed++;
					last_printed_was_b = false;
					last_printed_was_c = false;					
					break;
				
				case 2: //proc b
					c_c.receive();
					c_a.receive();
					System.out.println("B");
					num_b_printed++;
					last_printed_was_b = true;
					last_printed_was_c = false;
					break;

				case 3: //proc c
					c_b.receive();
					System.out.println("C");
					last_printed_was_b = false;
					last_printed_was_c = true;
					num_c_printed++;
					break;
				default: break;
			}
			
			
			
		}
	
	
	}
	
	
}
