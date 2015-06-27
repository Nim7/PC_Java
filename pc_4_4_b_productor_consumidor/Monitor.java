package pc_4_4_b_productor_consumidor;

import java.util.ArrayList;

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
	
	public Monitor(){
	}
	
	synchronized void productor() throws InterruptedException{
		while(buffer.size() >= MAX_OBJ){
			System.out.println("Prod: no puedo producir más.");
			this.wait();
		}
		Integer num = (int) Math.round(Math.random()*100);
		buffer.add(num);
		System.out.println("Prod: Inserto " + buffer.get(buffer.size()-1));
		this.notifyAll();
		
	}
	
	synchronized void consumidor() throws InterruptedException{
		while(buffer.isEmpty() ){
			System.out.println("Consumidor: No puedo consumir más!");
			this.wait();
		}
		System.out.println("Consumidor: " + buffer.get(buffer.size()-1) + ", qué rico!");
		buffer.remove(buffer.size()-1);
		this.notify();
	}
	
	public synchronized String toString(){
		String aux = "[";
		for (int i = 0; i < buffer.size(); i++) {
			aux += buffer.get(i) + ", ";
		}
		aux += "]";
		return aux;
	}
	
}
