package pc_4_4_b_productor_consumidor;

import pc_0_aux.Auxiliares;

public class Proc_Consum_Synch {
	
	public static void main(String[] args) {
		Monitor m = new Monitor();
		
		new HiloProductor(m).start();
		new HiloConsumidor(m).start();


		
		while(true){
//			System.out.println("Hola :) ");
			Auxiliares.sleep(500);
			System.out.println(m.toString());
		}
	}

}
