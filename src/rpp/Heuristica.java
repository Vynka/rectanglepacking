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
 * @version 1.06.17
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
	static final int PURE_RANDOM_SEARCH = 0;
	static final int RANDOM_SEARCH = 1;
	static final int LOCAL_SEARCH = 2;
	static final int MULTISTART_WITH_LOCAL_SEARCH = 3;
	static final int SIMULATED_ANNEALING_SEARCH = 4;
	static final int GRASP = 5;

	/**
	 * Eleccion de heuristica de colocacion
	 */
	static final int WASTE_EVAL = 0;
	static final int AREA_EVAL = 1;
	static final int MIXED_EVAL = 2;
	static final int POND_EVAL = 3;
	
	/**
	 * Eleccion de la eleccion del rectangulo en el GRASP
	 */
	static final int AREA_GRASP = 0;
	static final int DIAGONAL_GRASP = 1;
	static final int MIXED_GRASP = 2;
	static final int POND_GRASP = 3;
	static final int WASTE_GRASP = 4;
	
	private static final int SETSIZE = 2;
	
	/**
	 * Tipo de periodo tabu en el tabuSearch
	 */
	static final int CONSTANT_TENURE = 0;
	static final int SQRT_TENURE = 1;
	static final int SIMPLE_RAND_DINAMIC_TENURE = 2;
	static final int SIMPLE_SISTEMATIC_DINAMIC_TENURE = 3;
	static final int COMPLEX_DINAMIC_TENURE = 4;

	
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
	public Heuristica(Problem p, int evaluationMode) {
		initPoints();
		this.problem = p;
		callEvalue(p.getSolution(), evaluationMode);
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
			simulatedAnnealingSearch(hop.getNeighbors(), hop.getSampling(), hop.getDoubCoolingFactor(), 1.05, hop.getEvaluationMode());
			break;
		case GRASP:
			GRASP(hop.getGraspMode());
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
			
			Point [] actPos = callEvalue(actual, evaluationMode);
			
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
					
			Point [] actPos = callEvalue(actual, evaluationMode);

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
			
			Point [] actPos = callEvalue(actual, evaluationMode);

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

			Point [] actPos = callEvalue(actual, evaluationMode);
			
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
			nueva = scatterSolution(actual);
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
	public Solution simulatedAnnealingSearch(int neighbourType,
											 int sampleType, double coolingFactor, double iterationIncrement,
											 int evaluationMode) {
		//double zero = 0.1;
		double zero = 0.1/(Math.log(
						   (Math.pow(problem.getRectangleSize(), problem.getRectangleSize()) - 1)
						   / 0.60));
		Solution actual;
		Random r = new Random(System.nanoTime());
		actual = problem.getSolution().clone();
		double c = (int) Math.sqrt(problem.getRectangleSize());
		int L = 0;
		if (coolingFactor >= 0.8)
			L = (int) Math.pow(problem.getRectangleSize(), 2);
		else
			L = 1;
		int k = 0;                // Numero de iteraciones hechas
		
		Point[] bestPos = callEvalue(actual, evaluationMode);
		
		Solution best = actual.clone();
		do {
			for (int i = 0; i < L; i++) {
				actual = neighbour(actual, neighbourType, sampleType, evaluationMode);
								
				Point [] actPos = callEvalue(actual, evaluationMode);
				
				if (best.getObjF() > actual.getObjF()) {
					best = actual.clone();
					bestPos = actPos.clone();
				} else if (Math.exp((actual.getObjF() - best.getObjF()) / c) > r.nextFloat()) {
					best = actual.clone();
					bestPos = actPos.clone();
				}
			}
			k++;
			//Deteccion automatica del plan de enfriamiento
			if (coolingFactor >= 0.8) {
				c = CalculateTemperature(c, coolingFactor);
			  	L = CalculateSize(k, iterationIncrement); 
			}
			else
				c  = CalculateTemperature2(c, coolingFactor);
		} while (c > zero);
		System.out.println("C = " + c + "     L = " + L + "     k = " + k);
		problem.setSolution(best);
		problem.changeRectanglePositions(bestPos);
		return best.clone();
	}
	
	/**
	 * 
	 * @param neighbourType
	 * @param sampleType
	 * @param tenure
	 * @param tenureType
	 * @param evaluationMode
	 * @return
	 */
	public Point[] tabuSearch(int neighbourType, int sampleType,
			 int tenure, int tenureType, int evaluationMode) {
		getTenure(problem, tenure, tenureType);
		tabuMemory mem = new tabuMemory(problem);
		Solution actual = problem.getSolution().clone();		
		Solution best = actual.clone();
		Point[] bestPos = problem.getActualPositions();
		boolean betterFound;
		int besti = 0;
		int bestj = 0;
		
		do {
			betterFound = false;
			for (int i = 0; i < problem.getRectangleSize(); i++)
				for (int j = 0; j < problem.getRectangleSize(); j++) {
					actual = best.clone();
					int[] newOrder = best.getOrder().clone();
					swap(newOrder, i, j);
					actual.setOrder(newOrder);
					Point [] actPos = callEvalue(actual, evaluationMode);
					if (mem.getRecency(i, j) == 0)
						if (best.getObjF() > actual.getObjF()) {
							best = actual.clone();
							besti = i;
							bestj = j;
							bestPos = actPos.clone();
							betterFound = true;
						}
				}
			mem.decRecency();
			if ((tenureType != CONSTANT_TENURE) && (tenureType != SQRT_TENURE))
				getTenure(problem, tenure, tenureType);
			mem.setRecency(besti, bestj, tenure);
		} while (betterFound);
		
		problem.setSolution(best);		
		problem.changeRectanglePositions(bestPos);
		return bestPos;
	}
	
	/**
	 * 
	 * @param p
	 * @param tenure
	 * @param tenureType
	 */
	private void getTenure(Problem p, int tenure, int tenureType) {
		switch (tenureType) {
			case CONSTANT_TENURE:
				break;
			case SQRT_TENURE:
				tenure = (int) Math.sqrt(p.getRectangleSize());
				break;
			case SIMPLE_RAND_DINAMIC_TENURE:
				//Elegido al azar entre un rango fijo de valores (por ejemplo entre 3 y 7)
				break;
			case SIMPLE_SISTEMATIC_DINAMIC_TENURE:
				//Elegido sistematicamente entre un rango fijo de valores
				break;
			case COMPLEX_DINAMIC_TENURE:
				//Elegido al azar entre un rango variable de valores
				break;
		}
	}
	

	/**
	 * Genera una solucion de manera constructiva.
	 * @return posiciones de los rectangulos en pantalla
	 */
	public Point[] GRASP(int evaluationMode) {
		initPoints(); // Inicializa los puntos
		int [] order = new int [problem.getRectangleSize()];
		Rectangle [] rectangles = problem.getRectangles().clone();
		Point[] rectanglePosition = new Point[problem.getRectangleSize()];
		Solution s = new Solution(0, 0, problem.getAreaRec(), null);
		int rectPos = 0;
		// Va colocando los rectangulos y guardando sus posiciones
		for (int i = 0; i < problem.getRectangleSize(); i++) {
			switch (evaluationMode) {
			case AREA_GRASP:
				rectPos = nextGRASPRectangle(rectangles, s, AREA_EVAL);
				break;
			case WASTE_GRASP:
				rectPos = nextGRASPRectangle(rectangles, s, WASTE_EVAL);
				break;
			case MIXED_GRASP:
				rectPos = nextGRASPRectangle(rectangles, s, MIXED_EVAL);
				break;
			case POND_GRASP:
				rectPos = nextGRASPRectangle(rectangles, s, POND_EVAL);
				break;
			case DIAGONAL_GRASP:
				rectPos = nextDiagonalGRASPRectangle(rectangles, s);
				break;
			}
			// Guardamos la posicion del rectangulo i, su posicion y evitamos que se vuelva a elegir
			order[i] = rectPos;
			rectanglePosition[rectPos] = rectangles[rectPos].getPosition();
			if (i != (problem.getRectangleSize() - 1)) {
				managePoints(rectangles[rectPos]);
				rectangles[rectPos] = null;
			}
		}
		// Se ponen los datos de la solucion
		s.setOrder(order);
		s.setObjF();
		problem.setSolution(s);
		problem.changeRectanglePositions(rectanglePosition);
		return rectanglePosition;
	}

	/**
	 * Plan de enfriamiento #1, Lineal.
	 * 
	 * @param c
	 *            temperatura anterior
	 * @param k
	 *            Numero de iteraciones hechas
	 */
	private double CalculateTemperature(double c, double coolingFactor) {
		return (c * coolingFactor);
	}

	/**
	 * Plan de enfriamiento #2, Lundy-Mees.
	 * 
	 * @param c
	 *            temperatura anterior
	 * @param k
	 *            Numero de iteraciones hechas
	 */
	private double CalculateTemperature2(double c, double coolingFactor) {
		return (c / (1 + (c * coolingFactor)));
	}
	
	/**
	 * Calcula el numero de iteraciones del metodo de busqueda SimulatedAnneaing
	 * para la iteracion k.
	 * 
	 * @param k
	 *            Numero de iteraciones hechas
	 */
	private int CalculateSize(int k, double iterationIncrement) {
		return (int)(k * iterationIncrement);
	}

	/**
	 * Calcula el mejor rectangulo que se adapta a la solucion
	 * empleando como criterio Minimizacion de area
	 * @return posicion en el array del rectangulo que minimiza el area
	 */
	private int nextGRASPRectangle(Rectangle [] rectangles, Solution s, int evaluationMode) {
		Random rand = new Random(System.nanoTime());
		ArrayList<Integer> selected = new ArrayList<Integer>(SETSIZE);
		ArrayList<Point> bestPoint = new ArrayList<Point>(0);
		// Se hallan los puntos donde cada rectangulo desvia lo menos posible de pi/4 la diagonal
		ArrayList<Double> deviation = diagonalDeviation(rectangles, s);
		// De las colocaciones que desvian menos la diagonal, se coge la que devuelve menor area
		Solution aux = new Solution(s.getBase(), s.getHeight(), problem.getAreaRec(), new int[0]);
		for (int i = 0; i < rectangles.length; i++) {
			if (rectangles[i] != null) {
				switch (evaluationMode) {
				case AREA_EVAL:
					bestPoint.add(areaAllocateRectangle(rectangles[i], aux));
					break;
				case MIXED_EVAL:
					if (i < Math.sqrt(problem.getRectangleSize()))
						bestPoint.add(wasteAllocateRectangle(rectangles[i], aux));
					else bestPoint.add(areaAllocateRectangle(rectangles[i], aux));
					break;
				case POND_EVAL:
					bestPoint.add(pondAllocateRectangle(rectangles[i], aux));
					break;
				case WASTE_EVAL:
					bestPoint.add(wasteAllocateRectangle(rectangles[i], aux));
					break;
				}
			} else {
				bestPoint.add(new Point(0,0));
			}
		}
		// Se coge entre las SETSIZE de menor desvio producido
		for (int i = 0; i < deviation.size(); i++) {
			if (rectangles[i] != null) {	
				if (selected.size() < SETSIZE) {
					selected.add(i);
				} else {
					int worst = 0;
					for (int j = 1; j < selected.size(); j++) {
						if (deviation.get(selected.get(worst)) < deviation.get(selected.get(j))) {
							worst = j;
						}
					}
					if (deviation.get(i) < deviation.get(selected.get(worst))) {
						selected.set(worst, i);
					}
				}
			}
		}
		// Se actualiza la solucion
		int ranSelect = rand.nextInt(selected.size());
		int selctInd = selected.get(ranSelect);
		rectangles[selctInd].setPosition(bestPoint.get(selctInd));
		
		// Eje y
		int newH = s.getHeight();
		if ((rectangles[selctInd].getPosition().getY() + rectangles[selctInd].getHeight()) > (s.getHeight()))
			newH = rectangles[selctInd].getPosition().getY() + rectangles[selctInd].getHeight();
		// Eje X
		int newB = s.getBase();
		if ((rectangles[selctInd].getPosition().getX() + rectangles[selctInd].getBase()) > (s.getBase()))
			newB = rectangles[selctInd].getPosition().getX() + rectangles[selctInd].getBase();
		
		s.setBase(newB);
		s.setHeight(newH);
		
		return selected.get(ranSelect);
	}
	
	/**
	 * 
	 * @param r
	 * @param s
	 * @return
	 */
	private double diagonalDeviation(Rectangle r, Solution s) {
		// Nueva altura
		int h = s.getHeight();
		if (r.getPosition().getY() + r.getHeight() > (s.getHeight()))
			h = r.getPosition().getY() + r.getHeight();
		// Nueva base
		int b = s.getBase();
		if (r.getPosition().getX() + r.getBase() > (s.getBase()))
			b = r.getPosition().getX() + r.getBase();
		double angle = Math.atan2(h, b);
		double deviation = Math.abs((Math.PI / 4) - angle);
		return deviation;
	}
	
	/**
	 * 
	 * @param rectangles
	 * @param pointInd
	 * @param s
	 * @return
	 */
	private ArrayList<Double> diagonalDeviation(Rectangle [] rectangles, ArrayList<Integer> pointInd, Solution s) {
		// Se halla la menor desviacion causada por cada punto
		ArrayList<Double> deviation = new ArrayList<Double>(0);
		for (int i = 0; i < rectangles.length; i++) {
			if (rectangles[i] != null) {
				// Se toman los nuevos datos resultantes de colocar el rectangulo
				// Como minimo van a ser iguales que los actuales
				for (int j = 0; j < points.size(); j++) {
					rectangles[i].setPosition(this.points.get(j));
					double actualDeviation = diagonalDeviation(rectangles[i], s);
					// Si son mejores que los actuales se guardan
					if (deviation.size() < (i + 1)) {
						deviation.add(actualDeviation);
						pointInd.add(j);
					} else if (deviation.get(i) > actualDeviation) {
						deviation.set(i, actualDeviation);
						pointInd.set(i, j);
					}
				}
			} else {
				deviation.add(Double.MAX_VALUE);
				pointInd.add(-1);
			}
		}
		return deviation;
	}
	
	/**
	 * 
	 * @param rectangles
	 * @param s
	 * @return
	 */
	private ArrayList<Double> diagonalDeviation(Rectangle [] rectangles, Solution s) {
		// Se halla la menor desviacion causada por cada punto
		ArrayList<Double> deviation = new ArrayList<Double>(0);
		for (int i = 0; i < rectangles.length; i++) {
			if (rectangles[i] != null) {
				// Se toman los nuevos datos resultantes de colocar el rectangulo
				// Como minimo van a ser iguales que los actuales
				for (int j = 0; j < points.size(); j++) {
					rectangles[i].setPosition(this.points.get(j));
					double actualDeviation = diagonalDeviation(rectangles[i], s);
					// Si son mejores que los actuales se guardan
					if (deviation.size() < (i + 1)) {
						deviation.add(actualDeviation);
					} else if (deviation.get(i) > actualDeviation) {
						deviation.set(i, actualDeviation);
					}
				}
			} else {
				deviation.add(Double.MAX_VALUE);
			}
		}
		return deviation;
	}
	
	/**
	 * Calcula el mejor rectangulo que se adapta a la solucion
	 * empleando como criterio una minimizacion de la diagonal.
	 * @return posicion en el array del rectangulo que minimiza 
	 *         una ponderacion entre area y desperdicio
	 */
	private int nextDiagonalGRASPRectangle(Rectangle [] rectangles, Solution s) {
		Random rand = new Random(System.nanoTime());
		ArrayList<Integer> selected = new ArrayList<Integer>(0);
		ArrayList<Integer> pointInd = new ArrayList<Integer>(SETSIZE);
		ArrayList<Double> deviation = diagonalDeviation(rectangles, pointInd, s);
		// Se coge entre las SETSIZE mejores
		for (int i = 0; i < deviation.size(); i++) {
			if (rectangles[i] != null) {
				// Si son mejores que los actuales se guardan
				if (selected.size() < SETSIZE) {
					selected.add(i);
				} else {
					int worst = 0;
					for (int j = 1; j < selected.size(); j++) {
						if (deviation.get(selected.get(worst)) < deviation.get(selected.get(j))) {
							worst = j;
						}
					}
					if (deviation.get(i) < deviation.get(selected.get(worst))) {
						selected.set(worst, i);
					}
				}
			}
		}

		int selectedIndex = rand.nextInt(selected.size());
		// Rectangulo correspondiente
		int selecRect = selected.get(selectedIndex);
		// Punto correspondiente
		int selecPoint = pointInd.get(selecRect);
		// Se obtienen los nuevos datos de la solucion
		// Eje y
		int newH = s.getHeight();
		// Explicacion de los if: lo mismo que en las funciones evalue pero escogiendo entre uno de los
		// rectangulos aleatoriamente en el conjunto de los elegidos
		if ((points.get(selecPoint).getY() + 
				rectangles[selecRect].getHeight()) > (s.getHeight()))
			newH = points.get(selecPoint).getY() + rectangles[selecRect].getHeight();
		// Eje X
		int newB = s.getBase();
		if ((points.get(selecPoint).getX() + 
				rectangles[selecRect].getBase()) > (s.getBase()))
			newB = points.get(selecPoint).getX() + rectangles[selecRect].getBase();
		
		// Se actualiza
		s.setBase(newB);
		s.setHeight(newH);
		rectangles[selecRect].setPosition(this.points.get(selecPoint));
		return selecRect;
	}
	
	/**
	 * Genera una solucion aleatoria dispersa de la actual
	 * @param actual Solucion con la que comparar
	 * @return solucion generada
	 */
	private Solution scatterSolution(Solution actual) {
		Solution newS = null;
		int sameId;
		do {
			sameId = 0;
			newS = new Solution(problem.getAreaRec(), Solution.RANDOM, problem.getRectangleSize());
			for (int i = 0; i < problem.getRectangleSize(); i++)
			if (newS.getOrder(i) == actual.getOrder(i))
				sameId++;
		} while (sameId > Math.sqrt(problem.getRectangleSize()));
		return newS;
	}
	
	/**
	 * Genera una poblacion inicial del tamaño designado
	 * @param populSize tamano de la poblacion
	 * @return la poblacion generada
	 */
	private ArrayList<Solution> EAInitPopulation(int populSize) {
		ArrayList<Solution> population = new ArrayList<Solution>(0);
		population.add(new Solution(problem.getAreaRec(), Solution.RANDOM, problem.getRectangleSize()));
		while (population.size() < populSize) {
			Solution newS = scatterSolution(population.get(population.size() - 1));
			boolean valid = true;
			for (int i = 0; i < population.size(); i++) {
				valid = !newS.equals(population.get(i));
				if (!valid)
					break;
			}
			if (valid)
				population.add(newS);		
		}
		return population;
	}
	
	/**
	 * Selecciona de una poblacion de soluciones populSize elementos
	 * @param population poblacion donde escoger
	 * @param populSize numero de elementos a elegir
	 * @return poblacion seleccionada de populSize elementos
	 */
	private ArrayList<Solution> EASelect(ArrayList<Solution> population, int populSize) {
		// Se sigue el metodo de la rueda de hilar
		Random r = new Random(System.nanoTime());
		ArrayList<Solution> newPopulation = new ArrayList<Solution>(0);
		double [] hilar = new double [population.size()];
		
		int totalValue = 0;
		int maxValue = 0;
		for (int i = 0; i < population.size(); i++) {
			totalValue += population.get(i).getObjF();
			if (population.get(i).getObjF() > maxValue)
				maxValue = population.get(i).getObjF();
		}
		
		// Se halla el inicio del intervalo correspondiente a cada solucion 
		double probability = 0;
		for (int i = 0; i < population.size(); i++) {
			probability += (double)(population.get(i).getObjF()) / (double)totalValue;
			hilar[i] = probability;
		}
		
		// Se seleccionan aleatoriamente
		for (int i = 0; i < populSize; i++) {
			double chosen = r.nextFloat();
			int j = 0;
			while (hilar[j] < chosen)
				j++;
			newPopulation.add(population.get(j).clone());
		}
		return newPopulation;
	}
	
	/**
	 * Realiza la recombinacion de dos soluciones
	 * @param s1 uno de los padres
	 * @param s2 el otro padre
	 * @return el hijo fruto de la recombinacion
	 */
	private Solution EACrossover(Solution s1, Solution s2) {
		// Inicializacion
		Solution child = s1.clone();
		int [] newOrder = child.getOrder().clone();
		// que usar del primer padre
		boolean [] fromS1 = new boolean [newOrder.length];
		// que usar del segundo
		ArrayList<Integer> noOrderFromS2 = new ArrayList<Integer>();
		ArrayList<Integer> orderFromS2 = new ArrayList<Integer>();
		Random r = new Random (System.nanoTime());
		// Se designa aleatoriamente lo que se cruza
		for (int i = 0; i < fromS1.length; i++) {
			fromS1[i] = r.nextBoolean();
			if (!fromS1[i])
				noOrderFromS2.add(newOrder[i]);
		}
		
		// Se tiene en noOrderFromS2 lo que se usara de la solucion2 SIN el orden de esta
		int [] aux = new int [noOrderFromS2.size()];
		for (int i = 0; i < noOrderFromS2.size(); i++) {
			orderFromS2.add(0); // Se inicializa para usarlo en el siguiente bucle
			for (int j = 0; j < s2.getOrder().length; j++) {
				if (s2.getOrder(j) == noOrderFromS2.get(i)) {
					aux[i] = j;
					break;
				}
			}
		}
		
		// aux tiene la posicion de los elementos en el orden2, asi que se usa como base para ordenar
		// el array noOrderFromS2
		// TODO Optimizar la ordenacion O(n^2) -> O(n)
		for	(int i = 0; i < noOrderFromS2.size(); i++) {
			int lessers = 0;
			for (int j = 0; j < aux.length; j++) {
				if (aux[i] > aux[j]) {
					lessers++;
				}
			}
			orderFromS2.set(lessers, noOrderFromS2.get(i));
		}
		
		// Ahora se tiene en OrderFromS2 el orden en el que se usara
		int j = 0;
		for (int i = 0; i < newOrder.length; i++) {
			if (!fromS1[i]) {
				newOrder[i] = orderFromS2.get(j).intValue();
				j++;
				if (j >= orderFromS2.size())
					break;
			}
		}

		return child;
	}
	
	/**
	 * Realiza la recombinacion de los elementos de la poblacion
	 * @param population poblacion a tratar
	 * @return una nueva poblacion seleccionada y cruzada
	 */
	private ArrayList<Solution> EACrossover(ArrayList<Solution> population) {
		// Inicializacion
		ArrayList<Solution> newPopulation = new ArrayList<Solution>(population.size());
		for (int i = 0; i < population.size(); i++) {
			newPopulation.add(population.get(i).clone());
		}
		Random r = new Random(System.nanoTime());
		// Se cruzara el orden inferior de veces (con respecto al tam de la poblacion)
		int times = population.size() / 5;
		// Las Soluciones solo se escogeran UNA vez
		boolean [] alreadyChosen = new boolean [newPopulation.size()];
		// Se cruzan
		for (int i = 0; i < times; i++) {
			// Se escogen padre y madre de entre los no seleccionados
			int k = r.nextInt(alreadyChosen.length);
			while (alreadyChosen[k])
				k = r.nextInt(alreadyChosen.length);
			alreadyChosen[k] = true;
			
			int l = r.nextInt(alreadyChosen.length);
			while (alreadyChosen[l])
				l = r.nextInt(alreadyChosen.length);
			alreadyChosen[l] = true;
			
			// El nuevo hijo forma ahora parte de la poblacion pero no es opcion para ser recombinado
			newPopulation.add(EACrossover(newPopulation.get(l), newPopulation.get(k)));
		}

		return newPopulation;
	}
	
	/**
	 * Realiza mutacion
	 * @param s Solucion a mutar
	 * @return solucion mutada
	 */
	private Solution EAMutation(Solution s) {
		final int changes = 4; // Numero de elementos a mutar de la poblacion
		Random r = new Random (System.nanoTime());
		Solution mutant = s.clone();
		int [] toChange = new int [changes];
		// Se establecen los cambios
		int i = 0;
		int j;
		while (i < changes) {
			int rand = r.nextInt(mutant.getOrder().length);
			boolean valid = true;
			// Comprueba que no esten repetidos
			j = 0;
			while (j < i) {
				valid = !(toChange[j] == rand);
				if (!valid)
					break;
				j++;
			}
			if (valid) {
				toChange[i] = rand;
				i++;
			}
		}
		
		// Se desplazan hacia la izquierda solo los valores seleccionados
		j = 0;
		i = 1;
		while (i < toChange.length) {
			swap(mutant.getOrder(), toChange[i], toChange[j]);
			j++;
			i++;
		}
		return mutant;
	}
	
	/**
	 * Algoritmo evolutivo compuesto de mutacion, crossover y seleccion de soluciones
	 * @return la mejor solucion encontrada.
	 */
	public Solution EA(int populSize, int evaluationMode, int mutRate, int times, int stop) {
		// Inicializar
		Solution best = new Solution (problem.getAreaRec(), Solution.RANDOM, problem.getRectangleSize());
		ArrayList<Solution> population = EAInitPopulation(populSize);
		ArrayList<Solution> aux = null;
		boolean cont = true;
		Random r = new Random(System.nanoTime());		
		// Evaluar
		callEvalue(best, evaluationMode);
		for (int i = 0; i < population.size(); i++) {
			callEvalue(population.get(i), evaluationMode);
		}		
		
		// Ejecucion del algoritmo hasta criterio de parada
		do {
			aux = EACrossover(population);
			int i = 0;
			while (i < ((double)mutRate / (double)100 * aux.size())) {
				int mutant = r.nextInt(aux.size());
				aux.set(mutant, EAMutation(aux.get(mutant)));
				i++;
			}
			population = EASelect(aux, populSize);
		
			switch (stop) {
			case OUT_UNLESS_BETTER:
				Collections.sort(population);
				if (best.compareTo(population.get(0)) != 1) {
					cont = false;
				} else {
					best = population.get(0).clone();
				}
				break;
			case NUMBER_OF_TIMES:
				times--;
				if (times <= 0) {
					cont = false;
					Collections.sort(population);
					best = population.get(0).clone();
				}
				break;
			}
		} while (cont);
		
		problem.setSolution(best);
		problem.changeRectanglePositions(callEvalue(best, evaluationMode));
		return best;
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
	 * Metodo que devuelve una soluciÃ³n vecina a una dada, sin muestreo del
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
						
						callEvalue(aux, evaluationMode);
						
						if (s.getObjF() > aux.getObjF()) {
							s = aux.clone();
						}
					}
				}
				break;
			case SWAP_WITH_LAST:
				for (int i = 0; i < size - 1; i++) {
					Solution aux = s.clone();
					swap(newOrder, i, newOrder.length - 1);
					aux.setOrder(newOrder);
					
					callEvalue(aux, evaluationMode);
					
					if (s.getObjF() > aux.getObjF()) {
						s = aux.clone();
					}
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
						
						callEvalue(aux, evaluationMode);
						
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
					
					callEvalue(aux, evaluationMode);
					
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
				
				callEvalue(aux, evaluationMode);
				
				if (s.getObjF() > aux.getObjF()) {
					s = aux.clone();
					betterFound = true;
				}
			}
			return s;
		}
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
	 * 
	 */
	private double pondCalculation(Rectangle r, Solution s, double factor) {
		double areaFactor = 1 - factor;
		int h = r.getPosition().getY() + r.getHeight();
		int b = r.getPosition().getX() + r.getBase();
		int a = s.getArea() - (h * b);
		return ((areaFactor * a) + (factor * wasteCalculation(r)));
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
	private Point pondAllocateRectangle(Rectangle r, Solution s) {
		// El mejor caso es que las menores dimensiones sean las ya obtenidas anteriormente
		double minor = 0;
		boolean modified = false;
		int selected = 0;
		// Se toman los nuevos datos resultantes de colocar el rectangulo
		// Como minimo van a ser iguales que los actuales
		for (int i = 0; i < points.size(); i++) {
			r.setPosition(this.points.get(i));
			double actual = pondCalculation(r, s, 0.9);
			// Si son mejores que los actuales se guardan
			if ((!modified) || (actual < minor)) {
				selected = i;
				minor = actual;
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
	
	/**
	 * Funcion que tiene como objetivo la colocacion de los rectangulos para hallar el valor
	 * que tiene la funcion objetivo dada una solucion representada con una permutacion de 
	 * rectangulos.
	 * @param s
	 *          Solucion a evaluar.
	 */
	private Point[] mixedEvalue(Solution s) {
		initPoints(); // Inicializa los puntos (establece como unico punto el origen)
		s.reset();
		int n = (int)Math.sqrt(problem.getRectangleSize());
		Point [] rectanglePosition = new Point[problem.getRectangleSize()];
		for (int i = 0; i < n; i++) {
			Rectangle r = getNewRectangle(i, s);
			// Se coloca el rectangulo primero minimizando el espacio desperdiciado
			rectanglePosition[s.getOrder(i)] = wasteAllocateRectangle(r, s);
			managePoints(r);
		}
		for (int i = n; i < problem.getRectangleSize(); i++) {
			Rectangle r = getNewRectangle(i, s);
			// Se aprovecha el area libre resultante de minimizar el desperdicio
			rectanglePosition[s.getOrder(i)] = areaAllocateRectangle(r, s);
			if (i != (problem.getRectangleSize() - 1))
				managePoints(r);
		}
		s.setObjF();
		return rectanglePosition;
	}
	
	/**
	 * Funcion que tiene como objetivo la colocacion de los rectangulos para hallar el valor
	 * que tiene la funcion objetivo dada una solucion representada con una permutacion de 
	 * rectangulos.
	 * @param s
	 *          Solucion a evaluar.
	 */
	private Point[] pondEvalue(Solution s) {
		initPoints(); // Inicializa los puntos (establece como unico punto el
		// origen)
		s.reset();
		Point[] rectanglePosition = new Point[problem.getRectangleSize()];
		for (int i = 0; i < problem.getRectangleSize(); i++) {
			Rectangle r = getNewRectangle(i, s);
			// Guardamos la posicion del rectangulo i
			rectanglePosition[s.getOrder(i)] = pondAllocateRectangle(r, s);
			if (i != (problem.getRectangleSize() - 1))
				managePoints(r);
		}
		s.setObjF();
		return rectanglePosition;
	}
	
	/**
	 * Llama al evalue correspondiente
	 * @param s Solucion a evaluar
	 * @param evaluationMode metodo de evaluacion
	 * @return Las posiciones de los rectangulos
	 */
	private Point[] callEvalue(Solution s, int evaluationMode) {
		Point [] rectanglePosition = null;
		switch (evaluationMode) {
		case WASTE_EVAL:
			rectanglePosition = wasteEvalue(s);
			break;
		case AREA_EVAL:
			rectanglePosition = areaEvalue(s);
			break;
		case MIXED_EVAL:
			rectanglePosition = mixedEvalue(s);
			break;
		case POND_EVAL:
			rectanglePosition = pondEvalue(s);
			break;
		}
		return rectanglePosition;
	}
	
	
}