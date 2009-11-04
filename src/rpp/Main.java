package rpp;

/**
 * Clase que tiene como objetivo la definicion y resolucion del problema conocido
 * como "Rectangle Packing Problem". El objetivo de dicho problema es empaquetar
 * un numero dado de rectangulos de dimensiones arbitrarias dentro de un
 * rectangulo, minimizando el area del envoltorio.
 * Para mas informacion: http://en.wikipedia.org/wiki/Packing_problem
 * 
 * @author Castor Miguel Perez Melian
 * @author Alberto Cabrera Perez
 * @author Javier Luis Moreno Villena
 * @author Alejandro Tejera Perez
 * @author Isaac Galan Estarico
 * @version 1.01.03
 * @since 1.0
 */
public class Main {

	/**
	 * Metodo main para pruebas y utilizacion general.
	 * 
	 * @param args
	 *            Argumentos de la linea de comandos
	 */
	public static void main(String[] args) {
		/*Problem r = new Problem("test.dat");
		System.out.println(r);
		Heuristica h = new Heuristica(r);
		System.out.println("La solucion inicial es -> " + r.getSolution());
		h.multistartSearch(5, Heuristica.SWAP_WITH_LAST, Heuristica.ANXIOUS_SAMPLING, Solution.RANDOM, Heuristica.OUT_UNLESS_BETTER);
		System.out.println("La solucion del metodo MSS con busqueda ANXIOUS es -> " + r.getSolution());
		h.pureRandomSearch(50, Heuristica.NUMBER_OF_TIMES);
		System.out.println("La solucion del metodo PRS es -> " + r.getSolution());
		h.localSearch(Heuristica.ONE_SWAP, Heuristica.GREEDY_SAMPLING, Solution.RANDOM);
		System.out.println("La solucion del metodo LS con muestreo GREEDY es -> " + r.getSolution());*/
		MainFrame mf = new MainFrame();
		mf.setVisible(true);
	}
	
}
