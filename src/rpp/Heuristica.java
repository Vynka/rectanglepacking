package rpp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Clase que contiene diversos metodos heuristicos (GRASP, busqueda de entornos,
 * enjambres, multiarranque...) para la resolucion del problema rectangle
 * packing problem.
 * 
 * @author Alberto Cabrera Perez
 * @author Javier Luis Moreno Villena
 * @author Alejandro Tejera Perez
 * @author Isaac Galan Estarico
 * @version 1.01.05
 * @since 1.0
 */

public class Heuristica {
	/**
	 * Posibles espacios de entorno para soluciones vecinas.
	 */
	static final int RANDOM_SWAP = 0;
	static final int ONE_SWAP = 1;
	static final int SWAP_WITH_LAST = 2;

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
	static final int NO_SAMPLING = 3;

	/**
	 * Metodos de resolucion del problema
	 */
	public static final int PURE_RANDOM_SEARCH = 0;
	public static final int RANDOM_SEARCH = 1;
	public static final int LOCAL_SEARCH = 2;
	public static final int MULTISTART_WITH_LOCAL_SEARCH = 3;
	public static final int SIMULATED_ANNEALING_SEARCH = 4;

	/**
	 * Eleccion de heuristica de colocacion
	 */
	static final int WASTE = 0;
	static final int AREA = 1;
	
	/**
	 * Lista de puntos factibles en los que colocar un rectangulo Como punto
	 * inicial esta el (0, 0)
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
	 *            Rectangulos del problema inicial.
	 */
	public Heuristica(Problem p) {
		initPoints();
		this.problem = p;
		wasteEvalue(p.getSolution());
	}

	/**
	 * Se usa para la eleccion del metodo a utilizar en la GUI
	 * @param hop
	 * 			Opciones con las que se ejecuta la heuristica
	 * @return Si las opciones son correctas.
	 */
	public boolean callProcedure(HeuristicOptions hop) {
		switch (hop.getProcedure()) {
		case PURE_RANDOM_SEARCH:
			pureRandomSearch(hop.getTimes(), hop.getStopCriteria(), hop.getEvaluationMode());
			break;
		case RANDOM_SEARCH:
			randomSearch(hop.getTimes(), hop.getStopCriteria(), hop.getEvaluationMode());
			break;
		case LOCAL_SEARCH:
			localSearch(hop.getNeighbors(), hop.getSampling(), hop.getEvaluationMode());
			break;
		case MULTISTART_WITH_LOCAL_SEARCH:
			multistartSearch(hop.getTimes(), hop.getNeighbors(), hop.getSampling(),
							 hop.getInitialization(), hop.getStopCriteria(), hop.getEvaluationMode());
			break;
		case SIMULATED_ANNEALING_SEARCH:
			simulatedAnnealingSearch(hop.getInitialization(), hop.getNeighbors(), hop.getSampling(), 0.95, 1.05, hop.getEvaluationMode());
			break;
		default:
			return false;
		}
		return true;
	}
	
	/**
	 * Metodo Heuristico de Busqueda por entorno numero 1. Busqueda Local.
	 * 
	 * Este metodo Heuristico implementa una busqueda de minimos relativos. Es
	 * decir, genera una solucion inicial y busca entre sus vecinas (entorno)
	 * alguna que mejore la funcion objetivo. En caso afirmativo se repite el
	 * proceso en dicha solucion vecina. En caso negativo se considera la
	 * solucion actual un minimo relativo (porque no se puede saber si es
	 * absoluto) y se termina la ejecucion.
	 * 
	 * @param neighbourType
	 *            tipo de estructura de entorno, puede ser RANDOM_SWAP, ONE_SWAP
	 *            o SWAP_WITH_LAST (de momento) ATENCION: El tipo de estructura
	 *            aleatoria solo se puede usar con el muestreo aleatorio
	 * @param sampleType
	 *            tipo de comportamiento de la busqueda local (o tipo de
	 *            muestreo) que puede ser GREEDY_SAMPLING si se escoje el mejor
	 *            vecino de todo el entorno, ANXIOUS_SAMPLING si se coje el
	 *            primer mejor vecino, o RANDOM_SAMPLING si se coje el primer
	 *            vecino al azar que mejore
	 */
	public Solution localSearch(int neighbourType, int sampleType, int evaluationMode) {
		Solution actual = problem.getSolution().clone();		
		Solution best = actual.clone();
		Point[] bestPos = problem.getActualPositions();
		boolean betterFound;
		do {
			betterFound = false;
			actual = neighbour(actual, neighbourType, sampleType, evaluationMode);
			Point[] actPos;
			
			if (evaluationMode == WASTE)
				actPos = wasteEvalue(actual);
			else actPos = areaEvalue(actual);
			
			if (best.getObjF() > actual.getObjF()) {
				best = actual.clone();
				bestPos = actPos.clone();
				betterFound = true;
			}
		} while (betterFound);
		
		problem.setSolution(best);		
		problem.changeRectanglePositions(bestPos);
		return best.clone();
	}

