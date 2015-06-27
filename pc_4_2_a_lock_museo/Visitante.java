package pc_4_2_a_lock_museo;
import pc_0_aux.*;

public class Visitante {

	public void visita(String nombre){
		System.out.println(nombre + ": Hola :)");
		Auxiliares.sleepRandom(700);
		System.out.println(nombre + ": Qué bonito! Alucinaaaante!");
		Auxiliares.sleepRandom(700);
		System.out.println(nombre + ": Me voy a dar un paseo, ¡adiós!");
	}
}
