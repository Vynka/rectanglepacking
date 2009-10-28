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
 * @version 1.0a
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
		Problem r = new Problem("test2.dat");
		System.out.println(r);
		Heuristica h = new Heuristica(r);
		System.out.println("La solucion inicial es -> " + r.getSolution());
		h.localSearch(r.getSolution(), Heuristica.ONE_SWAP_NEIGHBOUR, Heuristica.GREEDY_SAMPLING);
		System.out.println("La solucion del metodo LS con busqueda GREEDY es -> " + r.getSolution());
		/*h.pureRandomSearch(20, Heuristica.NUMBER_OF_TIMES);
		System.out.println("La solucion del metodo PSR es (n = 20) -> " + r.getSolution());
		h.randomSearch(20, Heuristica.NEAREST_NEIGHBOUR, Heuristica.NUMBER_OF_TIMES);
		System.out.println("La solucion del metodo RS es (n = 20) -> " + r.getSolution());
		MainFrame mf = new MainFrame();
		mf.setVisible(true);*/
	}
}
