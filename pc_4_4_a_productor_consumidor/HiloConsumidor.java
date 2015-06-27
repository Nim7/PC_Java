package pc_4_4_a_productor_consumidor;

import pc_0_aux.Auxiliares;

public class HiloConsumidor extends Thread {
	private Monitor mon;

	public HiloConsumidor(Monitor m){
		mon = m;
	}
	
	public void run(){
		while(true){
			Auxiliares.sleepRandom(2000);
			try{
				this.mon.consumidor();
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	
}
