package rpp;

import java.io.*;

/**
 * Clase que tiene como objetivo la definición y resolución del problema conocido
 * como "Rectangle Packing Problem". El objetivo de dicho problema es empaquetar
 * un numero dado de rectángulos de dimensiones arbitrarias dentro de un
 * rectangulo, minimizando el area del envoltorio.
 * Para mas informacion: http://en.wikipedia.org/wiki/Packing_problem
 * 
 * @author Castor Miguel Perez Melian
 * @author Alberto Cabrera Perez
 * @author Javier Luis Moreno Villena
 * @author Alejandro Tejera Perez
 * @version 1.0a
 * @since 1.0
 */
public class Main {

	/**
	 * Metodo main Metodo para pruebas y utilizacion general.
	 * 
	 * @param args
	 *            Argumentos de la linea de comands
	 */
	public static void main(String[] args) {
		Problem r = new Problem("test.dat");
	}
}
