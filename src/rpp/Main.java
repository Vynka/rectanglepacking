package rpp;

import java.io.*;

/**
 * Clase que tiene como objetivo la definición y resolución del problema conocido
 * como "Rectangle Packing Problem". El objetivo de dicho problema es empaquetar
 * un numero dado de rectángulos de dimensiones arbitrarias dentro de un
 * rectangulo, minimizando el area del envoltorio.
 * Para mas informacion: http://en.wikipedia.org/wiki/Packing_problem
 * 
 * @author Castor Miguel Perez Melian
 * @author Alberto Cabrera Perez
 * @author Javier Luis Moreno Villena
 * @author Alejandro Tejera Perez
 * @version 1.0a
 * @since 1.0
 */
public class Main {

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
			System.out.println(e.toString()
					+ " (El array tiene tamano negativo, cambie la n del fichero).");
			System.exit(1);
		} catch (IOException e) {
			System.out.println(e.toString());
			System.exit(1);
		} finally {
			try {
				myStream.close();
				myReader.close();
			} catch (IOException e) {
				System.out.println(e.toString());
				System.exit(1);
			}
		}

		return new Rectangle[0];
	}

	/**
	 * Metodo main Metodo para pruebas y utilizacion general.
	 * 
	 * @param args
	 *            Argumentos de la linea de comands
	 */
	public static void main(String[] args) {
		Rectangle[] recs = readFile("test.dat");
		for (int i = 0; i < recs.length; i++) {
			System.out.println(recs[i].toString());
		}
	}
}
