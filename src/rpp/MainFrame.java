package rpp;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import java.awt.BorderLayout;

/**
 * Marco principal de la GUI, que servira como objeto de comunicacion entre la GUI y las clases para
 * la resolucion del problema, ademas de ser el contenedor principal y gestor de eventos.
 * 
 * @author Castor Miguel Perez Melian
 * @author Alberto Cabrera Perez
 * @author Javier Luis Moreno Villena
 * @author Alejandro Tejera Perez
 * @author Isaac Galan Estarico
 * @version 1.0
 * @since 1.0
 */
public class MainFrame extends JFrame {
	/**
	 * Atributo dp: Panel donde se representara la solucion. 
	 */
	private DrawPanel dp;

	/**
	 * Constructor por defecto, en el que se aniaden los objetos de la GUI y se le ajustan los parametros.
	 */
	public MainFrame() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(800, 600);
		
		JMenuBar mb = new JMenuBar();
		this.setJMenuBar(mb);
		JMenu mfile = new JMenu("File");
		mb.add(mfile);
		
		dp = new DrawPanel();
		this.add(dp, BorderLayout.CENTER);
		
		//Testeo
		Problem r = new Problem("test2.dat");
		System.out.println(r);
		Heuristica h = new Heuristica(r);
		System.out.println("La solucion inicial es -> " + r.getSolution());
		h.pureRandomSearch(20, Heuristica.NUMBER_OF_TIMES);
		System.out.println("La solucion del metodo PSR es (n = 20) -> " + r.getSolution());
		/*h.randomSearch(20, Heuristica.NEAREST_NEIGHBOUR, Heuristica.NUMBER_OF_TIMES);
		System.out.println("La solucion del metodo RS es (n = 20) -> " + r.getSolution());*/
		dp.setProblem(r);
	}
}