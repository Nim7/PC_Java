package pc_3_5_filosofos;
import java.util.concurrent.Semaphore;


public class FilosofosSemaforos {
	public static final int NUMFILOSOFOS = 5;
	
	public static void main(String [] args){
		
		// Se crean los tenedores
		Semaphore [] tenedores = new Semaphore [NUMFILOSOFOS];
		for (int i = 0; i < NUMFILOSOFOS; i++) {
			tenedores[i] = new Semaphore(1);
		}
		//Y los huecos
		Semaphore[] huecos = new Semaphore[NUMFILOSOFOS-1];
		for (int i = 0; i < huecos.length; i++) {
			huecos[i] = new Semaphore(1);
		}
		
		//crear y lanzar los procesos
		Filosofo [] comensales = new Filosofo [NUMFILOSOFOS]; 
		for(int i=0; i<NUMFILOSOFOS; i++){
			//System.out.println("Creando... " + ((i+1) % (NUMFILOSOFOS-1)));
			comensales[i] = new Filosofo (i, tenedores[i], tenedores[(i+1) % NUMFILOSOFOS], huecos[(i+1) % (NUMFILOSOFOS-1)]);
		}
		Thread fil [] = new Thread [NUMFILOSOFOS]; 
		for(int i=0; i<NUMFILOSOFOS; i++){
			fil [i] = new Thread (comensales[i], "Hilo " + i);
			fil [i].start();
		}
	}
}
