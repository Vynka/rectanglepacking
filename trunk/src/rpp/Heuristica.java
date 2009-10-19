package rpp;

import java.util.ArrayList;

/**
 * Clase que contiene diversos metodos heuristicos (GRASP, busqueda de entornos,
 * enjambres, multiarranque...) para la resolucion del problema rectangle packing problem.
 * 
 * @author Castor Miguel Perez Melian
 * @author Alberto Cabrera Perez
 * @author Javier Luis Moreno Villena
 * @author Alejandro Tejera Perez
 * @version 1.0a
 * @since 1.0
 */

public class Heuristica {
	/**
	 * Lista de puntos factibles en los que colocar un rectangulo
	 * Como punto inicial esta el (0, 0)
	 */
	private ArrayList<Point> points = new ArrayList<Point>(1);
	
	/**
	 * Solucion del problema
	 */
	private Solution solution;
	
	/**
	 * Rectangulos del problema.
	 */
	Rectangle [] rectangles;
	
	/**
	 * Constructor de la clase Heuristica.
	 * 
	 * @param rectangles
	 *         Rectangulos del problema inicial.
	 */
	public Heuristica(Rectangle [] rectangles) {
	  Point origen = new Point(0, 0);
	  this.points.add(origen);
	  this.rectangles = rectangles;
	}	
	
	/**
	 * @param i
	 *         indice del nuevo punto a devolver
	 * 
	 * @return siguiente punto de la lista de puntos factibles
	 */
	private Point getNewPoint(int i) {
	  return points.get(i);
	}
	
	/**
	 * @param i
	 *         indice del nuevo rectangulo
	 * @return siguiente rectangulo a colocar
	 */
	private Rectangle getNewRectangle(int i) {
	  //Mirar TODO para saber que es "rectangulos" (linea 45)
	  return rectangles[solution.getOrden(i)];
	}
	
	/**
	 * Busca un punto de la lista de puntos que minimize el criterio de colocacion de
	 * rectangulos, es decir, que al colocar el rectangulo seleccionado sea (menor area,
	 * menor numero de puntos ocultos,... por decidir)
	 */
	private void allocateRectangle() {
	  
	}
	
	/**
	 * Reestructura la lista de puntos, eliminando el usados, asi como los ocultados, y
	 * añadiendo los nuevos puntos creados con la eleccion tomada.
	 */
	private void managePoints() {
	  
	}
	
	/**
	 * Metodo Heuristico de Busqueda por entorno numero 1. Busqueda Local.
	 */
	public Solution busquedaLocal() {
		return solution;
	}
	
	/**
	 * Metodo Heuristico de Busqueda por entorno numero 2. Aleatoria pura.
	 */
	public Solution busquedaAP() {
		return solution;
	}
	
	/**
	 * Metodo Heuristico de Busqueda por entorno numero 3. Recorrido al azar.
	 */
	public Solution busquedaAlAzar(){
		return solution;
	}
}
