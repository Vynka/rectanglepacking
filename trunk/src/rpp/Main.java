package rpp;

import java.io.*;

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
public class Main {

	/**
	 * Método readFile: Se le pasa por parámetro una ruta absoluta de fichero
	 * con formato para leer el Rectangular Packing Problem y extrae la
	 * información necesaria que define los rectángulos a utilizar en dicho
	 * problema y a partir de dicha información devuelve un array de objetos
	 * Rectángulo de tamaño "n" (ver a continuación) y con "n" rectángulos de
	 * las dimensiones especificadas por "bX" y "hX". El formato de fichero será
	 * tal que así:
	 * 
	 * n b1 h1 b2 h2 b3 h3 ...
	 * 
	 * Siendo "n" el número de rectángulos del problema, "bX" la base del
	 * rectángulo X y "hX" la altura del rectángulo X.
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
			Rectangle[] rectangles;
			int n;
			myStream = new FileInputStream(fileName);
			myReader = new InputStreamReader(myStream);
			myTokenizer = new StreamTokenizer(myReader);
			myTokenizer.nextToken();
			n = (int) myTokenizer.nval;
			rectangles = new Rectangle[n];
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
					+ " (El array tiene tamaño negativo, cambie la n del fichero).");
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
	 * Método main Método para pruebas y utilización general.
	 * 
	 * @param args
	 *            Argumentos de la línea de comands
	 */
	public static void main(String[] args) {
		Rectangle[] recs = readFile("../../test.dat");
		for (int i = 0; i < recs.length; i++) {
			System.out.println(recs[i].toString());
		}
	}
}