	/**
	 * Metodo Heuristico de Busqueda por entorno numero 2. Aleatoria pura.
	 * 
	 * Este metodo Heuristico implementa una busqueda aleatoria en todo el
	 * conjunto de soluciones posibles del problema. Para ello genera soluciones
	 * aleatorias, y mantiene la mejor de ellas, hasta cumplir una codicion de
	 * parada.
	 * 
	 * @param n
	 *            numero de iteraciones a realizar
	 * @param stop
	 *            condicion de parada, puede ser OUT_UNLESS_BETTER si se quiere
	 *            terminar la ejecucion al cumpli una serie de iteraciones sin
	 *            encontrar una solucion que mejore, o NUMBER_OF_TIMES, si se
	 *            quiere terminar la ejecucion en el numero exacto de
	 *            iteraciones especificadas.
	 */
	public Solution pureRandomSearch(int times, int stop, int evaluationMode) {
		int n = times;
		Solution best = problem.getSolution().clone();
		Point[] bestPos = problem.getActualPositions();
		Solution actual = best.clone();
		do {
			actual = new Solution(problem.getAreaRec(), Solution.RANDOM,
								  problem.getRectangleSize());
			
			Point[] actPos;
			if (evaluationMode == WASTE)
				actPos = wasteEvalue(actual);
			else actPos = areaEvalue(actual);

			switch (stop) {
			case OUT_UNLESS_BETTER:
				if (best.getObjF() > actual.getObjF()) {
					best = actual.clone();
					bestPos = actPos.clone();
					n = times;
				}
				break;
			case NUMBER_OF_TIMES:
				if (best.getObjF() > actual.getObjF()) {
					best = actual.clone();
					bestPos = actPos.clone();
				}
				break;
			}

			n--;
		} while (n > 0);
		problem.setSolution(best);
		problem.changeRectanglePositions(bestPos);
		return best.clone();
	}

	/**
	 * Metodo Heuristico de Busqueda por entorno numero 3. Recorrido al azar.
	 * 
	 * Este metodo Heuristico implementa una busqueda aleatoria en todo el
	 * conjunto de soluciones posibles del problema, pero dentro de las
	 * soluciones vecinas a una solucion dada. Para ello genera una solucion
	 * inicial, y obtiene una solucion aleatoria vecina de esta. Si dicha
	 * solucion vecina mejora la actual, se toma como actual y se repite el
	 * proceso. En caso contrario se genera una nueva. Continua hasta que se
	 * cumpla la condicion de parada.
	 * 
	 * @param n
	 *            numero de iteraciones
	 * @param stop
	 *            condicion de parada, puede ser OUT_UNLESS_BETTER si se quiere
	 *            terminar la ejecucion al cumpli una serie de iteraciones sin
	 *            encontrar una solucion que mejore, o NUMBER_OF_TIMES, si se
	 *            quiere terminar la ejecucion en el numero exacto de
	 *            iteraciones especificadas.
	 */
	public Solution randomSearch(int times, int stop, int evaluationMode) {
		int n = times;
		Solution best = problem.getSolution().clone();
		Point[] bestPos = problem.getActualPositions();
		Solution actual = best.clone();
		do {
			actual = neighbour(actual, RANDOM_SWAP, NO_SAMPLING, 0);
			Point[] actPos;
			
			if (evaluationMode == WASTE)
				actPos = wasteEvalue(actual);
			else actPos = areaEvalue(actual);

			switch (stop) {
			case OUT_UNLESS_BETTER:
				if (best.getObjF() > actual.getObjF()) {
					best = actual.clone();
					bestPos = actPos.clone();
					n = times;
				}
				break;
			case NUMBER_OF_TIMES:
				if (best.getObjF() > actual.getObjF()) {
					best = actual.clone();
					bestPos = actPos.clone();
				}
				break;
			}

			n--;
		} while (n > 0);
		problem.setSolution(best);
		problem.changeRectanglePositions(bestPos);
		return best.clone();
	}

