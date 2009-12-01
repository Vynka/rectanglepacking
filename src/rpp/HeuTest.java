package rpp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HeuTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Problem p = new Problem ("./datasets/C4/2.dat");
		Heuristica h = new Heuristica(p, Heuristica.WASTE_EVAL);
		
		long meanT = 0;
		long meanFobj = 0;
		double meanWaste = 0;
		
		long t;
		final int PRUEBAS = 100;
		File f = null;
		FileWriter obs = null;
		BufferedWriter bw = null;
		try {
			f = new File("/home/alu3774/Pruebitatuntuncas.cas");
		    obs = new FileWriter(f);
		    bw = new BufferedWriter(obs);
			for (int i = 0; i < PRUEBAS; i++) {
				t = - System.nanoTime();
				h.GRASP(Heuristica.POND_GRASP);
				h.localSearch(Heuristica.ONE_SWAP, Heuristica.ANXIOUS_SAMPLING, Heuristica.POND_EVAL);
				//Metodo guay a evaluar
				t += System.nanoTime();
				meanT = (meanT + t) / 2;
				meanFobj = (p.getSolution().getArea() + meanFobj) / 2;
				meanWaste = ((double)p.getSolution().getObjF() / (double)p.getAreaRec()
							+ meanWaste) / 2;
			}
			bw.newLine();
			bw.newLine();
			bw.newLine();
			bw.write(meanT / 1000000 + " Obj " + meanFobj + " Waste " + meanWaste);
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				
			} finally {
				try {
					obs.close();
				} catch (IOException e) {
				}
			}
		}
	}

}
