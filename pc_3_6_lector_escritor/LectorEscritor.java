/*
 * Se dispone de una base de datos (simulada mediante el acceso a variables compartidas) 
que puede ser accedida por dos tipos de procesos: Escritor y Lector. El proceso Escritor se 
encarga de escribir en la base de datos (simulado mediante la escritura en un String). El 
lector accede a la base de datos (simulado mediante la muestra por pantalla de la variable 
compartida). Hay ciertos aspectos que debe tener en cuenta para la solución del problema: 
	• Cualquier número de lectores puede acceder a la BD, si no hay escritores 
	accediendo. 
	• El acceso a la BD de los escritores es exclusivo. Por lo tanto, mientras haya algún 
	lector leyendo, ningún escritor puede acceder a la BD, pero otros lectores sí. 
	• Puede haber varios escritores trabajando, aunque estos se deberán sincronizar para 
	que la escritura se lleve a cabo de uno en uno. 
	• Se da prioridad a los escritores. Ningún lector puede acceder a la BD cuando haya 
	escritores que desean hacerlo (Inanición de Lectores) 
Diseñar un programa concurrente correcto que resuelva el problema planteado.
 */

package pc_3_6_lector_escritor;
import java.util.concurrent.Semaphore;

import pc_0_aux.*;

public class LectorEscritor {
	//private static volatile ArrayList<String> bd = new ArrayList<>();
	private static int escritoresTrabajando = 0;
	private static int lectoresTrabajando = 0;
	private static int lectoresEspera=0;
	private static int escritoresEspera=0;
	private static Semaphore esperaFinEscritores = new Semaphore(1);
	private static Semaphore esperaFinLectores = new Semaphore(1);
	private static Semaphore emEscritura = new Semaphore(1);
	private static Semaphore emControl = new Semaphore(1);
	
	private static final int NUM_HILOS = 10;
	private static Thread[] hilos = new Thread[NUM_HILOS];


	public static void main(String[] args) {
		try {
			esperaFinLectores.acquire();
			esperaFinEscritores.acquire();
		} catch (InterruptedException e) {e.printStackTrace();}
		for(int i = 0; i < NUM_HILOS; i++){
			createThread("proceso", i);
		}
		startThreadsAndWait();
	}

	/*
	 * Cuando un lector intenta entrar en la BD, primero mira para ver si hay algún escritor 
	 * en ella (trabajando) o algún escritor a la espera de entrar. Si no hay ningún escritor 
	 * trabajando o a la espera, entonces entra el lector. En caso contrario, se bloquea a la 
	 * espera de que terminen los escritores y se aumenta el número de lectores esperando.
	 * Cuando un lector finaliza el acceso a la base de datos y es el último lector trabajando,
	 * desbloquea a los escritores que estuvieran esperando.
	 */
	public static void lector(){
            while(true){
		try {
			if(escritoresTrabajando>0 || escritoresEspera > 0){
				System.out.println(Thread.currentThread().getName() + ": Me espero para leer :(");
				emControl.acquire();
				lectoresEspera++;
				emControl.release();
								
				esperaFinEscritores.acquire();
				System.out.println(Thread.currentThread().getName() + ": MI TURNOOOO");
				
				emControl.acquire();
				lectoresEspera--;
				emControl.release();
				
				leer();
			}
			else{
				leer();
			}
		} catch (InterruptedException e) {e.printStackTrace();}
            }
	}
	
	public static void leer() throws InterruptedException{
		Auxiliares.sleepRandom(700);
                if(lectoresTrabajando==0){
                    esperaFinLectores.tryAcquire();
                }
                emControl.acquire();
                lectoresTrabajando++;
                emControl.release();
                System.out.println(Thread.currentThread().getName()  + ": Leo :)");
                Auxiliares.sleepRandom(700);
                emControl.acquire();
                lectoresTrabajando--;
                emControl.release();
                if(lectoresTrabajando==0){
				esperaFinLectores.release();
			}
	}
	
	/*
	 * Cuando un escritor intenta entrar en la BD, sólo mira por si hay un lector 
	 * en ella (trabajando), pero no mira si hay algún lector a la espera, porque los 
	 * escritores tienen prioridad sobre los lectores. Si no hay ningún lector trabajando,
	 * entonces entra el escritor. En caso contrario, se bloque a la espera de que 
	 * terminen los escritores.
	 * 
	 *  Cuando un escritor finaliza el acceso a la base de datos y es el último 
	 *  escritor trabajando, desbloquea a los lectores que estuvieran esperando.
	 */
	public static void escribir(){
            emControl.tryAcquire();
            escritoresTrabajando++;
            emControl.release();
            System.out.println(Thread.currentThread().getName() + ": Ejcribiendo.");
            Auxiliares.sleepRandom(1200);
            emControl.tryAcquire();
            escritoresTrabajando--;
            emControl.release();
            if(escritoresEspera==0){
                esperaFinEscritores.release();
            }
	}
	
	public static void escritor(){
            while(true){
                if(lectoresTrabajando > 0){
                    System.out.println(Thread.currentThread().getName() + ": Me espero para escribir...");
                    emControl.tryAcquire();
                    escritoresEspera++;
                    emControl.release();
                    
                    esperaFinLectores.tryAcquire();
                    System.out.println(Thread.currentThread().getName() + ": MI TURNOOOO, a escribir!");
                }
                if(escritoresTrabajando>0){
                    emControl.tryAcquire();
                    escritoresEspera++;
                    emControl.release();
                    
                    emEscritura.tryAcquire();
                    escribir();
                    emEscritura.release();
                    
                    emControl.tryAcquire();
                    escritoresEspera--;
				emControl.release();
				
			}
			else{
				emEscritura.tryAcquire();
				escribir();
				emEscritura.release();
			}
            }
        }
	
	
	//----
	 private static void createThread(final String s, final int i){
		 Thread th = new Thread(new Runnable(){
			 @Override public void run(){
				int random_number = (int) Math.round(Math.random()*i);
				if (random_number%2 == 0){
//					System.out.println("Lector creado!");
					lector();
				}
				else{
//					System.out.println("Escritor creado!");
					escritor();
				}
			 }
		 });
		 hilos[i] = th;
	 }
	 
	 private static void startThreadsAndWait(){
		 for (int i = 0; i < hilos.length; i++) {
			hilos[i].start();
			
		}
	 }

}