	/**
	 * Metodo heuristico de Busqueda por entorno numero 4. Busqueda
	 * multiarranque.
	 * 
	 * Este metodo Heuristico se desarrolla de la siguiente manera. Se generan
	 * tantas soluciones iniciales como las especificadas y de la forma
	 * especificada, y se lanzan busquedas locales (Busquedas por entorno numero
	 * 1) desde ellas con el fin de evitar obtener solo soluciones relativas. El
	 * metodo escoje la mejor solucion de las devueltas por las busquedas
	 * locales y la toma como solucion optima.
	 * 
	 * @param n
	 *            numero de iteraciones
	 * @param neighbourType
	 *            tipo de estructura de entorno, puede ser RANDOM_SWAP, ONE_SWAP
	 *            o SWAP_WITH_LAST (de momento) ATENCION: El tipo de estructura
	 *            aleatoria solo se puede usar con el muestreo aleatorio
	 * @param sampleType
	 *            tipo de comportamiento de la busqueda local (o tipo de
	 *            muestreo) que puede ser GREEDY_SAMPLING si se escoje el mejor
	 *            vecino de todo el entorno, ANXIOUS_SAMPLING si se coje el
	 *            primer mejor vecino, o RANDOM_SAMPLING si se coje el primer
	 *            vecino al azar que mejore
	 * @param initType
	 *            tipo de generacion de solucion inicial, puede ser RANDOM,
	 *            DETERMINISTICINIT1, DETERMINISTICINIT2, MIXEDINIT1,
	 *            MIXEDINIT2,...
	 * @see Solution
	 * @param stop
	 *            condicion de parada, puede ser OUT_UNLESS_BETTER si se quiere
	 *            terminar la ejecucion al cumpli una serie de iteraciones sin
	 *            encontrar una solucion que mejore, o NUMBER_OF_TIMES, si se
	 *            quiere terminar la ejecucion en el numero exacto de
	 *            iteraciones especificadas.
	 */
	public Solution multistartSearch(int times, int neighbourType, int sampleType, int initType, int stop, int evaluationMode) {
		int n = times;
		Point[] bestPos = problem.getActualPositions();
		Solution actual, nueva;
		Solution best = problem.getSolution().clone();
		do {
			actual = localSearch(neighbourType, sampleType, evaluationMode);
			Point[] actPos;
			if (evaluationMode == WASTE)
				actPos = wasteEvalue(actual);
			else actPos = areaEvalue(actual);
			switch (stop) {
			case OUT_UNLESS_BETTER:
				if (best.getObjF() > actual.getObjF()) {
					best = actual.clone();
					bestPos = actPos.clone();
					n = times;
				}
				break;
			case NUMBER_OF_TIMES:
				if (best.getObjF() > actual.getObjF()) {
					best = actual.clone();
					bestPos = actPos.clone();
				}
				break;
			}
			int sameId = 0;
			do {
				nueva = new Solution(problem.getAreaRec(), initType, problem.getRectangleSize());
				Point[] nuevaPos = areaEvalue(nueva);
				for (int i = 0; i < problem.getRectangleSize(); i++)
					if (nuevaPos[i] == actPos[i])
						sameId++;
			} while (sameId > (problem.getRectangleSize()/2));
			problem.setSolution(nueva);
			n--;
		} while (n > 0);
		problem.setSolution(best);
		problem.changeRectanglePositions(bestPos);
		return best.clone();
	}

