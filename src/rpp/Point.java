package rpp;

import java.util.Comparator;

/**
 * . <- Eso es un punto (y tiene coordenadas ^^U)
 * 
 * @author Castor Miguel Perez Melian
 * @author Alberto Cabrera Perez
 * @author Javier Luis Moreno Villena
 * @author Alejandro Tejera Perez
 * @author Isaac Galan Estarico
 * @version 1.0a
 * @since 1.0
 */
public class Point implements Comparable<Point> {
	
	/**
	 * Atributo x: coordenada x del punto.
	 */
	private int x;
	/**
	 * Atributo y: coordenada y del punto.
	 */
	private int y;
	
	/**
	 * Constructor por defecto, establece el punto a las coordenadas (0, 0).
	 */
	public Point () {
		this.x = 0;
		this.y = 0;
	}
	
	/**
	 * Constructor de la clase Point, establece las coordenadas y crea un nuevo punto.
	 * @param x Coordenada x del punto.
	 * @param y Coordenada y del punto.
	 */
	public Point (int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Metodo setPoint: establece las coordenadas de un punto ya creado.
	 * @param x Coordenada x del punto.
	 * @param y Coordenada y del punto.
	 */
	public void setPoint (int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Getter de x: devuelve el valor x del punto.
	 * @return Coordenada x del punto.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Setter de x: establece el valor x del punto.
	 * @param x Coordenada x del punto.
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Getter de y: devuelve el valor y del punto.
	 * @return Coordenada y del punto.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Setter de y: establece el valor y del punto.
	 * @param y Coordenada y del punto.
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * Metodo toString: devuelve la cadena representativa del punto.
	 * @return Cadena representativa del punto.
	 */
	public String toString() {
		return new String("(" + getX() + ", " + getY() + ")");
	}

	/**
	 * Equals devuelve true si la instancia del obnjeto es igual a otra
	 * pasada por parametro.
	 * @param p
	 * 			punto con el que comparar
	 * @return ¿Son iguales?
	 */
	boolean equals(Point p) {
		return (this.x == p.getX()) && (this.y == getY());
	}

	/** Ordena segun el Eje X() */
	@Override
	public int compareTo(Point p) {
		if (this.x > p.getX()) {
			return 1;
		} else if (this.x < p.getX()) {
			return -1;
		} else {
			return 0;
		}
	}
}

class PointYComparator implements Comparator<Point> {

	@Override
	public int compare(Point p1, Point p2) {
		if (p1.getX() > p2.getX()) {
			return 1;
		} else if (p1.getX() < p2.getX()) {
			return -1;
		} else {
			return 0;
		}
	}
	
}

