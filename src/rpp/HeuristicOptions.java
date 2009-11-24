package rpp;


/**
 * Concepto abstracto para la eleccion del metodo a realizar en la heuristica.
 * @author Haside
 * @version 1.01.05
 * @since 1.01.04
 * @see Heuristica
 */
public class HeuristicOptions {
	
	/**
	 * Constantes de tipos de generacion inicial
	 */
	public static final int RANDOM = 0;
	public static final int DETERMINISTIC1 = 1;
	public static final int DETERMINISTIC2 = 2;
	public static final int MIXED1 = 3;
	public static final int MIXED2 = 4;
	
	/**
	 * Posibles espacios de entorno para soluciones vecinas.
	 */
	public static final int RANDOM_SWAP = 0;
	public static final int ONE_SWAP = 1;
	public static final int SWAP_WITH_LAST = 2;
	
	/**
	 * Criterios de parada
	 */
	public static final int OUT_UNLESS_BETTER = 0;
	public static final int NUMBER_OF_TIMES = 1;
	
	/**
	 * Tipos de muestreo del entorno
	 */
	public static final int GREEDY_SAMPLING = 0;
	public static final int ANXIOUS_SAMPLING = 1;
	public static final int RANDOM_SAMPLING = 2;
	public static final int NO_SAMPLING = 3;
	
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
	static final int WASTE_EVAL = 0;
	static final int AREA_EVAL = 1;
	static final int MIXED_EVAL = 2;
	static final int POND_EVAL = 3;
	
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
}