	/**
	 * Metodo heuristico de Busqueda por entorno numero 5. Busqueda por Recocido
	 * Simulado.
	 * 
	 * Este metodo de busqueda simula el recocido que se realiza para dar forma
	 * a ciertos metales. El procedimiento es simple: Se calienta y enfria el metal
	 * de froma continuada de forma que los enlaces de los atomos se hagan fuertes.
	 * La simulacion pretende demostrar que una solucion peor que la encontrada en
	 * busqueda puede llevar a una solucion mejor. Dicho "puede" equivale a una
	 * probabilidad. Por ello, se realiza una busqueda local y se acepta la solucion
	 * vecina si es mejor o si tiene probabilidad de mejorar la solucion actual.
	 * 
	 * La temperatura "c" es un parametro de control que influye directamente en la
	 * probabilidad de aceptacion. A mayor numero de iteraciones, menor probabilidad,
	 * por lo que la temperatura es menor.
	 * 
	 * El tamanio "L" es el numero de soluciones que se generaran en la iteracion
	 * actual. Dado que la probabilidad de aceptar una solucion como valida cada
	 * vez es menor, el numero de soluciones a generar cada vez es mayor.
	 * 
	 * @param initType
	 * 				tipo de inicializacion del problema entre:
	 *        RANDOM, DETERMINISTICINIT1, DETERMINISTICINIT2,
	 *        MIXEDINIT1, MIXEDINIT2,...
	 *        
	 * @param neighbourType
	 *        tipo de estructura de entorno, puede ser RANDOM_SWAP, ONE_SWAP
	 *        o SWAP_WITH_LAST (de momento) ATENCION: El tipo de estructura
	 *        aleatoria solo se puede usar con el muestreo aleatorio
	 *            
	 * @param sampleType
	 *        tipo de comportamiento de la busqueda local (o tipo de
	 *        muestreo) que puede ser GREEDY_SAMPLING si se escoje el mejor
	 *        vecino de todo el entorno, ANXIOUS_SAMPLING si se coje el
	 *        primer mejor vecino, o RANDOM_SAMPLING si se coje el primer
	 *        vecino al azar que mejore
	 */
	public Solution simulatedAnnealingSearch(int initType, int neighbourType,
											 int sampleType, double coolingFactor, double iterationIncrement,
											 int evaluationMode) {
		
		Solution actual;
		Random r = new Random(System.nanoTime());
		actual = new Solution(problem.getAreaRec(), initType, problem.getRectangleSize());
		
		int c = (int) Math.sqrt(problem.getRectangleSize());
		int L = (int) Math.pow(problem.getRectangleSize(), 2);
		int k = 0;                // Numero de iteraciones hechas
		
		Point[] bestPos = problem.getActualPositions();
		if (evaluationMode == WASTE)
			bestPos = wasteEvalue(actual);
		else bestPos = areaEvalue(actual);
		
		Solution best = actual.clone();
		do {
			for (int i = 0; i < L; i++) {
				actual = neighbour(actual, neighbourType, sampleType, evaluationMode);
				Point[] actPos;
				
				if (evaluationMode == WASTE)
					actPos = wasteEvalue(actual);
				else actPos = areaEvalue(actual);
				
				if (best.getObjF() > actual.getObjF()) {
					best = actual.clone();
					bestPos = actPos.clone();
				} else if (Math.exp((actual.getObjF() - best.getObjF()) / c) > r.nextFloat()) {
					best = actual.clone();
					bestPos = actPos.clone();
				}
			}
			k++;
			c = CalculateTemperature(c, coolingFactor);
			CalculateSize(L, k, iterationIncrement);
		} while (c > 0);
		problem.setSolution(best);
		problem.changeRectanglePositions(bestPos);
		return best.clone();
	}

	/**
	 * Genera una solucion de manera constructiva.
	 */
	public Solution GRASP1() {
		return null;
	}

	/**
	 * Genera una solucion de manera constructiva.
	 */
	public Solution GRASP2() {
		return null;
	}

	/**
	 * Genera una solucion de manera constructiva.
	 */
	public Solution GRASP3() {
		return null;
	}

	/**
	 * Calcula la temperatura del metodo de busqueda SimulatedAnnealing.
	 * 
	 * @param c
	 *            temperatura anterior
	 * @param k
	 *            Numero de iteraciones hechas
	 */
	private int CalculateTemperature(int c, double coolingFactor) {
		return (int)(c * coolingFactor);
	}

	/**
	 * Calcula el numero de iteraciones del metodo de busqueda SimulatedAnneaing
	 * para la iteracion k.
	 * 
	 * @param L
	 *            Numero de iteraciones anterior
	 * @param k
	 *            Numero de iteraciones hechas
	 */
	private int CalculateSize(int L, int k, double iterationIncrement) {
		return (int)(k * iterationIncrement);
	}

	/**
	 * Anade un punto a la lista de puntos factibles.
	 * 
	 * @param p
	 *            Punto a anadir.
	 */
	private void addPoint(Point p) {
		points.add(p);
	}

	/**
	 * @param i
	 *            indice del nuevo rectangulo de la solucion
	 * @return siguiente rectangulo a colocar
	 */
	private Rectangle getNewRectangle(int i, Solution s) {
		return problem.getRectangle(s.getOrder(i));
	}

