package rpp;

import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

/**
 * Representa una solucion del problema 'RPP' con el fin de poder ser manipulada
 * posteriormente. Esta compuesta por las dimensiones del rectangulo solucion y
 * una serie de vectores que indican el modo de posicionar los rectangulos.
 * 
 * @author Castor Miguel Perez Melian
 * @author Alberto Cabrera Perez
 * @author Javier Luis Moreno Villena
 * @author Alejandro Tejera Perez
 * @author Isaac Galan Estarico
 * @version 1.01.03
 * @since 1.0
 */

public class Solution {

	/**
	 * Constantes de tipos de generacion inicial
	 */
	static final int RANDOM = 0;
	static final int DETERMINISTIC1 = 1;
	static final int DETERMINISTIC2 = 2;
	static final int MIXED1 = 3;
	static final int MIXED2 = 4;
	
	/**
	 * Constante INFinito para inicializar la funcion objetivo.
	 */
	final int INF = Integer.MAX_VALUE;

	/**
	 * Atributo b: base del rectangulo solucion.
	 */
	private int b;

	/**
	 * Atributo h: altura del rectangulo solucion.
	 */
	private int h;

	/**
	 * Atributo area: area del rectangulo solucion.
	 */
	private int area;
	
	/**
	 * Area Ideal
	 */
	private int recArea;

	/**
	 * Atributo ObjF: valor de la funcion objetivo del problema. f(x) = area -
	 * sum(rectangle[i].h * rectangle[i].b); con 0 <= i < n y rectangle un vector
	 * de rectangulos que componen el problema.
	 */
	private int objF;

	/**
	 * Atributo Order: array que indica la colocacion de los rectangulos usando el
	 * indice i para indicar cuando fue puesto el rectangulo Order[i].
	 */
	private int[] order;

	/**
	 * Por defecto pone todo a 0 o null (aunque no hace falta) a excepcion de la
	 * funcion objetivo que vale INF. Dado que el problema consiste en minimizar,
	 * asi se evita un posible error de que la mejor solucion valga 0.
	 */
	public Solution() {
		this.b = 0;
		this.h = 0;
		this.area = 0;
		this.recArea = 0;
		this.objF = INF;
		this.order = null;
	}

	/**
	 * Construye la solucion directamente con los valores obtenidos.
	 * 
	 * @param b
	 *          base del rectangulo solucion.
	 * @param h
	 *          altura del rectangulo solucion.
	 * @param recArea
	 *          area de los rectangulos.
	 * @param order
	 *          permutacion de los rectangulos
	 */
	public Solution(int b, int h, int recArea, int [] order) {
		this.b = b;
		this.h = h;
		this.area = b * h;
		this.recArea = recArea;
		this.objF = this.area;
		this.order = order;
	}
	
	/**
	 * Construye la solucion directamente con los valores obtenidos.
	 * 
	 * @param recArea
	 *          area de los rectangulos.
	 * @param tipoInicializacion
	 *          Es un valor de alguna de las constantes dadas para el tipo de funcion inicial
	 */
	public Solution(int recArea, int initType, int size) {
		this.b = 0;
		this.h = 0;
		this.area = 0;
		this.recArea = recArea;
		this.objF = 0;
		switch (initType) {
		case RANDOM:
			randomInit(size);
			break;
		case DETERMINISTIC1:
			deterministicInit1(size);
			break;
		case DETERMINISTIC2:
			deterministicInit2(size);
			break;
		case MIXED1:
			mixedInit1(size);
			break;
		case MIXED2:
			mixedInit2(size);
			break;
		}
	}
	
	/**
	 * @return la base del rectangulo.
	 */
	public int getBase() {
		return b;
	}

	/**
	 * @param b
	 *          la altura del rectangulo.
	 */
	public void setBase(int b) {
		this.b = b;
		this.area = this.b * this.h;
	}

	/**
	 * @return la altura del rectangulo.
	 */
	public int getHeight() {
		return h;
	}

	/**
	 * @param h
	 *          altura a establecer.
	 */
	public void setHeight(int h) {
		this.h = h;
		this.area = this.h * this.b;
	}

	/**
	 * @return el area del rectangulo obtenido.
	 */
	public int getArea() {
		return area;
	}

	/**
	 * @param recArea
	 * 			area del rectangulo ideal
	 */
	public void setRecArea(int recArea) {
		this.recArea = recArea;
	}
	
	/**
	 * @return el valor de la funcion objetivo.
	 */
	public int getObjF() {
		return this.objF;
	}
	
