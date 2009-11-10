package rpp;


/**
 * Concepto abstracto para la eleccion del metodo a realizar en la heuristica.
 * @author Haside
 * @version 1.01.04
 * @since 1.01.04
 * @see Heuristica
 */
public class HeuristicOptions {
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
	
	/**
	 * StopCritera: Criterio de parada del problema
	 */
	private int stopCriteria;
	
	/**
	 * Neighbors: Espacio de entorno. (Estructura de entorno) 
	 */
	private int neighbors;
	
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
	 * Constructor por defecto: PSR con OUT_UNLESS_BETTER
	 */
	public HeuristicOptions(String fileName) {
		this.stopCriteria = 0;
		this.neighbors = 0;
		this.sampling = 0;
		this.procedure = 0;
		this.fileName = fileName;
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
	
	
	
}