	/**
	 * Reestructura la lista de puntos, eliminando el usado, asi como los
	 * ocultados, y anadiendo los nuevos puntos creados con la eleccion tomada.
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
			if ((points.get(i).getY() < py.getY())
					&& (points.get(i).getX() < r.getPosition().getX())) {
				points.remove(i);
			} else if (points.get(i).getY() == py.getY()) {
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
			if ((points.get(i).getX() < px.getX())
					&& (points.get(i).getY() < r.getPosition().getY())) {
				points.remove(i);
			} else if (points.get(i).getX() == px.getX()) {
				newx = false;
				i++;
			} else {
				i++;
			}
		}
		// Se aniaden si son puntos utiles
		if (newy)
			addPoint(py);
		if (newx)
			addPoint(px);
		// Por ultimo se elimina el punto utilizado de la lista
		points.remove(r.getPosition());
	}

	/**
	 * Busca un punto de la lista de puntos que minimize el criterio de
	 * colocacion de rectangulos, es decir, que al colocar el rectangulo
	 * seleccionado sea menor area desperdiciada. El critero para comparar es la
	 * relacion de unidades que aumenta el rectangulo de la funcion objetivo. Al
	 * ser un rectangulo es igual cual sea la direccion de crecimiento, por lo
	 * que se toma la suma de las dos unidades como medida de comparacion. El
	 * rectangulo ira colocado en aquel punto que haga que las dimensiones del
	 * rectangulo de la solucion crezcan lo menos posible.
	 * 
	 * @param r
	 *            rectangulo a asignar
	 * @param s
	 *            solucion en la que va a ser asignado
	 * @return el punto donde ira colocado el rectangulo.
	 */
	private Point areaAllocateRectangle(Rectangle r, Solution s) {
		// El mejor caso es que las menores dimensiones sean las ya obtenidas
		// anteriormente
		int minorH = 0;
		int minorB = 0;
		boolean modified = false;
		int selected = 0;
		// Se toman los nuevos datos resultantes de colocar el rectangulo
		// Como minimo van a ser iguales que los actuales
		for (int i = 0; i < points.size(); i++) {
			// Eje y
			int newH = s.getHeight();
			if ((points.get(i).getY() + r.getHeight()) > (s.getHeight()))
				newH = points.get(i).getY() + r.getHeight();
			// Eje X
			int newB = s.getBase();
			if ((points.get(i).getX() + r.getBase()) > (s.getBase()))
				newB = points.get(i).getX() + r.getBase();
			// Si son mejores que los actuales se guardan
			if ((!modified) || (newH * newB) < (minorH * minorB)) {
				selected = i;
				minorH = newH;
				minorB = newB;
				modified = true;
			}
		}
		// Se actualiza la solucion
		s.setBase(minorB);
		s.setHeight(minorH);
		r.setPosition(this.points.get(selected));
		return r.getPosition();
	}

	/**
	 * Obtiene las coordenadas donde iria el nuevo punto. Hay que tener en
	 * cuenta que la coordenada Y siempre sera la misma, mientras que la
	 * coordenada X sera igual a la del ultimo punto que tape.
	 * 
	 * @param r
	 *            rectangulo colocado
	 * @return Candidato a nuevo punto del eje y
	 */
	private Point obtainNewPointY(Rectangle r) {
		int prevY = 0;
		int i = 0;
		Point p = new Point(0, r.getHeight() + r.getPosition().getY());
		for (int j = 0; j < points.size(); j++) {
			if ((points.get(j).getY() >= prevY) && 
				(points.get(j).getY() < p.getY())) {
				prevY = points.get(j).getY();
				i = j;
			}
		}
		p.setX(points.get(i).getX());
		return p;
	}

	/**
	 * Obtiene las coordenadas donde iria el nuevo punto. Hay que tener en
	 * cuenta que la coordenada Y siempre sera la misma, mientras que la
	 * coordenada X sera igual a la del ultimo punto que tape.
	 * 
	 * @param r
	 *            rectangulo colocado
	 * @return Candidato a nuevo punto del eje y
	 */
	private Point obtainNewPointX(Rectangle r) {
		int prevX = 0;
		int i = 0;
		Point p = new Point(r.getBase() + r.getPosition().getX(), 0);
		for (int j = 0; j < points.size(); j++) {
			if ((points.get(j).getX() >= prevX)
					&& (points.get(j).getX() < p.getX())) {
				prevX = points.get(j).getX();
				i = j;
			}
		}
		p.setY(points.get(i).getY());
		return p;
	}

	private void initPoints() {
		Point origen = new Point(0, 0);
		this.points.clear();
		this.points.add(origen);
	}

	/**
	 * Funcion que tiene como objetivo la colocacion de los rectangulos para
	 * hallar el valor que tiene la funcion objetivo dada una solucion
	 * representada con una permutacion de rectangulos.
	 * 
	 * @param s
	 *            Solucion a evaluar.
	 */
	private Point[] areaEvalue(Solution s) {
		initPoints(); // Inicializa los puntos (establece como unico punto el
		// origen)
		s.reset();
		Point[] rectanglePosition = new Point[problem.getRectangleSize()];
		for (int i = 0; i < problem.getRectangleSize(); i++) {
			Rectangle r = getNewRectangle(i, s);
			// Guardamos la posicion del rectangulo i
			rectanglePosition[s.getOrder(i)] = areaAllocateRectangle(r, s);
			if (i != (problem.getRectangleSize() - 1))
				managePoints(r);
		}
		s.setObjF();
		return rectanglePosition;
	}

