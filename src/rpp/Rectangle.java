package rpp;

/**
 * [TODO] DESCRIPCION DETALLADA DE LA CLASE
 * 
 * @author Castor Miguel P�rez Melian
 * @author Alberto Cabrera P�rez
 * @author Javier Luis Moreno Villena
 * @author Alejandro Tejera P�rez
 * @version 1.0
 * @since 1.0
 */
public class Rectangle {
	/**
	 * Atributo b: Medida en unidades de la base del rect�ngulo.
	 */
	private int b;
	/**
	 * Atributo h: Medida en unidades de la altura del rect�ngulo.
	 */
	private int h;

	/**
	 * Constructor: Recibe como par�metros la base y la altura y construye un
	 * rect�ngulo de dichas dimensiones.
	 * 
	 * @param b
	 *            Medida en unidades de la base del rect�ngulo.
	 * @param h
	 *            Medida en unidades de la altura del rect�ngulo.
	 */
	public Rectangle(int b, int h) {
		this.b = b;
		this.h = h;
	}

	/**
	 * Constructor por defecto: Construye un rect�ngulo de 0 x 0.
	 */
	public Rectangle() {
		this.b = 0;
		this.h = 0;
	}

	/**
	 * Setter b: Establece la medida de la base del rect�ngulo.
	 * 
	 * @param b
	 *            Medida en unidades de la base del rect�ngulo.
	 */
	public void setBase(int b) {
		this.b = b;
	}

	/**
	 * Setter h: Establece la medida de la altura del rect�ngulo.
	 * 
	 * @param h
	 *            Medida en unidades de la altura del rect�ngulo.
	 */
	public void setHeight(int h) {
		this.h = h;
	}

	/**
	 * Getter b: Devuelve la base del rect�ngulo.
	 * 
	 * @return Medida en unidades de la base del rect�ngulo.
	 */
	public int getBase() {
		return this.b;
	}

	/**
	 * Getter h: Devuelve la altura del rect�ngulo.
	 * 
	 * @return Medida en unidades de la altura del rect�ngulo.
	 */
	public int getHeight() {
		return this.h;
	}

	/**
	 * M�todo toString: Devuelve un String representativo de un rect�ngulo.
	 * 
	 * @return String representativo del rect�ngulo "(base, altura)".
	 */
	public String toString() {
		return new String("(" + this.b + ", " + this.h + ")");
	}
}