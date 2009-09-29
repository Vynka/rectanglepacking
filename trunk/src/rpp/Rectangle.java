package rpp;

/**
 * [TODO] DESCRIPCION DETALLADA DE LA CLASE
 * 
 * @author Castor Miguel Pérez Melian
 * @author Alberto Cabrera Pérez
 * @author Javier Luis Moreno Villena
 * @author Alejandro Tejera Pérez
 * @version 1.0
 * @since 1.0
 */
public class Rectangle {
	/**
	 * Atributo b: Medida en unidades de la base del rectángulo.
	 */
	private int b;
	/**
	 * Atributo h: Medida en unidades de la altura del rectángulo.
	 */
	private int h;

	/**
	 * Constructor: Recibe como parámetros la base y la altura y construye un
	 * rectángulo de dichas dimensiones.
	 * 
	 * @param b
	 *            Medida en unidades de la base del rectángulo.
	 * @param h
	 *            Medida en unidades de la altura del rectángulo.
	 */
	public Rectangle(int b, int h) {
		this.b = b;
		this.h = h;
	}

	/**
	 * Constructor por defecto: Construye un rectángulo de 0 x 0.
	 */
	public Rectangle() {
		this.b = 0;
		this.h = 0;
	}

	/**
	 * Setter b: Establece la medida de la base del rectángulo.
	 * 
	 * @param b
	 *            Medida en unidades de la base del rectángulo.
	 */
	public void setBase(int b) {
		this.b = b;
	}

	/**
	 * Setter h: Establece la medida de la altura del rectángulo.
	 * 
	 * @param h
	 *            Medida en unidades de la altura del rectángulo.
	 */
	public void setHeight(int h) {
		this.h = h;
	}

	/**
	 * Getter b: Devuelve la base del rectángulo.
	 * 
	 * @return Medida en unidades de la base del rectángulo.
	 */
	public int getBase() {
		return this.b;
	}

	/**
	 * Getter h: Devuelve la altura del rectángulo.
	 * 
	 * @return Medida en unidades de la altura del rectángulo.
	 */
	public int getHeight() {
		return this.h;
	}

	/**
	 * Método toString: Devuelve un String representativo de un rectángulo.
	 * 
	 * @return String representativo del rectángulo "(base, altura)".
	 */
	public String toString() {
		return new String("(" + this.b + ", " + this.h + ")");
	}
}