	/**
	 * Intercambia el elemento i con el j en el array
	 * 
	 * @param array
	 *            array de elementos
	 * @param i
	 *            una posicion del array
	 * @param j
	 *            otra posicion del array
	 */
	public static void swap(int[] array, int i, int j) {
		int aux = array[i];
		array[i] = array[j];
		array[j] = aux;
	}

	/**
	 * Lanza los distintos metodos para buscar soluciones vecinas a una dada
	 * segun el tipo de muestreo escogido.
	 * 
	 * @param s
	 *            solucion de la que se halla una vecina.
	 * @param neighbourType
	 *            Constante que define la estructura de entorno, puede ser:
	 *            NO_NEIGHBOUR_SAMPLING, GREEDY_SAMPLING, ANXIOUS_SAMPLING, o
	 *            RANDOM_SAMPLING
	 * @param sampleType
	 *            Constante que define el tipo de muestreo de la estructura de
	 *            entorno, puede ser: RANDOM_SWAP, ONE_SWAP o SWAP_WITH_LAST
	 * @return Solucion vecina de s siguiendo algun criterio
	 */
	private Solution neighbour(Solution s, int neighbourType, int sampleType, int evaluationMode) {
		Solution best = s.clone();
		switch (sampleType) {
		case NO_SAMPLING: // Sin muestreo del entorno
			best = noNeighbourSampling(best, neighbourType);
			break;
		case GREEDY_SAMPLING: // Muestreo GREEDY, se escoje el mejor de los
			// vecinos
			best = greedyNeighbourSampling(best, neighbourType, evaluationMode);
			break;
		case ANXIOUS_SAMPLING: // Muestreo ANXIOUS, se escoje el primer mejor
			// vecino
			best = anxiousNeighbourSampling(best, neighbourType, evaluationMode);
			break;
		case RANDOM_SAMPLING: // Muestreo RANDOM, se escoje el primer mejor
			// vecino aleatorio
			best = randomNeighbourSampling(best, neighbourType, evaluationMode);
			break;
		}
		return best;
	}

	/**
	 * Metodo que devuelve una solución vecina a una dada, sin muestreo del
	 * entorno.
	 * 
	 * @param s
	 *            Solucion para la que se halla una vecina
	 * @param neighbourType
	 *            Constante que define la estructura de entorno, puede ser:
	 *            NO_NEIGHBOUR_SAMPLING, GREEDY_SAMPLING, ANXIOUS_SAMPLING, o
	 *            RANDOM_SAMPLING
	 * @return solucion vecina de s sin muestreo del entorno
	 */
	private Solution noNeighbourSampling(Solution s, int neighbourType) {
		if (neighbourType != RANDOM_SWAP) {
			System.out.println("No se puede usar otro tipo de vecino sin muestreo de entorno que no sea RANDOM_SWAP");
			return null;
		} else {
			Random r = new Random(System.nanoTime());
			int[] newOrder = s.getOrder().clone();

			int i = (int) (r.nextFloat() * newOrder.length);
			int j = (int) (r.nextFloat() * newOrder.length);

			while (i == j)
				j = (int) (r.nextFloat() * newOrder.length);

			swap(newOrder, i, j);
			s.setOrder(newOrder);
			return s;
		}
	}

	/**
	 * Metodo que devuelve una solucion vecina a una dada, con muestreo Voraz.
	 * Evalua todos los vecinos candidatos y escoge el mejor entre todos ellos.
	 * 
	 * @param s
	 *            Solucion para la que se halla una vecina
	 * @param neighbourType
	 *            Constante que define la estructura de entorno, puede ser:
	 *            NO_NEIGHBOUR_SAMPLING, GREEDY_SAMPLING, ANXIOUS_SAMPLING, o
	 *            RANDOM_SAMPLING
	 * @return solucion vecina de s con muestreo Voraz
	 */
	private Solution greedyNeighbourSampling(Solution s, int neighbourType, int evaluationMode) {
		if (neighbourType == RANDOM_SWAP) {
			System.out.println("Solo se usa vecino aleatorio si el muestreo es aleatorio.");
			return null;
		} else {
			int[] newOrder = s.getOrder().clone();
			int size = problem.getRectangleSize();
			switch (neighbourType) {
			case ONE_SWAP:
				for (int i = 0; i < size; i++) {
					for (int j = i + 1; j < size; j++) {
						Solution aux = s.clone();
						swap(newOrder, i, j);
						aux.setOrder(newOrder);
						
						if (evaluationMode == WASTE)
							wasteEvalue(aux);
						else areaEvalue(aux);
						
						if (s.getObjF() > aux.getObjF())
							s = aux.clone();
					}
				}
				break;
			case SWAP_WITH_LAST:
				for (int i = 0; i < size - 1; i++) {
					Solution aux = s.clone();
					swap(newOrder, i, newOrder.length - 1);
					aux.setOrder(newOrder);
					
					if (evaluationMode == WASTE)
						wasteEvalue(aux);
					else areaEvalue(aux);
					
					if (s.getObjF() > aux.getObjF())
						s = aux.clone();
				}
				break;
			}
			return s;
		}
	}

