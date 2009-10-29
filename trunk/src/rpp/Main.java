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
		Problem r = new Problem("test2.dat");
		System.out.println(r);
		Heuristica h = new Heuristica(r);
		System.out.println("La solucion inicial es -> " + r.getSolution());
		h.localSearch(Heuristica.SWAP_WITH_LAST, Heuristica.GREEDY_SAMPLING, Solution.MIXED1);
		System.out.println("La solucion del metodo LS con busqueda GREDDY es -> " + r.getSolution());
		h.pureRandomSearch(1000, Heuristica.NUMBER_OF_TIMES);
		System.out.println("La solucion del metodo PRS es -> " + r.getSolution());
		/*
		MainFrame mf = new MainFrame();
		mf.setVisible(true);
		*/
	}
}
