package rpp;

import javax.swing.JFrame;

import GUI.MainFrame;

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
		MainFrame mf = new MainFrame();
		mf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mf.setVisible(true);	
	}
	
}
