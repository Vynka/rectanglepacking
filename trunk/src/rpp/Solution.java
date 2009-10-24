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
 * @author Isaac Galán Estárico
 * @version 1.0a
 * @since 1.0
 */

public class Solution {

	/**
	 * Constantes de tipos de generacion inicial
	 */
	static final int RANDOM = 0;
	static final int DETERMINISTIC1 = 1; 
	static final int MIXED1 = 2;
	static final int MIXED2 = 3;
	
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
	 * Atributo ObjF: valor de la funcion objetivo del problema. f(x) = area -
	 * sum(rectangle[i].h * rectangle[i].b); con 0 <= i < n y rectangle un vector
	 * de rectangulos que componen el problema.
	 */
	private int ObjF;

	/**
	 * Atributo orden: array que indica la colocacion de los rectangulos usando el
	 * indice i para indicar cuando fue puesto el rectangulo orden[i].
	 */
	private int[] orden;

	/**
	 * Por defecto pone todo a 0 o null (aunque no hace falta) a excepcion de la
	 * funcion objetivo que vale INF. Dado que el problema consiste en minimizar,
	 * asi se evita un posible error de que la mejor solucion valga 0.
	 */
	public Solution() {
		this.b = 0;
		this.h = 0;
		this.area = 0;
		this.ObjF = INF;
		this.orden = null;
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
	 * @param orden
	 *          permutacion de los rectangulos
	 */
	public Solution(int b, int h, int recArea, int [] orden) {
		this.b = b;
		this.h = h;
		this.area = b * h;
		this.ObjF = this.area - recArea;
		this.orden = orden;
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
		this.ObjF = -recArea;
		switch (initType) {
		case RANDOM:
			this.orden = randomInit(size);
			break;
		case DETERMINISTIC1:
			this.orden = deterministicInit1(size);
			break;
		case MIXED1:
			this.orden = mixedInit1(size);
			break;
		case MIXED2:
			this.orden = mixedInit2(size);
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
		int oldarea = this.area;
		this.b = b;
		this.area = this.b * this.h;
		ObjF += this.area - oldarea;
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
		int oldarea = this.area;
		this.h = h;
		this.area = this.h * this.b;
		ObjF += this.area - oldarea;
	}

	/**
	 * @return el area del rectangulo obtenido.
	 */
	public int getArea() {
		return area;
	}

	/**
	 * @return el valor de la funcion objetivo.
	 */
	public int getObjF() {
		return ObjF;
	}

	/**
	 * @param orden
	 *          el array de indices a establecer.
	 */
	public void setOrden(int[] orden) {
		this.orden = orden;
	}

	/**
	 * @return el array de indices
	 */
	public int[] getOrden() {
		return orden;
	}

	/**
	 * @return el valor de la posicion i en el array de indices.
	 */
	public int getOrden(int i) {
		return orden[i];
	}
	
	/**
	 * Metodo estandar de impresion por pantalla
	 */
	public String toString() {
		return new String ("Funcion obj " + ObjF +" con base " + b + " y altura " + h);
	}
	
	public int[] randomInit(int size) {
		ArrayList<Integer> toMix = new ArrayList<Integer>(0);
		Random generator = new Random(System.currentTimeMillis());
		for (int i = 0; i < size; i++) {
			toMix.add(new Integer(i));
		}		
		Collections.shuffle(toMix, generator);
		Object[] toNormalInt = toMix.toArray();
		int[] toReturn = new int[toNormalInt.length];
		for (int i = 0; i < toNormalInt.length; i++) {
			toReturn[i] = ((Integer)toNormalInt[i]).intValue();
		}
		return toReturn;
	}


    public int[] deterministicInit1(int size) {
    	int[] toReturn = new int [size];
		for (int i = 0; i < toReturn.length; i++) {
			toReturn[i] = toReturn.length - i - 1;
		}
		return toReturn;
    }


    public int[] mixedInit1(int size) {
    	int[] toReturn = new int [size];
		return toReturn;
    }


    public int[] mixedInit2(int size) {
    	int[] toReturn = new int [size];
		return toReturn;
    }
	
}
