package rpp;

import java.util.ArrayList;

/**
 * Clase que contiene diversos metodos heuristicos (GRASP, busqueda de entornos,
 * enjambres, multiarranque...) para la resolucion del problema rectangle packing problem.
 * 
 * @author Alberto Cabrera Perez
 * @author Javier Luis Moreno Villena
 * @author Alejandro Tejera Perez
 * @author Isaac Galan Estarico
 * @version 1.0a
 * @since 1.0
 */

public class Heuristica {
	/**
	 * Lista de puntos factibles en los que colocar un rectangulo
	 * Como punto inicial esta el (0, 0)
	 */
	private ArrayList<Point> points = new ArrayList<Point>(0);
	
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
	 *         indice del nuevo rectangulo de la solucion
	 * @return siguiente rectangulo a colocar
	 */
	private Rectangle getNewRectangle(int i) {
	  return problem.getRectangle(problem.getSolution().getOrden(i));
	}
	
	/**
	 * Busca un punto de la lista de puntos que minimize el criterio de colocacion de
	 * rectangulos, es decir, que al colocar el rectangulo seleccionado sea menor area
	 * desperdiciada.
	 * El critero para comparar es la relacion de unidades que aumenta el rectangulo de
	 * la funcion objetivo. Al ser un rectangulo es igual cual sea la direccion de crecimiento,
	 * por lo que se toma la suma de las dos unidades como medida de comparacion.
	 * El rectangulo ira colocado en aquel punto que haga que las dimensiones del rectangulo
	 * de la solucion crezcan lo menos posible.
	 * @param r
	 * 			rectangulo a asignar
	 * @param s
	 * 			solucion en la que va a ser asignado
	 */
	private void allocateRectangle(Rectangle r, Solution s) {
	  // El mejor caso es que las menores dimensiones sean las ya obtenidas
	  // anteriormente
	  int minorH = Integer.MAX_VALUE / 2;
	  int minorB = Integer.MAX_VALUE / 2;
	  boolean modified = false;
	  int next = 0;
	  for (int i = 0; i < points.size(); i++) {
		// Se toman los nuevos datos resultantes de colocar el rectangulo
		// Como minimo van a ser iguales que los actuales
	    int newH = s.getHeight();
	    int newB = s.getBase();
	    // Eje y
	    if ((points.get(i).getY() + r.getHeight()) > (s.getHeight()))
	      newH = points.get(i).getY() + r.getHeight();
	    // Eje X
	    if ((points.get(i).getX() + r.getBase()) > (s.getBase()))
	      newB = points.get(i).getX() + r.getBase();
	    // Si son mejores que los actuales se guardan
	    if ((newH + newB) < (minorH + minorB)) {
          next = i;
          minorH = newH;
          minorB = newB;
          modified = true;
        }
	  }
	  // Se actualiza la solucion
	  if (modified) {
		  s.setBase(menorB);
		  s.setHeight(menorH);
	  }
	  r.setPosition(this.points.get(next));
	}
	
	/**
	 * Obtiene las coordenadas donde iria el nuevo punto. Hay que tener en cuenta que la coordenada Y
	 * siempre sera la misma, mientras que la coordenada X sera igual a la del ultimo punto que tape. 
	 * @param r
	 * 			rectangulo colocado
	 * @return Candidato a nuevo punto del eje y
	 */
	private Point obtainNewPointY(Rectangle r) {
		int prevY = 0;
		int i = 0;
		Point p = new Point (0, r.getHeight() + r.getPosition().getY());
		for (int j = 0; j < points.size(); j++) {
			if ((points.get(j).getY() > prevY) && (points.get(j).getY() < p.getY())) {
				prevY = points.get(j).getY();
				i = j;
			}
		}
		p.setX(points.get(i).getX());
		return p;
	}
	
	/**
	 * Obtiene las coordenadas donde iria el nuevo punto. Hay que tener en cuenta que la coordenada Y
	 * siempre sera la misma, mientras que la coordenada X sera igual a la del ultimo punto que tape. 
	 * @param r
	 * 			rectangulo colocado
	 * @return Candidato a nuevo punto del eje y
	 */	
	private Point obtainNewPointX(Rectangle r) {
		int prevX = 0;
		int i = 0;
		Point p = new Point (r.getBase() + r.getPosition().getX(), 0);
		for (int j = 0; j < points.size(); j++) {
			if ((points.get(j).getX() > prevX) && (points.get(j).getX() < p.getX())) {
				prevX = points.get(j).getY();
				i = j;
			}
		}
		p.setX(points.get(i).getX());
		return p;
	}
	
	
	/**
	 * Reestructura la lista de puntos, eliminando el usado, asi como los ocultados, y
	 * anadiendo los nuevos puntos creados con la eleccion tomada.
	 */
	private void managePoints(Rectangle r) {
		// Posible nuevo punto
		Point p;
		boolean nuevo = true;
		// Se manipulan con respecto al eje Y
		p = obtainNewPointY(r);
		for (int i = 0; i < points.size(); i++) {
			// Se eliminan los puntos con coordenada Y menor que la actual
			// si estos estan a la izquierda de la base del rectangulo nuevo
			if ((points.get(i).getY() < p.getY()) &&
				(points.get(i).getX() < p.getX())) {
				points.remove(i);
			} else if (points.get(i).getY() == p.getY()){
				nuevo = false;
			}
		}
		if (nuevo)
			addPoint(p);
		// Se repite pero con respecto al eje X
		nuevo = false;
		p = obtainNewPointX(r);
		for (int i = 0; i < points.size(); i++) {
			// Se eliminan los puntos con coordenada X menor que la actual
			// si estos estan por debajo de la altura del rectangulo nuevo
			if ((points.get(i).getX() < p.getX()) &&
				(points.get(i).getY() < p.getY())) {
				points.remove(i);
			} else if (points.get(i).getX() == p.getX()){
				nuevo = false;
			}
		}
		if (nuevo)
			addPoint(p);
		// Por ultimo se elimina el punto utilizado de la lista
		points.remove(r.getPosition());
	}
	
	/**
	 * Funcion que tiene como objetivo la colocacion de los rectangulos para hallar el valor
	 * que tiene la funcion objetivo dada una solucion representada con una permutacion de 
	 * rectangulos.
	 * @param s
	 *          Solucion a evaluar.
	 */
	public void evalue(Solution s) {
		Rectangle r;
		for (int i = 0; i < problem.getRectangleSize(); i++) {
			r = getNewRectangle(i);
			allocateRectangle(r,s);
			// En la ultima iteracion es innecesario calcular los puntos
			if (i != (problem.getRectangleSize() - 1)) {
				managePoints(r);
			}
		}
	}
	
	/**
	 * Metodo Heuristico de Busqueda por entorno numero 1. Busqueda Local.
	 */
	public void localSearch() {
		
	}
	
	/**
	 * Metodo Heuristico de Busqueda por entorno numero 2. Aleatoria pura.
	 */
	public void pureRandomSearch() {

	}
	
	/**
	 * Metodo Heuristico de Busqueda por entorno numero 3. Recorrido al azar.
	 */
	public void randomSearch() {

	}
}
