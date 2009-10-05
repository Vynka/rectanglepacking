package rpp;

/**
 * Representa una solucion del problema 'RPP' con el fin de poder ser 
 * manipulada posteriormente.
 * Esta compuesta por las dimensiones del rectangulo solucion y una serie
 * de vectores que indican el modo de posicionar los rectangulos.
 * 
 * TODO Modo de comunicar la clase solucion con la heuristica de colocacion.
 *      Una posibilidad, que esta clase contenga a la propia 
 *      clase 'HeuristicaDeColocacion'. 
 * TODO Contemplar la posibilidad de tener el area de los rectangulos hallada
 *      previamente. (Una posible clase que contenga al vector de rectangulos
 *      con su constructor y el area que ocupan conjuntamente (caso optimo del
 *      problema)).
 * TODO Ver como relacionar el vector de rectangulos con la clase.  
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
	 * TODO ¿Es mejor considerar INF un valor negativo que careceria de sentido?
	 *      EJ: -1.
	 */
	final int INF = 999999999;
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
	 * Atributo fObj: valor de la funcion objetivo del problema.
	 * f(x) = area - sum(rectangle[i].h * rectangle[i].b);
	 *    con 0 <= i < n y rectangle un vector de rectangulos que componen
	 *    el problema.
	 */
	private int fObj;
	/**
	 * Atributo orden: array que indica la colocacion de los rectangulos
	 * usando el indice i para indicar cuando fue puesto el rectangulo
	 * orden[i].
	 */
	int [] orden;
	/**
	 * Atributo pos: array que indica la colocacion de los rectangulos
	 * atribuyendo a cada rectangulo i la posicion pos[i].
	 */
	int [] pos;
	/**
	 * @return la base del rectangulo.
	 */
	public int getB() {
		return b;
	}
	/**
	 * @param b la altura del rectangulo.
	 */
	public void setB(int b) {
		this.b = b;
	}
	/**
	 * @return la altura.
	 */
	public int getH() {
		return h;
	}
	/**
	 * @param h altura a establecer.
	 */
	public void setH(int h) {
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
	 * @return el array de indices
	 */
	public int[] getOrden() {
		return orden;
	}
	/**
	 * @param orden el array de indices a establecer.
	 */
	public void setOrden(int[] orden) {
		this.orden = orden;
	}
	/**
	 * @return el valor de la posicion i en el array de indices.
	 */
	public int getOrden(int i) {
		return orden[i];
	}
	/**
	 * @return el array de orden de posicion de los rectangulos.
	 */
	public int[] getPos() {
		return pos;
	}
	/**
	 * @return el valor de la posicion i en el array de orden de posicion.
	 */
	public int getPos(int i) {
		return pos[i];
	}
	/**
	 * @param pos array de orden del 
	 */
	public void setPos(int[] pos) {
		this.pos = pos;
	}
	/**
	 * Por defecto pone todo a 0 o null (aunque no hace falta) a excepcion
	 * de la funcion objetivo que vale INF.
	 * Dado que el problema consiste en minimizar, asi se evita un posible
	 * error de que la mejor solucion valga 0.
	 */
	public Solution() {
		this.b = 0;
		this.h = 0;
		this.area = 0;
		this.fObj = INF;
		this.orden = null;
		this.pos = null;
	}
	/**
	 * Construye la solucion directamente con los valores obtenidos.
	 * @param b base del rectangulo solucion.
	 * @param h altura del rectangulo solucion.
	 * @param areaRec area de los rectangulos.
	 * @param orden vector con el orden de insercion de los rectangulos.
	 * @param permType numero que define la representacion en el array.
	 */
	public Solution(int b, int h, 
			        int areaRec, int [] array, int permType) {
		this.b = b;
		this.h = h;
		this.area = b * h;
		this.fObj = this.area - areaRec;
		// permType = 1 -> el array introducido es de Orden.
		if (permType == 1) {
			this.orden = array;
			for (int i = 0; i < orden.length; i++) {
				this.pos[orden[i]] = i;
			}
		// permType = 2 -> el array introducido es de Pos.
		} else if (permType == 2) {
			this.pos = array;
			for (int i = 0; i < pos.length; i++) {
				this.orden[pos[i]] = i;
			}
		}
	}
	/**
	 * TODO Funcion/es para hallar una solucion vecina.
	 * @param b nueva base del rectangulo contenedor.
	 * @param h nueva altura del rectangulo contenedor.
	 * @param i indice del primer rectangulo a intercambiar.
	 * @param j indice del segundo rectangulo a intercambiar. 
	 */
	public Solution solucionVecina(int b, int h,int i, int j) {
		Solution newOne = null;
		/*
		 * EJEMPLO
		 * Intercambia el rectangulo i por el rectangulo j en el vector
		 * de posiciones. Se actualiza la funcion objetivo (fObj) actual y 
		 * el otro vector de posicionamiento.
		 */
		return newOne;
	}
}
