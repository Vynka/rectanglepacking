package rpp;

import java.util.ArrayList;

/**
 * Clase que contiene diversos metodos heuristicos (GRASP, busqueda de entornos,
 * enjambres, multiarranque...) para la resolucion del problema rectangle packing problem.
 * 
 * @author Alberto Cabrera Perez
 * @author Javier Luis Moreno Villena
 * @author Alejandro Tejera Perez
 * @author Isaac Galán Estárico
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
	 * Problema a resolver
	 */
	private Problem problem;
	
	/**
	 * Constructor de la clase Heuristica.
	 * 
	 * @param rectangles
	 *         Rectangulos del problema inicial.
	 */
	public Heuristica(Problem p) {
	  Point origen = new Point(0, 0);
	  this.points.clear();
	  this.points.add(origen);
	  this.problem = p;
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
	 * Anade un punto a la lista de puntos factibles.
	 * 
	 * @param p
	 *          Punto a anadir.
	 */
	private void addPoint(Point p) {
		points.add(p);
	}	
	
	/**
	 * @param i
	 *         indice del nuevo rectangulo
	 * @return siguiente rectangulo a colocar
	 */
	private Rectangle getNewRectangle(int i) {
	  return problem.getRectangle(solution.getOrden(i));
	}
	
	/**
	 * Busca un punto de la lista de puntos que minimize el criterio de colocacion de
	 * rectangulos, es decir, que al colocar el rectangulo seleccionado sea (menor area,
	 * menor numero de puntos ocultos,... por decidir)
	 */
	private void allocateRectangle(Rectangle rectangle) {
	  /*   En proceso.
	   *   OJO: PSEUDOCODIGO
	  ArrayList<int> foo = new ArrayList<int>(points.size());
	  for (int i = 0; i < points.size(); i++) {
	    int aux = 0;
	    if ((points[i].getY() + rectangle.getHeight()) > (problem.getSolution().getHeight()))
	      aux += rectangle.getHeight();
      if ((points[i].getX() + rectangle.getBase()) > (problem.getSolution().getBase()))
        aux += rectangle.getBase();
	    foo.push(aux);
	  }
	  */
	}
	
	/**
	 * Reestructura la lista de puntos, eliminando el usados, asi como los ocultados, y
	 * añadiendo los nuevos puntos creados con la eleccion tomada.
	 */
	private void managePoints() {
	  
	}
	
	/**
	 * Funcion que tiene como objetivo la colocacion de los rectangulos para hallar el valor
	 * que tiene la funcion objetivo dada una solucion representada con una permutacion de 
	 * rectangulos.
	 * @param s
	 *          Solucion a evaluar.
	 */
	private void evalue(Solution s) {
		
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
