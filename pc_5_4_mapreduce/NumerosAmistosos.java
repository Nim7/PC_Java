package pc_5_4_mapreduce;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * para cada no del rango hacer 
 * 		encontrar todos sus divisores
 * 		sumarlos -> suma
 * 		calcular el ratio -> suma/no
 * fpara
 * 
 * comparar entre sí todos los ratios, 
 * cuando exista coincidencia mostrar los números
 * 
 * La función map en un lenguaje funcional toma como entradas una función y una secuencia de valores. 
 * Su salida será una nueva secuencia resultado de aplicar la función a cada valor de la secuencia de
 * entrada.
 * La función reduce en un lenguaje funcional toma como entradas un operador binario y una secuencia
 * de valores. Su salida será el resultado de aplicar el operador binario para combinar los valores
 * de la secuencia de entrada.


 */
public class NumerosAmistosos{
	static Map<Integer, int[]> hm;
	
	private static List<Thread> threadsMap = new ArrayList<Thread>();
	private static List<Thread> threadsReduce = new ArrayList<Thread>();
	private static final int MAX_NUMBER = 250;
	
	public static void map(int max){
		for (int i = 1; i <= max; i++) {
			final int elem = i;
			Thread th = new Thread(new Runnable(){
				 @Override public void run(){
					int divisoresSumados = getSumaDeDivisores(elem);
					int[] fraccion = {divisoresSumados, elem};
					hm.put(elem, fraccion);
				 }
			 });
			threadsMap.add(th);
			th.start();
		}
		//Esperamos a que terminen todos los hilos
		for (Thread thread : threadsMap) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args) {
		hm = Collections.synchronizedMap(new HashMap<Integer, int[]>());
		
		map(MAX_NUMBER);
		
		for (int i = 1; i <= MAX_NUMBER ; i++) {
			final int elem = i;
			Thread th = new Thread(new Runnable(){
				 @Override public void run(){
					String aux = mutuamenteAmistosos(elem);
					System.out.println(aux);
				 }
			 });
			threadsReduce.add(th);
			th.start();
		}
		
	}
	
	private static String mutuamenteAmistosos(int elem){
		int[] estaFraccion = hm.get(elem);
		String aux = "";
		aux += "Son mutuamente amistosos con " + elem + ": ";
		for (int i = 1; i <= hm.size(); i++) 
			if(elem != i){
				int[] otraFraccion = hm.get(i);
				if(sonFraccionesEquivalentes(estaFraccion, otraFraccion)){
					aux += i + ", ";
			}
		}
		return aux;
	}
	
	private static boolean sonFraccionesEquivalentes(int[] a, int[]b){
		int num = a[0] * b[1];
		int den = a[1] * b[0];
		return (num == den);
		
	}
	
	private static int getSumaDeDivisores(int elem) {
		int suma = 0;
		for(int i = 1; i < elem; i++){
			if(elem%i == 0) //es divisor
				suma += i;
		}
		return suma;
	}
	
}
