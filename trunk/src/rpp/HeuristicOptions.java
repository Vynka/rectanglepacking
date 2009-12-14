package rpp;


/**
 * Concepto abstracto para la eleccion del metodo a realizar en la heuristica.
 * @author Haside
 * @version 1.02.10
 * @since 1.01.04
 * @see Heuristica
 */
public class HeuristicOptions {
	
	/**
	 * Constantes de tipos de generacion inicial
	 */
	public static final int RANDOM = Solution.RANDOM;
	public static final int DETERMINISTIC1 = Solution.DETERMINISTIC1;
	public static final int DETERMINISTIC2 = Solution.DETERMINISTIC2;
	public static final int MIXED1 = Solution.MIXED1;
	public static final int MIXED2 = Solution.MIXED2;
	
	/**
	 * Posibles espacios de entorno para soluciones vecinas.
	 */
	public static final int RANDOM_SWAP = Heuristica.RANDOM_SWAP;
	public static final int ONE_SWAP = Heuristica.ONE_SWAP;
	public static final int SWAP_WITH_LAST = Heuristica.SWAP_WITH_LAST;
	
	/**
	 * Criterios de parada
	 */
	public static final int OUT_UNLESS_BETTER = Heuristica.OUT_UNLESS_BETTER;
	public static final int NUMBER_OF_TIMES = Heuristica.NUMBER_OF_TIMES;
	
	/**
	 * Tipos de muestreo del entorno
	 */
	public static final int GREEDY_SAMPLING = Heuristica.GREEDY_SAMPLING;
	public static final int ANXIOUS_SAMPLING = Heuristica.ANXIOUS_SAMPLING;
	public static final int RANDOM_SAMPLING = Heuristica.RANDOM_SAMPLING;
	public static final int NO_SAMPLING = Heuristica.NO_SAMPLING;
	
	/**
	 * Metodos de resolucion del problema
	 */
	public static final int PURE_RANDOM_SEARCH = Heuristica.PURE_RANDOM_SEARCH;
	public static final int RANDOM_SEARCH = Heuristica.RANDOM_SEARCH;
	public static final int LOCAL_SEARCH = Heuristica.LOCAL_SEARCH;
	public static final int MULTISTART_WITH_LOCAL_SEARCH = Heuristica.MULTISTART_WITH_LOCAL_SEARCH;
	public static final int SIMULATED_ANNEALING_SEARCH = Heuristica.SIMULATED_ANNEALING_SEARCH;
	public static final int GRASP = Heuristica.GRASP;
	public static final int TABU_SEARCH = Heuristica.TABU_SEARCH;
	
	/**
	 * Eleccion de heuristica de colocacion
	 */
	public static final int WASTE_EVAL = Heuristica.WASTE_EVAL;
	public static final int AREA_EVAL = Heuristica.AREA_EVAL;
	public static final int MIXED_EVAL = Heuristica.MIXED_EVAL;
	public static final int POND_EVAL = Heuristica.POND_EVAL;
	
	/**
	 * Eleccion de la eleccion del rectangulo en el GRASP
	 */
	public static final int AREA_GRASP = Heuristica.AREA_GRASP;
	public static final int DIAGONAL_GRASP = Heuristica.DIAGONAL_GRASP;
	public static final int MIXED_GRASP = Heuristica.MIXED_GRASP;
	public static final int POND_GRASP = Heuristica.POND_GRASP;
	public static final int WASTE_GRASP = Heuristica.WASTE_GRASP;
	
	public static final int CONSTANT_TENURE = Heuristica.CONSTANT_TENURE;
	public static final int SQRT_TENURE = Heuristica.SQRT_TENURE;
	public static final int SIMPLE_RAND_DINAMIC_TENURE = Heuristica.SIMPLE_RAND_DINAMIC_TENURE;
	public static final int SIMPLE_SISTEMATIC_DINAMIC_TENURE = Heuristica.SIMPLE_SISTEMATIC_DINAMIC_TENURE;

	
	/**
	 * StopCritera: Criterio de parada del problema
	 */
	private int stopCriteria;
	
	/**
	 * Neighbors: Espacio de entorno. (Estructura de entorno) 
	 */
	private int neighbors;
	
