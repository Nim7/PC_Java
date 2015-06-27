package pc_4_1_mensajes;

public class Mensajes {
	private String[] cadenas = {"La vida es bella.", "... o no", "¡Los pajaritos cantan!", "... y molestan."};
	
	public void canta(){
		Thread currentThread = Thread.currentThread();
		String nombre = currentThread.getName();
		
		boolean interrupted = false;
		while (!interrupted){
			for (int i = 0; i < cadenas.length; i++) {
				System.out.println(nombre + ": " + cadenas[i]);
				try {
					Thread.sleep(400);
				} catch (InterruptedException e) {
					System.out.println("¡Se acabó!");
					return; //importante, o vuelve a empezar
				}
			}
			interrupted = currentThread.isInterrupted();
		}
		System.out.println(nombre + ": Ya estoy.");
	}
	
	
}
