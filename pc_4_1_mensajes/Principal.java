package pc_4_1_mensajes;

public class Principal {

	public static void main(String[] args) {
		
		final Mensajes m = new Mensajes();
		
		Thread t = new Thread(new Runnable() {
			public void run() { 
				m.canta();
			}
		});
		t.start();
		int i = 0;
		while(t.isAlive() && i < 5) {
			System.out.println("Todavía esperando...");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			i++;
		}
		
		if(i == 5){
			System.out.println("ME HE CANSADO!");
			t.interrupt();
		}
		System.out.println("Por fin.");
		
	}

}
