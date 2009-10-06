package rpp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;

/**
 * TODO Descripcion detallada de la clase.
 * 
 * @author Castor Miguel Perez Melian
 * @author Alberto Cabrera Perez
 * @author Javier Luis Moreno Villena
 * @author Alejandro Tejera Perez
 * @version 1.0a
 * @since 1.0
 */
public class Problem {
	
	/**
	 * Atributo rectangles: rectangulos a empaquetar en el problema.
	 */
	private Rectangle[] rectangles;
	
	/**
	 * Lista de puntos factibles en los que colocar un rectangulo.
	 */
	private ArrayList<Point> points;
	
	public Problem(String fileName) {
		this.rectangles = readFile(fileName);
		points.clear();
	}
	
	/**
	 * Metodo readFile: Se le pasa por parametro una ruta absoluta de fichero
	 * con formato para leer el Rectangular Packing Problem y extrae la
	 * informacion necesaria que define los rectangulos a utilizar en dicho
	 * problema y a partir de dicha informacion devuelve un array de objetos
	 * Rectangulo de tamano "n" (ver a continuacion) y con "n" rectangulos de
	 * las dimensiones especificadas por "bX" y "hX". El formato de fichero sera
	 * tal que asi:
	 * 
	 * n
	 * b1 h1
	 * b2 h2
	 * b3 h3
	 * ...
	 * 
	 * Siendo "n" el numero de rectangulos del problema, "bX" la base del
	 * rectangulo X y "hX" la altura del rectangulo X.
	 * 
	 * @param fileName
	 *            Nombre del fichero a leer
	 * @return Array de objetos Rectangulo
	 */
	public static Rectangle[] readFile(String fileName) {
		FileInputStream myStream = null;
		InputStreamReader myReader = null;
		StreamTokenizer myTokenizer = null;
		try {
			myStream = new FileInputStream(fileName);
			myReader = new InputStreamReader(myStream);
			myTokenizer = new StreamTokenizer(myReader);
			myTokenizer.nextToken();
			int n = (int) myTokenizer.nval;
			Rectangle [] rectangles = new Rectangle[n];
			for (int i = 0; i < n; i++) {
				Rectangle r = new Rectangle();
				myTokenizer.nextToken();
				int b = (int) myTokenizer.nval;
				myTokenizer.nextToken();
				int h = (int) myTokenizer.nval;
				r.setBase(b);
				r.setHeight(h);
				rectangles[i] = r;
			}
			return rectangles;

		} catch (NegativeArraySizeException e) {
			System.out.println(e + " (El array tiene tamano negativo, cambie la n del fichero).");
			System.exit(1);
		} catch (IOException e) {
			System.out.println(e);
			System.exit(1);
		} finally {
			try {
				myStream.close();
				myReader.close();
			} catch (IOException e) {
				System.out.println(e);
				System.exit(1);
			}
		}

		return new Rectangle[0];
	}
	
	/**
	 * Anade un punto a la lista de puntos factibles.
	 * 
	 * @param p
	 *            Punto a anadir.
	 */
	public void addPoint(Point p) {
		points.add(p);
	}

	/**
	 * TODO Decidir criterio para elegir que punto es mejor.
	 * 
	 * @return Punto donde colocar el rectangulo.
	 */
	public Point getPoint(Point p) {
		Point toRet = new Point();

		if (points.contains(p)) {
			toRet = points.remove(points.indexOf(p));
		}

		return toRet;
	}
}
