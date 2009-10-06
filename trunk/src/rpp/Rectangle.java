package rpp;

/**
 * Describe un rectangulo definido por su base (b) y su altura (h).
 * 
 * @author Castor Miguel Perez Melian
 * @author Alberto Cabrera Perez
 * @author Javier Luis Moreno Villena
 * @author Alejandro Tejera Perez
 * @version 1.0a
 * @since 1.0
 */
public class Rectangle {
	
	/**
	 * Atributo b: Medida en unidades de la base del rectangulo.
	 */
	private int b;
	
	/**
	 * Atributo h: Medida en unidades de la altura del rectangulo.
	 */
	private int h;
	
	/**
	 * Atributo a: Representa el area del rectangulo.
	 */
	private int a;
	
	/**
	 * Atributo p: Punto en el que se coloca el rectangulo.
	 */
	private Point p;
	
	/**
	 * Constructor por defecto: Construye un rectangulo de 0 x 0.
	 */
	public Rectangle() {
		this.b = 0;
		this.h = 0;
		this.a = 0;
	}

	/**
	 * Constructor: Recibe como parametros la base y la altura y construye un
	 * rectangulo de dichas dimensiones.
	 * 
	 * @param b
	 *            Medida en unidades de la base del rectangulo.
	 * @param h
	 *            Medida en unidades de la altura del rectangulo.
	 */
	public Rectangle(int b, int h) {
		this.b = b;
		this.h = h;
		this.a = b * h;
	}

	/**
	 * Setter b: Establece la medida de la base del rectangulo.
	 * 
	 * @param b
	 *            Medida en unidades de la base del rectangulo.
	 */
	public void setBase(int b) {
		this.b = b;
	}
	
	/**
	 * Getter b: Devuelve la base del rectangulo.
	 * 
	 * @return Medida en unidades de la base del rectangulo.
	 */
	public int getBase() {
		return this.b;
	}

	/**
	 * Setter h: Establece la medida de la altura del rectangulo.
	 * 
	 * @param h
	 *            Medida en unidades de la altura del rectangulo.
	 */
	public void setHeight(int h) {
		this.h = h;
	}

	/**
	 * Getter h: Devuelve la altura del rectangulo.
	 * 
	 * @return Medida en unidades de la altura del rectangulo.
	 */
	public int getHeight() {
		return this.h;
	}
	
	/**
	 * Getter a: Devuelve el area del rectangulo.
	 * @return Area del rectangulo en unidades.
	 */
	public int getArea() {
		return this.a;
	}

	/**
	 * Metodo getPosition: devuelve el punto en el que se encuentra localizado
	 * el rectangulo.
	 * @return El punto en el que se localiza el rectangulo (esquina inferior izquierda).
	 */
	public Point getPosition() {
		return this.p;
	}

	/**
	 * Metodo toString: Devuelve un String representativo de un rectangulo.
	 * 
	 * @return String representativo del rectangulo "(base, altura)".
	 */
	public String toString() {
		return new String("(" + this.b + ", " + this.h + ")");
	}
	
	/**
	 * Establece el punto del rectangulo (para poder acceder al p del Rectangle).
	 * @param p Punto a establecer.
	 */
	public void move(Point p) {
		this.p = p;
	}
}