package pc_4_5_descarga_cbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import pc_0_aux.Auxiliares;

public class MultipleDownloader {
	private static final int NFRAGM = 10;
	private static final int NFILES = 3;
	private static final int NTHREADS = 3;
	private static String[] file = new String[NFRAGM];
	private static int nextFragment = 0;
	private static Object nextFragmentLock = new Object(); //para actualizar el nextFragment
	//como el LOCK nos va a sincronizar el nextFragment, no necesitamos ponerle 'volatile'
	private static CyclicBarrier barrier;

	
//	private static String downloadData(int numFragm){
//		try {
//			Thread.sleep((long)(Math.random()*1000));
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return "Fragmento #"+ numFragm;
//	}
	
	public static void downloader(){
//		System.out.println("downloader");
		for(int i = 0; i < NFILES; i++){
			boolean finish = true;
			do{
				finish = downloadFragment();
			} while(finish);
			
			try {
				barrier.await();
			} catch (InterruptedException | BrokenBarrierException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static void fileRefresh(){
//		printFile();
		System.out.println("Fichero descargado con éxito.");
		nextFragment = 0;
		file = new String[NFRAGM]; //"SIN PROBLEMA! Es como se hace en Orientación a Objetos"
	}
	
	public static boolean downloadFragment(){
		int localFragment = 0;
		synchronized(nextFragmentLock){ //como nextFragment sólo se toca aquí, en el synchro, no hace falta que sea volatile :)
			if(nextFragment < NFRAGM){
				localFragment = nextFragment;
				nextFragment++;
			}
			else{
				return false;
			}
		}
//		System.out.println(name + ": Inicio de descarga del fragmento " + localFragment);
		Auxiliares.sleepRandom(4000);
		file[localFragment] = "# " + localFragment + "#"; //<-- parte chunga
//		System.out.println(name + ": fin de descarga del fragmento " + localFragment);
		//las partes chungas son las pesadas, las costosas, las que se tienen que ejecutar de forma concurrente
		printFile();
		return true;
	}
	
	private static void printFile(){
		System.out.println("------");
		System.out.print("File = [");
		for(int i = 0; i < NFRAGM; i++){
			System.out.print(file[i] + ", ");
		}
		System.out.println("]");
	}

	public static void main(String[] args) {
		System.out.println("Empezando descarga de " + NFILES + " ficheros.");

		barrier = new CyclicBarrier(NTHREADS, new Runnable(){
			public void run(){
				Auxiliares.sleep(500);
				fileRefresh();
			}
		});
		
		Thread[] thds = new Thread[NTHREADS];
		for(int i = 0; i < NTHREADS; i++){
			thds[i] = new Thread(new Runnable(){
				public void run(){
					downloader();
				}
			}, "Downloader" + i);
			thds[i].start();
		}

	}

}
