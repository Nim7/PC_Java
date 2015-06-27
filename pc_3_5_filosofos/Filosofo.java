package pc_3_5_filosofos;
import java.util.concurrent.Semaphore;

import pc_0_aux.Auxiliares;


public class Filosofo implements Runnable {
	private Semaphore tenedorIzq, tenedorDer, hueco;
	private int id;
	
	public Filosofo(int i, Semaphore izq, Semaphore der, Semaphore h){
		id = i;
		tenedorIzq = izq;
		tenedorDer = der;
		hueco = h;
	}

	@Override
	public void run() {
		for (int i = 0; i < 3; i++) {
			System.out.println("Filósofo " + id + " pensando...");
			Auxiliares.sleepRandom(700);
			try{
				hueco.acquire();
				tenedorIzq.acquire();
				tenedorDer.acquire();
			}catch (Exception e){
				e.printStackTrace();
			}
			System.out.println("Filósofo " + id + " comiendo...");
			Auxiliares.sleepRandom(900);
			tenedorIzq.release();
			tenedorDer.release();
			hueco.release();
			System.out.println("Filósofo " + id + " se va.");
		}
		
	}
	

}
