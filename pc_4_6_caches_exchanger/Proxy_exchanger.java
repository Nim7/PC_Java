package pc_4_6_caches_exchanger;

import java.util.HashMap;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Semaphore;
import pc_0_aux.Auxiliares;

public class Proxy_exchanger {	    
	    static Exchanger<String> exchanger = new Exchanger<>();
	    static HashMap<Integer, String> cache = new HashMap<Integer, String>();
	    static final int NUM_PETICIONES = 10;
	    static Semaphore pedir = new Semaphore(1);
            
    public static void pedirAlServidor(int i) throws InterruptedException{
    	String nuevoRecurso = "rec_" + i + ".html";
    	cache.put(i, nuevoRecurso);
    	exchanger.exchange(nuevoRecurso);
    }
    
    public static void solicitar(int j) throws InterruptedException{
    	String name = Thread.currentThread().getName();
        final Integer num = (int) Math.round(Math.random()*j);
        pedir.acquire();
        boolean existeRecurso = cache.containsKey(num);
        pedir.release();
        if(existeRecurso){ //devuelve de la caché
        	Auxiliares.sleepRandom(1000);
        	String aux = (name + ": Recurso " + num + " -> " + cache.get(num));
        	System.out.println(aux);
        }
        else{ //pedirlo al servidor, guardar en caché, devolver
        	pedir.acquire();
        	System.out.println(name + ": oops, no tengo el recurso " + num + ". Espera que pido.");
        	Auxiliares.sleepRandom(4000);
        	crearHiloPeticion(num);
        	String recurso = exchanger.exchange(null);
        	String aux = (name + ": Recurso nuevo -> " + recurso);
        	System.out.println(aux);
        	pedir.release();
        }
        System.out.println(cache.toString());

    }
    
    public static void crearHiloPeticion(final int j){
    	 Thread peticionServidor = new Thread(new Runnable(){
             @Override
             public void run() {
                 try {
 					pedirAlServidor(j);
 				} catch (InterruptedException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
             }
         },"Petición al servidor ");
     	peticionServidor.start();
    }
    
    public static void main(String[] args) {
    	//Rellenamos la 'caché' con contenido de prueba
    	cache.put(1, "música.mp3");
    	cache.put(2, "imagen.jpg");
    	cache.put(5, "documento.pdf");
    	
        for (int i = 0; i < NUM_PETICIONES; i++) {
        	final int j = i;
        	Thread th = new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
						solicitar(j);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            },"Cliente " + i);
        	th.start();
		}
    }

}
