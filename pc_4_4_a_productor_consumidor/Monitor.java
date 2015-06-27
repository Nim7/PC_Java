package pc_4_4_a_productor_consumidor;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import pc_0_aux.Auxiliares;
/*
 * monitor: aquí todo aquello que haya que proteger.
 * aquí, asegura q si el buffer lo usa el productor, el consumidor no puede
 * acceder al buffer hasta que el prod no diga que ha terminado
 * esto lo aseguramos con rentrantlocks y synchronized (en la cabecera del mét) 
 * en cada método del monitor.
 * 
 * Lleno = cerrojo.newCondition();
 * Vacio = " " " " " " " " "
 * 
 * Con synchronized, no hacen falta esas newconditions; simplemente se hace con un
 * if(nohayhuecos), wait()
 * 
 * mientras no queda hueco libre, lleno.await()
 * 
 * cuando termina de producir, Vacio.signal()
 */
public class Monitor {
	private static int MAX_OBJ = 30;
	private static volatile ArrayList<Integer> buffer = new ArrayList<Integer>();
	private static Lock rLock = new ReentrantLock();
	private static Condition lleno = rLock.newCondition();
	private static Condition vacio = rLock.newCondition();
	
	public Monitor(){
	}
	
	void productor() throws InterruptedException{
		try{
			rLock.lock();
			while(buffer.size() >= MAX_OBJ){
				System.out.println("Prod: no puedo producir más.");
				lleno.await();
			}
			Integer num = (int) Math.round(Math.random()*100);
			buffer.add(num);
			System.out.println("Prod: Inserto " + buffer.get(buffer.size()-1));
		}
		finally{
			vacio.signalAll();
			rLock.unlock();
		}

		
	}
	
	void consumidor() throws InterruptedException{
		try{
			rLock.lock();
			while(buffer.isEmpty() ){
				System.out.println("Consumidor: No puedo consumir más!");
				vacio.await();
			}
			System.out.println("Consumidor: " + buffer.get(buffer.size()-1) + ", qué rico!");
			buffer.remove(buffer.size()-1);
		}
		finally{
			lleno.signalAll();
			rLock.unlock();
		}

	}
	
	public String toString(){
		String aux = "[";
		for (int i = 0; i < buffer.size(); i++) {
			aux += buffer.get(i) + ", ";
		}
		aux += "]";
		return aux;
	}
	
}