	/**
	 * Metodo que devuelve una solucion vecina a una dada, con muestreo Ansioso.
	 * Selecciona la primera solucion vecina que mejore a s.
	 * 
	 * @param s
	 *            Solucion para la que se halla una vecina
	 * @param neighbourType
	 *            Constante que define la estructura de entorno, puede ser:
	 *            NO_NEIGHBOUR_SAMPLING, GREEDY_SAMPLING, ANXIOUS_SAMPLING, o
	 *            RANDOM_SAMPLING
	 * @return solucion vecina de s con muestreo Ansioso
	 */
	private Solution anxiousNeighbourSampling(Solution s, int neighbourType, int evaluationMode) {
		if (neighbourType == RANDOM_SWAP) {
			System.out.println("Solo se usa vecino aleatorio si el muestreo es aleatorio.");
			return null;
		} else {
			int[] newOrder = s.getOrder().clone();
			int size = problem.getRectangleSize();
			boolean betterFound = false;
			switch (neighbourType) {
			case ONE_SWAP:
				for (int i = 0; (i < size) && (!betterFound); i++) {
					for (int j = i + 1; (j < size) && (!betterFound); j++) {
						Solution aux = s.clone();
						swap(newOrder, i, j);
						aux.setOrder(newOrder);
						
						if (evaluationMode == WASTE)
							wasteEvalue(aux);
						else areaEvalue(aux);
						
						if (s.getObjF() > aux.getObjF())
							s = aux.clone();
					}
				}
				break;
			case SWAP_WITH_LAST:
				for (int i = 0; (i < size - 1) && (!betterFound); i++) {
					Solution aux = s.clone();
					swap(newOrder, i, newOrder.length - 1);
					aux.setOrder(newOrder);
					
					if (evaluationMode == WASTE)
						wasteEvalue(aux);
					else areaEvalue(aux);
					
					if (s.getObjF() > aux.getObjF())
						s = aux.clone();
				}
				break;
			}
			return s;
		}
	}

	/**
	 * Metodo que devuelve una solucion vecina a una dada, con muestreo
	 * Aleatorio. Selecciona de forma aleatoria un vecino que mejora a s.
	 * 
	 * @param s
	 *            Solucion para la que se halla una vecina
	 * @param neighbourType
	 *            Constante que define la estructura de entorno, puede ser:
	 *            NO_NEIGHBOUR_SAMPLING, GREEDY_SAMPLING, ANXIOUS_SAMPLING, o
	 *            RANDOM_SAMPLING
	 * @return solucion vecina de s con muestreo Aleatorio
	 */
	private Solution randomNeighbourSampling(Solution s, int neighbourType, int evaluationMode) {
		if (neighbourType != RANDOM_SWAP) {
			System.out.println("Si se usa muestreo aleatorio el vecino debe ser aleatorio.");
			return null;
		} else {
			Random r = new Random(System.nanoTime());
			int[] newOrder = s.getOrder().clone();
			int size = problem.getRectangleSize();
			boolean betterFound = false;
			for (int i = 0; (i < (2 * size)) && (!betterFound); i++) {
				Solution aux = s.clone();
				int l = (int) (r.nextFloat() * newOrder.length);
				int k = (int) (r.nextFloat() * newOrder.length);
				while (l == k)
					k = (int) (r.nextFloat() * newOrder.length);
				swap(newOrder, l, k);
				aux.setOrder(newOrder);
				
				if (evaluationMode == WASTE)
					wasteEvalue(aux);
				else areaEvalue(aux);
				
				if (s.getObjF() > aux.getObjF()) {
					s = aux.clone();
					betterFound = true;
				}
			}
			return s;
		}
	}

