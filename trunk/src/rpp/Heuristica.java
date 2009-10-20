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
	 * rectangulos, es decir, que al colocar el rectangulo seleccionado sea menor area
	 * desperdiciada.
	 * El critero para comparar es la relacion de unidades que aumenta el rectangulo de
	 * la funcion objetivo. Al ser un rectangulo es igual cual sea la direccion de crecimiento,
	 * por lo que se toma la suma de las dos unidades como medida de comparación.
	 * El rectangulo ira colocado en aquel punto que haga que las dimensiones del rectangulo
	 * de la solucion crezcan lo menos posible.
	 */
	private void allocateRectangle(Rectangle rectangle) {
	  int menor = Integer.MAX_VALUE;
	  int fin = 0;
	  for (int i = 0; i < points.size(); i++) {
	    int nuevo = Integer.MAX_VALUE;
	    if ((this.points.get(i).getY() + rectangle.getHeight()) > (this.problem.getSolution().getHeight()))
	      nuevo += rectangle.getHeight();
      if ((this.points.get(i).getX() + rectangle.getBase()) > (this.problem.getSolution().getBase()))
        nuevo += rectangle.getBase();
      if (nuevo < menor) {
        fin = i;
        menor = nuevo;
      }
	  }
	  rectangle.setPosition(this.points.get(fin));
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