	/**
	 * Initialization: Tipo de generacion de la solution inicial
	 */
	private int initialization;
	
	/**
	 * Sampling: Tipo de estructura de entorno.
	 */
	private int sampling;
	
	/**
	 * Procedure: Metodo de resolucion elegido.
	 */
	private int procedure;
	
	/**
	 * fileName: Problema heuristico a resolver
	 */
	private String fileName;
	
	/**
	 * times: Numero de veces a ejecutar la heuristica (Si aplicable)
	 */
	private int times;
	
	/**
	 * evaluationMode: tipo de heuristica de colocacion
	 */
	private int evaluationMode;
	
	/**
	 * coolingFactor: Factor de enfriamiento (Recocido Simulado)
	 */
	private int coolingFactor;
	
	/**
	 * graspMode: Tipo de GRASP
	 */
	private int graspMode;
	
	/**
	 * tabutenure: Tipo de seleccion de tenure en tabu search
	 */
	private int tabutenure;
	
	/**
	 * Constructor por defecto: PSR con OUT_UNLESS_BETTER
	 */
	public HeuristicOptions(String fileName) {
		this.stopCriteria = 0;
		this.neighbors = 0;
		this.sampling = 0;
		this.procedure = 0;
		this.times = 0;
		this.initialization = 0;
		this.coolingFactor = 0;
		this.fileName = fileName;
		this.evaluationMode = 0;
		this.setGraspMode(0);
	}	
	
	/**
	 * @param init the initialization type
	 */
	public void setInitialization(int init) {
		this.initialization = init;
	}
	
	/**
	 * @return tipo de inicializacion
	 */
	public int getInitialization() {
		return initialization;
	}
	
	/**
	 * @param times the number of times to set
	 */
	public void setTimes(int times) {
		this.times = times;
	}


	/**
	 * @return number of times to execute an heuristic
	 */
	public int getTimes() {
		return times;
	}
	
	
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}



	/**
	 * @return the stop
	 */
	public int getStopCriteria() {
		return stopCriteria;
	}

	/**
	 * @param stop the stop to set
	 */
	public void setStop(int stopCriteria) {
		this.stopCriteria = stopCriteria;
	}

	/**
	 * @return the neighbors
	 */
	public int getNeighbors() {
		return neighbors;
	}

	/**
	 * @param neighbours the neighbors to set
	 */
	public void setNeighbors(int neighbours) {
		this.neighbors = neighbours;
	}

	/**
	 * @return the sampling
	 */
	public int getSampling() {
		return sampling;
	}

	/**
	 * @param sampling the sampling to set
	 */
	public void setSampling(int sampling) {
		this.sampling = sampling;
	}

	/**
	 * @return the procedure
	 */
	public int getProcedure() {
		return procedure;
	}

	/**
	 * @param procedure the procedure to set
	 */
	public void setProcedure(int procedure) {
		this.procedure = procedure;
	}

	/**
	 * @param evaluationMode the evaluationMode to set
	 */
	public void setEvaluationMode(int evaluationMode) {
		this.evaluationMode = evaluationMode;
	}

	/**
	 * @return the evaluationMode
	 */
	public int getEvaluationMode() {
		return evaluationMode;
	}
	
	/**
	 * @param coolingFactor the coolingFactor to set
	 */
	public void setCoolingFactor(int coolingFactor) {
		this.coolingFactor = coolingFactor;
	}
	
	/**
	 * @return the coolingFactor
	 */
	public int getCoolingFactor(){
		return this.coolingFactor;
	}
	
	public double getDoubCoolingFactor() {
		switch(this.coolingFactor){
		case 0:
			return 0.01;
		case 1:
			return 0.05;
		case 2:
			return 0.1;
		case 3:
			return 0.8;
		case 4:
			return 0.95;
		}
		return 0.99;
	}

	/**
	 * @param graspMode the graspMode to set
	 */
	public void setGraspMode(int graspMode) {
		this.graspMode = graspMode;
	}

	/**
	 * @return the graspMode
	 */
	public int getGraspMode() {
		return graspMode;
	}
	
	public void setTabuTenure(int tenure) {
		this.tabutenure = tenure;
	}
	
	public int getTabuTenure() {
		return tabutenure;
	}
}
