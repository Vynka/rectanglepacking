package rpp;

import java.util.ArrayList;
import java.util.Random;

/**
 * Clase que contiene diversos metodos heuristicos (GRASP, busqueda de entornos,
 * enjambres, multiarranque...) para la resolucion del problema rectangle packing problem.
 * 
 * @author Alberto Cabrera Perez
 * @author Javier Luis Moreno Villena
 * @author Alejandro Tejera Perez
 * @author Isaac Galan Estarico
 * @version 1.01.02
 * @since 1.0
 */

public class Heuristica {
	/**
	 * Posibles espacios de entorno para soluciones vecinas.
	 */
	static final int NEAREST_NEIGHBOUR = 0;
	
	/**
	 * Criterios de parada
	 */
	static final int OUT_UNLESS_BETTER = 0;
	static final int NUMBER_OF_TIMES = 1;
	
	/**
	 * Tipos de muestreo del entorno
	 */
	static final int GREEDY_SAMPLING = 0;
	static final int ANXIOUS_SAMPLING = 1;
	static final int RANDOM_SAMPLING = 2;
	
	
	
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
	  initPoints();
	  this.problem = p;
	  evalue(p.getSolution());
	}			
	
	/**
	 * Metodo Heuristico de Busqueda por entorno numero 1. Busqueda Local.
	 */
	public int localSearch(Solution start, int neighbourType, int environmentType) {
	  /*
	  Solution actual = start;
	  Solution best = actual;
	  */
	  
	  return (new Integer(5)).intValue();
	}
	
	/**
	 * Metodo Heuristico de Busqueda por entorno numero 2. Aleatoria pura.
	 */
	public int pureRandomSearch(int n, int stop) {
		int times = n;
		Solution best = problem.getSolution().clone();
		Solution actual = best.clone();
		do {
			actual = new Solution (problem.getAreaRec(), Solution.RANDOM, problem.getRectangleSize());
			evalue(actual);	

			switch (stop) {
			case OUT_UNLESS_BETTER:
				if (best.getObjF() > actual.getObjF()) {
					best = actual;
					n = times;
				}
				break;
			case NUMBER_OF_TIMES:
				if (best.getObjF() > actual.getObjF())
					best = actual;
				break;
			}

			n--;
		} while (n > 0);
		problem.setSolution(best);
		return best.getObjF();
	}
	
	/**
	 * Metodo Heuristico de Busqueda por entorno numero 3. Recorrido al azar.
	 */
	public int randomSearch(int n, int neighbourType, int stop) {

		int times = n;
		Solution best = problem.getSolution().clone();
		Solution actual = best.clone();
		do {
			actual = neighbour(actual, neighbourType);
			evalue(actual);		

			switch (stop) {
			case OUT_UNLESS_BETTER:
				if (best.getObjF() > actual.getObjF()) {
					best = actual;
					n = times;
				}
				break;
			case NUMBER_OF_TIMES:
				if (best.getObjF() > actual.getObjF())
					best = actual;
				break;
			}

			n--;
		} while (n > 0);
		problem.setSolution(best);
		return best.getObjF();
	}	
	
	/**
	 * Metodo heuristico de Busqueda por entorno numero 4. Busqueda multiarranque.
	 */
	public int multistartSearch(int startType) {
	  return (new Integer(5)).intValue();
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
	private Rectangle getNewRectangle(int i, Solution s) {
	  return problem.getRectangle(s.getOrder(i));
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
	  int minorH = 0;
	  int minorB = 0;
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
	    if ((!modified) || (newH * newB) < (minorH * minorB)) {
          next = i;
          minorH = newH;
          minorB = newB;
          modified = true;
        }
	  }
	  // Se actualiza la solucion
	  if (modified) {
		  s.setBase(minorB);
		  s.setHeight(minorH);
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
			if ((points.get(j).getY() >= prevY) && (points.get(j).getY() < p.getY())) {
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
			if ((points.get(j).getX() >= prevX) && (points.get(j).getX() < p.getX())) {
				prevX = points.get(j).getX();
				i = j;
			}
		}
		p.setY(points.get(i).getY());
		return p;
	}
	
	
	/**
	 * Reestructura la lista de puntos, eliminando el usado, asi como los ocultados, y
	 * anadiendo los nuevos puntos creados con la eleccion tomada.
	 */
	private void managePoints(Rectangle r) {
		// Posible nuevo punto
		Point py;
		Point px;
		boolean newy = true;
		boolean newx = true;
		int i;
		// Se manipulan con respecto al eje Y
		py = obtainNewPointY(r);
		i = 0;
		while (i < points.size()) {
			// Se eliminan los puntos con coordenada Y menor que la actual
			// si estos estan a la izquierda de la base del rectangulo nuevo
			if ((points.get(i).getY() < py.getY()) &&
				(points.get(i).getX() < r.getPosition().getX())) {
				points.remove(i);
			} else if (points.get(i).getY() == py.getY()){
				newy = false;
				i++;
			} else {
				i++;
			}
		}
		// Se repite pero con respecto al eje X
		px = obtainNewPointX(r);
		i = 0;
		while (i < points.size()) {
			// Se eliminan los puntos con coordenada X menor que la actual
			// si estos estan por debajo de la altura del rectangulo nuevo
			if ((points.get(i).getX() < px.getX()) &&
				(points.get(i).getY() < r.getPosition().getY())) {
				points.remove(i);
			} else if (points.get(i).getX() == px.getX()){
				newx = false;
				i++;
			} else {
				i++;
			}
		}
		// Se a�aden si son puntos utiles
		if (newy)
			addPoint(py);
		if (newx)
			addPoint(px);
		// Por ultimo se elimina el punto utilizado de la lista
		points.remove(r.getPosition());
	}
	
	private void initPoints() {
		Point origen = new Point(0, 0);
		this.points.clear();
		this.points.add(origen);
	}
	
	/**
	 * Funcion que tiene como objetivo la colocacion de los rectangulos para hallar el valor
	 * que tiene la funcion objetivo dada una solucion representada con una permutacion de 
	 * rectangulos.
	 * @param s
	 *          Solucion a evaluar.
	 */
	private void evalue(Solution s) {
		initPoints(); // Inicializa los puntos (establece como unico punto el origen)
		Rectangle r;
		for (int i = 0; i < problem.getRectangleSize(); i++) {
			r = getNewRectangle(i, s);
			allocateRectangle(r, s);
			if (i != (problem.getRectangleSize() - 1))
				managePoints(r);
		}
		s.setObjF();
	}
	
	/**
	 * Intercambia el elemento i con el j en el array
	 * @param array
	 * 			array de elementos
	 * @param i
	 * 			una posicion del array
	 * @param j
	 * 			otra posicion del array
	 */
	private void swap(int[] array, int i, int j) {				
		int aux = array[i];
		array[i] = array[j];
		array[j] = aux;
 	}
	
	/**
	 * Halla una solucion vecina a partir de una dada.
	 * @param s
	 * 			solucion de la que se halla una vecina.
	 * @param neighbourType
	 * 			Constante que define la estructura de entorno
	 * @return Solucion vecina de s siguiendo algun criterio
	 */
	private Solution neighbour(Solution s, int neighbourType) {
		Random r = new Random(System.nanoTime());
		int [] newOrder = s.getOrder().clone();
		
		switch (neighbourType) {
			// NEAREST NEIGHBOUR son aquellas soluciones que se encuentran
		    // a un intercambio de elementos
			case NEAREST_NEIGHBOUR:
			int i = (int)(r.nextFloat() * newOrder.length);
			int j = (int)(r.nextFloat() * newOrder.length);
			while (i == j) {
				j = (int)(r.nextFloat() * newOrder.length);
			}
			swap(newOrder, i, j);
			break;
		}
		
		Solution toReturn = new Solution(0, 0, problem.getAreaRec(), newOrder);
		return toReturn;
	}
}
