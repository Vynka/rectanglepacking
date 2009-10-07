package rpp;

/**
 * Representa una solucion del problema 'RPP' con el fin de poder ser manipulada
 * posteriormente. Esta compuesta por las dimensiones del rectangulo solucion y
 * una serie de vectores que indican el modo de posicionar los rectangulos.
 * 
 * @author Castor Miguel Perez Melian
 * @author Alberto Cabrera Perez
 * @author Javier Luis Moreno Villena
 * @author Alejandro Tejera Perez
 * @version 1.0a
 * @since 1.0
 */

public class Solution {

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
	 * Atributo fObj: valor de la funcion objetivo del problema. f(x) = area -
	 * sum(rectangle[i].h * rectangle[i].b); con 0 <= i < n y rectangle un vector
	 * de rectangulos que componen el problema.
	 */
	private int fObj;

	/**
	 * Atributo orden: array que indica la colocacion de los rectangulos usando el
	 * indice i para indicar cuando fue puesto el rectangulo orden[i].
	 */
	private int[] orden;

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
	public int getFObj() {
		return fObj;
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
	 * Por defecto pone todo a 0 o null (aunque no hace falta) a excepcion de la
	 * funcion objetivo que vale INF. Dado que el problema consiste en minimizar,
	 * asi se evita un posible error de que la mejor solucion valga 0.
	 */
	public Solution() {
		this.b = 0;
		this.h = 0;
		this.area = 0;
		this.fObj = INF;
		this.orden = null;
	}

	/**
	 * Construye la solucion directamente con los valores obtenidos.
	 * 
	 * @param b
	 *          base del rectangulo solucion.
	 * @param h
	 *          altura del rectangulo solucion.
	 * @param areaRec
	 *          area de los rectangulos.
	 * @param orden
	 *          permutacion de los rectangulos
	 */
	public Solution(int b, int h, int areaRec, int [] orden) {
		this.b = b;
		this.h = h;
		this.area = b * h;
		this.fObj = this.area - areaRec;
		this.orden = orden;
	}

	/**
	 * @param b
	 *          nueva base del rectangulo contenedor.
	 * @param h
	 *          nueva altura del rectangulo contenedor.
	 * @param i
	 *          indice del primer rectangulo a intercambiar.
	 * @param j
	 *          indice del segundo rectangulo a intercambiar.
	 */
	public Solution solucionVecina(int b, int h, int i, int j) {
		Solution newOne = null;
		/*
		 * EJEMPLO Intercambia el rectangulo i por el rectangulo j en el vector de
		 * posiciones. Se actualiza la funcion objetivo (fObj) actual y el otro
		 * vector de posicionamiento.
		 */
		return newOne;
	}
}
