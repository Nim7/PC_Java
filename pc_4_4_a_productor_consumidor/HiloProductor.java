package pc_4_4_a_productor_consumidor;

import pc_0_aux.Auxiliares;

public class HiloProductor extends Thread {
	private Monitor mon;
	
	public HiloProductor(Monitor m){
		mon = m;
	}
	
	public void run(){
		while(true){
			Auxiliares.sleepRandom(1000);
			try {
				this.mon.productor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
