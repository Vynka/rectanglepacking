package rpp;

import java.util.ArrayList;
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
 * @version 1.01.03
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

	/**
	 * Tipos de busqueda de solucion
	 */
	// static final int LOCAL_SEARCH = 30;
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
		evalue(p.getSolution());
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
			pureRandomSearch(hop.getTimes(), hop.getStopCriteria());
			break;
		case RANDOM_SEARCH:
			randomSearch(hop.getTimes(), hop.getStopCriteria());
			break;
		case LOCAL_SEARCH:
			localSearch(hop.getNeighbors(), hop.getSampling());
			break;
		case MULTISTART_WITH_LOCAL_SEARCH:
			multistartSearch(hop.getTimes(), hop.getNeighbors(), hop.getSampling(),
							 Solution.DETERMINISTIC1, hop.getStopCriteria());
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
	public Solution localSearch(int neighbourType, int sampleType) {
		Solution actual = problem.getSolution().clone();
		Point[] bestPos = evalue(actual);
		Solution best = actual.clone();
		boolean betterFound;
		do {
			betterFound = false;
			actual = neighbour(actual, neighbourType, sampleType);
			Point[] actPos = evalue(actual);
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
	public Solution pureRandomSearch(int times, int stop) {
		int n = times;
		Solution best = problem.getSolution().clone();
		Point[] bestPos = problem.getActualPositions();
		Solution actual = best.clone();
		do {
			actual = new Solution(problem.getAreaRec(), Solution.RANDOM,
								  problem.getRectangleSize());
			Point[] actPos = evalue(actual);

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
	public Solution randomSearch(int times, int stop) {
		int n = times;
		Solution best = problem.getSolution().clone();
		Point[] bestPos = problem.getActualPositions();
		Solution actual = best.clone();
		do {
			actual = neighbour(actual, RANDOM_SWAP, NO_SAMPLING);
			Point[] actPos = evalue(actual);

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
	public Solution multistartSearch(int times, int neighbourType,
			int sampleType, int initType, int stop) {
		int n = times;
		problem.setSolution(new Solution(problem.getAreaRec(), initType,
							problem.getRectangleSize()));
		Point[] bestPos = evalue(problem.getSolution());
		Solution actual;
		Solution best = problem.getSolution().clone();
		do {
			actual = localSearch(neighbourType, sampleType);
			Point[] actPos = evalue(actual);

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

			problem.setSolution(new Solution(problem.getAreaRec(), initType,
								problem.getRectangleSize()));
			n--;
		} while (n > 0);
		problem.setSolution(best);
		problem.changeRectanglePositions(bestPos);
		return best.clone();
	}

	/**
	 * Metodo heuristico de Busqueda por entorno numero 5. Busqueda por Recocido
	 * Simulado.
	 */
	public Solution simulatedAnnealingSearch(int initType, int neighbourType,
											 int sampleType) {
		Solution actual;
		Random r = new Random(System.nanoTime());
		actual = new Solution(problem.getAreaRec(), initType, problem.getRectangleSize());
		int c = 0/*
				 * numero suficientemente grande como para poder aceptar
				 * cualquier solucion vecina al principio
				 */;
		int L = 0/* numero de iteraciones peque�o al principio */;
		int k = 0; // Numero de iteraciones
		Point[] bestPos = evalue(actual);
		Solution best = actual.clone();
		do {
			actual = neighbour(actual, neighbourType, sampleType);
			Point[] actPos = evalue(actual);
			if (best.getObjF() > actual.getObjF()) {
				best = actual.clone();
				bestPos = actPos.clone();
				k++;
			} else if (Math.exp((actual.getObjF() - best.getObjF()) / c) > r.nextFloat()) {
				best = actual.clone();
				bestPos = actPos.clone();
				k++;
			}
			CalculateTemperature(c, k);
			CalculateSize(L, k);
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
	 * Calcula la temperatura del metodo de busqueda SimulatedAnnealing para la
	 * iteracion k
	 * 
	 * @param c
	 *            temperatura anterior
	 * @param k
	 *            Numero de iteraciones hechas
	 */
	private int CalculateTemperature(int c, int k) {
		return 0;
	}

	/**
	 * Calcula el numero de iteraciones del metodo de busqueda SimulatedAnneaing
	 * para la iteracion k
	 * 
	 * @param L
	 *            Numero de iteraciones anterior
	 * @param k
	 *            Numero de iteraciones hechas
	 */
	private int CalculateSize(int L, int k) {
		return 0;
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
	private Point allocateRectangle(Rectangle r, Solution s) {
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
	private Point[] evalue(Solution s) {
		initPoints(); // Inicializa los puntos (establece como unico punto el
		// origen)
		s.reset();
		Point[] rectanglePosition = new Point[problem.getRectangleSize()];
		for (int i = 0; i < problem.getRectangleSize(); i++) {
			Rectangle r = getNewRectangle(i, s);
			// Guardamos la posicion del rectangulo i
			rectanglePosition[s.getOrder(i)] = allocateRectangle(r, s);
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
	private Solution neighbour(Solution s, int neighbourType, int sampleType) {
		Solution best = s.clone();
		switch (sampleType) {
		case NO_SAMPLING: // Sin muestreo del entorno
			noNeighbourSampling(best, neighbourType);
			break;
		case GREEDY_SAMPLING: // Muestreo GREEDY, se escoje el mejor de los
			// vecinos
			greedyNeighbourSampling(best, neighbourType);
			break;
		case ANXIOUS_SAMPLING: // Muestreo ANXIOUS, se escoje el primer mejor
			// vecino
			anxiousNeighbourSampling(best, neighbourType);
			break;
		case RANDOM_SAMPLING: // Muestreo RANDOM, se escoje el primer mejor
			// vecino aleatorio
			randomNeighbourSampling(best, neighbourType);
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
	private void noNeighbourSampling(Solution s, int neighbourType) {
		if (neighbourType != RANDOM_SWAP)
			System.out.println("No se puede usar otro tipo de vecino sin muestreo de entorno que no sea RANDOM_SWAP");
		else {
			Random r = new Random(System.nanoTime());
			int[] newOrder = s.getOrder().clone();

			int i = (int) (r.nextFloat() * newOrder.length);
			int j = (int) (r.nextFloat() * newOrder.length);

			while (i == j)
				j = (int) (r.nextFloat() * newOrder.length);

			swap(newOrder, i, j);
			s.setOrder(newOrder);
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
	private void greedyNeighbourSampling(Solution s, int neighbourType) {
		if (neighbourType == RANDOM_SWAP)
			System.out.println("Solo se usa vecino aleatorio si el muestreo es aleatorio.");
		else {
			Random r = new Random(System.nanoTime());
			int[] newOrder = s.getOrder().clone();
			int size = problem.getRectangleSize();
			switch (neighbourType) {
			case ONE_SWAP:
				for (int i = 0; i < size; i++) {
					for (int j = i + 1; j < size; j++) {
						Solution aux = s.clone();
						swap(newOrder, i, j);
						aux.setOrder(newOrder);
						evalue(aux);
						if (s.getObjF() > aux.getObjF())
							s = aux.clone();
					}
				}
				break;
			case RANDOM_SWAP:
				for (int i = 0; i < (2 * size); i++) {
					Solution aux = s.clone();
					int l = (int) (r.nextFloat() * newOrder.length);
					int k = (int) (r.nextFloat() * newOrder.length);
					while (l == k)
						k = (int) (r.nextFloat() * newOrder.length);
					swap(newOrder, l, k);
					aux.setOrder(newOrder);
					evalue(aux);
					if (s.getObjF() > aux.getObjF())
						s = aux.clone();
				}
				break;
			case SWAP_WITH_LAST:
				for (int i = 0; i < size; i++) {
					Solution aux = s.clone();
					swap(newOrder, i, newOrder.length - 1);
					aux.setOrder(newOrder);
					evalue(aux);
					if (s.getObjF() > aux.getObjF())
						s = aux.clone();
				}
				break;
			}
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
	private void anxiousNeighbourSampling(Solution s, int neighbourType) {
		if (neighbourType == RANDOM_SWAP)
			System.out.println("Solo se usa vecino aleatorio si el muestreo es aleatorio.");
		else {
			Random r = new Random(System.nanoTime());
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
						evalue(aux);
						if (s.getObjF() > aux.getObjF())
							s = aux.clone();
					}
				}
				break;
			case RANDOM_SWAP:
				for (int i = 0; i < (2 * size) && (!betterFound); i++) {
					Solution aux = s.clone();
					int l = (int) (r.nextFloat() * newOrder.length);
					int k = (int) (r.nextFloat() * newOrder.length);
					while (l == k)
						k = (int) (r.nextFloat() * newOrder.length);
					swap(newOrder, l, k);
					aux.setOrder(newOrder);
					evalue(aux);
					if (s.getObjF() > aux.getObjF())
						s = aux.clone();
				}
				break;
			case SWAP_WITH_LAST:
				for (int i = 0; (i < size) && (!betterFound); i++) {
					Solution aux = s.clone();
					swap(newOrder, i, newOrder.length - 1);
					aux.setOrder(newOrder);
					evalue(aux);
					if (s.getObjF() > aux.getObjF())
						s = aux.clone();
				}
				break;
			}
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
	private void randomNeighbourSampling(Solution s, int neighbourType) {
		if (neighbourType != RANDOM_SWAP)
			System.out.println("Si se usa muestreo aleatorio el vecino debe ser aleatorio.");
		else {
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
				evalue(aux);
				if (s.getObjF() > aux.getObjF()) {
					s = aux.clone();
					betterFound = true;
				}
			}
		}
	}

}