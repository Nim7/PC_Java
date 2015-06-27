package pc_0_aux;


public class Auxiliares {
	
	//Metodo printLn
	public static void println(String text){
		sleepRandom(10);
		System.out.println(text);
		sleepRandom(10);
	} 
	public static void sleepRandom(long ms){
		sleep((long) (Math.random() * ms));
	}
	public static void sleep(long ms){
		try {
			Thread.sleep(ms);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		} 
	}
}