	/**
	 * Establece el valor de la funciono objetivo
	 */
	public void setObjF() {
		this.objF = this.area; 
	}

	/**
	 * @param orden
	 *          el array de indices a establecer.
	 */
	public void setOrder(int[] order) {
		this.order = order;
	}

	/**
	 * @return el array de indices
	 */
	public int[] getOrder() {
		return order;
	}

	/**
	 * @return el valor de la posicion i en el array de indices.
	 */
	public int getOrder(int i) {
		return order[i];
	}
	
	/**
	 * Metodo estandar de impresion por pantalla
	 */
	public String toString() {
		return new String ("Funcion obj " + objF +" con base " + b + " y altura " + h);
	}
	
	/**
	 * @return Copia de la solucion actual
	 */
	public Solution clone() {
		Solution s = new Solution();
		s.setBase(this.b);
		s.setHeight(this.h);
		s.setOrder(order.clone());
		s.setRecArea(this.recArea);
		s.setObjF();
		return s;
	}
	
	/**
	 * Inicializa de manera aleatoria las soluciones. (Generacion aleatoria)
	 * @param size
	 * 			numero de rectangulos (Tamano de la solucion)
	 */
	
	private void randomInit(int size) {
		clear();
		ArrayList<Integer> toMix = new ArrayList<Integer>(0);
		Random generator = new Random(System.nanoTime());
		for (int i = 0; i < size; i++) {
			toMix.add(new Integer(i));
		}		
		Collections.shuffle(toMix, generator);
		Object[] toNormalInt = toMix.toArray();
		order = new int[toNormalInt.length];
		for (int i = 0; i < toNormalInt.length; i++) {
			order[i] = ((Integer)toNormalInt[i]).intValue();
		}
	}

	/**
	 * Inicializa de una manera concreta una solucion. Para el mismo conjunto
	 * N de rectangulos siempre devolvera la misma solucion inicial. Ordenado por Tamaño de areas
	 * @param size
	 * 			numero de rectangulos (Tamano de la solucion)
	 */
    private void deterministicInit1(int size) {
		clear();
		order = new int[size];
		for (int i = 0; i < order.length; i++) {
			order[i] = i;
		}
    }
    
	/**
	 * Inicializa de una manera concreta una solucion. Para el mismo conjunto
	 * N de rectangulos siempre devolvera la misma solucion inicial. Uno Grande uno pequeño
	 * @param size
	 * 			numero de rectangulos (Tamano de la solucion)
	 */
    private void deterministicInit2(int size) {
		clear();
		order = new int[size];
		for (int i = 0; i < (order.length / 2); i++) {
			order[2 * i] = i;
			order[2 * i + 1] = order.length - 1 - i;
		}
		if (order.length % 2 == 1) {
			order[order.length - 1] = (int)(order.length / 2);
		}
    }

    /**
     * Inicializa de manera mixta una solucion. Primero aplica un metodo deterministico
     * y luego aplica a continuacion un metodo aleatorio generando asi una solucion inicial
     * mixta
     * @param size
	 * 			numero de rectangulos (Tamano de la solucion)
     */
    private void mixedInit1(int size) {
		clear();
    	order = new int [size];
		for (int i = 0; i < order.length; i++) {
			order[i] = i;
		}
		for (int i = 0; (3 * i) < order.length ; i++) {
		  Random generator = new Random(System.nanoTime());
		  int j = generator.nextInt(3);
		  Heuristica.swap(order, i, j + i);
		}
    }

    /**
     * Inicializa de manera mixta una solucion. Primero aplica un metodo deterministico
     * y luego aplica a continuacion un metodo aleatorio generando asi una solucion inicial
     * mixta
     * @param size
	 * 			numero de rectangulos (Tamano de la solucion)
     */
    private void mixedInit2(int size) {
		clear();
    	order = new int [size];
    	int h = order.length / 8;
		for (int i = 0; i < order.length; i++) {
			order[i] = i;
		}
		for (int i = 0; (h * i) < order.length ; i++) {
		  Random generator = new Random(System.nanoTime());
		  int j = generator.nextInt(h);
		  Heuristica.swap(order, i, j + i);
		}
    }
    
    /**
     *  Reestablece la solucion.
     */   
    private void clear() {
		this.b = 0;
		this.h = 0;
		this.area = 0;
		this.objF = 0;
		this.order = null;
    }
    
    /**
     *  Reestablece la solucion.
     */   
    public void reset() {
		this.b = 0;
		this.h = 0;
		this.area = 0;
		this.objF = 0;
    }
	
}