	/**
	 * Estima el espacio desperdiciado en un punto por un rectangulo r
	 * @param r Rectangulo
	 * @param p Punto donde se estima
	 */
	private int wasteCalculation(Rectangle r) {
		// Posible nuevo punto
		int waste = 0;
		Point p;
		ArrayList<Point> aux = new ArrayList<Point>(0);
		boolean newP = true;
		int i;
		// Se manipulan con respecto al eje Y
		p = obtainNewPointY(r);
		if (r.getPosition().getX() != 0) {
			i = 0;
			while (i < points.size()) {
				// Se eliminan los puntos con coordenada Y menor que la actual
				// si estos estan situados a la izquierda del rectangulo nuevo
				if ((points.get(i).getY() < p.getY())
						&& (points.get(i).getX() < r.getPosition().getX())) {
					aux.add(points.get(i));
				} else if (points.get(i).getY() == p.getY()) {
					newP = false;
					aux.add(points.get(i));
				}
				i++;
			}
			// Si no existe un punto en la misma longitud el nuevo punto sera la longitud maxima
			if (newP) {
				aux.add(p);
			}
			PointYComparator c = new PointYComparator();
			Collections.sort(aux, c);
			// Se suma la division de las areas resultantes en rectangulos.
			i = 0;
			for (int j = i + 1; j < aux.size(); j++) {
				waste = waste + (r.getPosition().getX() - aux.get(i).getX()) *
								(aux.get(j).getY() - aux.get(i).getY());
				i++;
			}
			aux.clear();
		}
		
		if (r.getPosition().getY() != 0) {
			// Se repite pero con respecto al eje X
			p = obtainNewPointX(r);
			newP = true;
			i = 0;
			while (i < points.size()) {
				// Se eliminan los puntos con coordenada X menor que la actual
				// si estos estan por debajo de la altura del rectangulo nuevo
				if ((points.get(i).getX() < p.getX())
						&& (points.get(i).getY() < r.getPosition().getY())) {
					aux.add(points.get(i));
				} else if (points.get(i).getX() == p.getX()) {
					aux.add(points.get(i));
					newP = false;
				}
				i++;
			}
			if (newP) {
				aux.add(p);
			}
			Collections.sort(aux);
			// Se suma la division de las areas resultantes en rectangulos.
			i = 0;
			for (int j = i + 1; j < aux.size(); j++) {
				waste = waste + (r.getPosition().getY() - aux.get(i).getY()) * 
								(aux.get(j).getX() - aux.get(i).getX());
				i++;
			}
		}
		return waste;
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
	 * @return el punto donde ira colocado el rectangulo.
	 */
	private Point wasteAllocateRectangle(Rectangle r, Solution s) {
		// El mejor caso es que las menores dimensiones sean las ya obtenidas anteriormente
		int minorWaste = 0;
		boolean modified = false;
		int selected = 0;
		// Se toman los nuevos datos resultantes de colocar el rectangulo
		// Como minimo van a ser iguales que los actuales
		for (int i = 0; i < points.size(); i++) {
			r.setPosition(this.points.get(i));
			int actualWaste = wasteCalculation(r);
			// Si son mejores que los actuales se guardan
			if ((!modified) || (actualWaste < minorWaste)) {
				selected = i;
				minorWaste = actualWaste;
				modified = true;
			}
		}
		// Se actualiza la solucion
		// Eje y
		int newH = s.getHeight();
		if ((points.get(selected).getY() + r.getHeight()) > (s.getHeight()))
			newH = points.get(selected).getY() + r.getHeight();
		// Eje X
		int newB = s.getBase();
		if ((points.get(selected).getX() + r.getBase()) > (s.getBase()))
			newB = points.get(selected).getX() + r.getBase();
		s.setBase(newB);
		s.setHeight(newH);
		r.setPosition(this.points.get(selected));
		return r.getPosition();
	}
	
	/**
	 * Funcion que tiene como objetivo la colocacion de los rectangulos para hallar el valor
	 * que tiene la funcion objetivo dada una solucion representada con una permutacion de 
	 * rectangulos.
	 * @param s
	 *          Solucion a evaluar.
	 */
	private Point[] wasteEvalue(Solution s) {
		initPoints(); // Inicializa los puntos (establece como unico punto el origen)
		s.reset();
		Point [] rectanglePosition = new Point[problem.getRectangleSize()];
		for (int i = 0; i < problem.getRectangleSize(); i++) {
			Rectangle r = getNewRectangle(i, s);
			// Guardamos la posicion del rectangulo i
			rectanglePosition[s.getOrder(i)] = wasteAllocateRectangle(r, s);
			if (i != (problem.getRectangleSize() - 1))
				managePoints(r);
		}
		s.setObjF();
		return rectanglePosition;
	}
	
	
	
}