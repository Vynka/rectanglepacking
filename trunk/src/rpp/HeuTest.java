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
		Problem p = null;
		Heuristica h = null;
		long meanT = 0;
		long meanFobj = 0;
		double meanWaste = 0;
		
		long t;
		final int PRUEBAS = 200;
		File f = null;
		FileWriter obs = null;
		BufferedWriter bw = null;
		try {	
			
			f = new File("./Tests/SA_ANXIOUS_ONE_SWAP_02.txt");
		    obs = new FileWriter(f);
		    bw = new BufferedWriter(obs);
		    for (int q = 1; q <= 7; q++) {
			    for (int j = 1; j <= 3; j++) {
			    	String dataset = "./datasets/C" + q + "/" + j + ".dat";
					p = new Problem (dataset);
					h = new Heuristica(p, Heuristica.WASTE_EVAL);
					bw.write("+++++++++++++++++++++++++++++++++++++++++++");
					bw.newLine();
					bw.write(dataset + "   Optimal: " + p.getAreaRec());
					bw.newLine();
					bw.write("+++++++++++++++++++++++++++++++++++++++++++");
					bw.newLine();
					bw.write(PRUEBAS + " tests");
					bw.newLine();
	
					bw.write("+++ FIRST COOLING PLAN (LINEAL) +++");
					bw.newLine();
					meanT = 0;
					meanFobj = 0;
					meanWaste = 0;
					for (int i = 0; i < PRUEBAS; i++) {
						t = - System.nanoTime();
						h.simulatedAnnealingSearch(Heuristica.ONE_SWAP, Heuristica.ANXIOUS_SAMPLING, 0.95, 1.05,
											 Heuristica.AREA_EVAL);
						//h.localSearch(Heuristica.ONE_SWAP, Heuristica.ANXIOUS_SAMPLING, Heuristica.AREA_EVAL);
						//Metodo guay a evaluar
						t += System.nanoTime();
						meanT = (meanT + t) / 2;
						meanFobj = (p.getSolution().getArea() + meanFobj) / 2;
						meanWaste = ((double)p.getSolution().getObjF() / (double)p.getSolution().getArea()
									+ meanWaste) / 2;
					}
					bw.newLine();
					bw.write(meanT / 1000000 + " Obj " + meanFobj + " Waste " + meanWaste);
					System.out.println(dataset);
					System.out.println(meanT / 1000000 + " Obj " + meanFobj + " Waste " + meanWaste);
					bw.newLine();
					bw.write("------------------------------------------------------------");
					bw.newLine();
					
					bw.write("+++ SECOND COOLING PLAN (LUNDY & MEES) +++");
					bw.newLine();
					meanT = 0;
					meanFobj = 0;
					meanWaste = 0;
					for (int i = 0; i < PRUEBAS; i++) {
						t = - System.nanoTime();
						h.simulatedAnnealingSearch(Heuristica.ONE_SWAP, Heuristica.ANXIOUS_SAMPLING, 0.05, 1,
											 Heuristica.AREA_EVAL);
						//h.localSearch(Heuristica.ONE_SWAP, Heuristica.ANXIOUS_SAMPLING, Heuristica.AREA_EVAL);
						//Metodo guay a evaluar
						t += System.nanoTime();
						meanT = (meanT + t) / 2;
						meanFobj = (p.getSolution().getArea() + meanFobj) / 2;
						meanWaste = ((double)p.getSolution().getObjF() / (double)p.getSolution().getArea()
									+ meanWaste) / 2;
					}
					bw.newLine();
					bw.write(meanT / 1000000 + " Obj " + meanFobj + " Waste " + meanWaste);
					bw.newLine();
					bw.write("------------------------------------------------------------");
					bw.newLine();
					
					/*bw.write("+++ MIXED +++");
					bw.newLine();
					meanT = 0;
					meanFobj = 0;
					meanWaste = 0;
					for (int i = 0; i < PRUEBAS; i++) {
						t = - System.nanoTime();
						h.GRASP(Heuristica.MIXED_GRASP);
					h.localSearch(Heuristica.ONE_SWAP, Heuristica.ANXIOUS_SAMPLING, Heuristica.MIXED_EVAL);
						//Metodo guay a evaluar
						t += System.nanoTime();
						meanT = (meanT + t) / 2;
						meanFobj = (p.getSolution().getArea() + meanFobj) / 2;
						meanWaste = ((double)p.getSolution().getObjF() / (double)p.getSolution().getArea()
								+ meanWaste) / 2;
					}
					bw.newLine();
					bw.write(meanT / 1000000 + " Obj " + meanFobj + " Waste " + meanWaste);
					bw.newLine();
					bw.write("------------------------------------------------------------");
					bw.newLine();
					/*
					bw.write("+++ DIAGONAL +++");
					bw.newLine();
					meanT = 0;
					meanFobj = 0;
					meanWaste = 0;
					for (int i = 0; i < PRUEBAS; i++) {
						t = - System.nanoTime();
						h.GRASP(Heuristica.DIAGONAL_GRASP);
						//Metodo guay a evaluar
						t += System.nanoTime();
						meanT = (meanT + t) / 2;
						meanFobj = (p.getSolution().getArea() + meanFobj) / 2;
						meanWaste = ((double)p.getSolution().getObjF() / (double)p.getSolution().getArea()
								+ meanWaste) / 2;
					}
					bw.newLine();
					bw.write(meanT / 1000000 + " Obj " + meanFobj + " Waste " + meanWaste);
					bw.newLine();
					bw.write("------------------------------------------------------------");
					bw.newLine();
					*/
					/*bw.write("+++ WASTE +++");
					bw.newLine();
					meanT = 0;
					meanFobj = 0;
					meanWaste = 0;
					for (int i = 0; i < PRUEBAS; i++) {
						t = - System.nanoTime();
						h.GRASP(Heuristica.WASTE_GRASP);
						h.localSearch(Heuristica.ONE_SWAP, Heuristica.ANXIOUS_SAMPLING, Heuristica.WASTE_EVAL);
						//Metodo guay a evaluar
						t += System.nanoTime();
						meanT = (meanT + t) / 2;
						meanFobj = (p.getSolution().getArea() + meanFobj) / 2;
						meanWaste = ((double)p.getSolution().getObjF() / (double)p.getSolution().getArea()
								+ meanWaste) / 2;
					}
					bw.newLine();
					bw.write(meanT / 1000000 + " Obj " + meanFobj + " Waste " + meanWaste);
					bw.newLine();
					bw.write("------------------------------------------------------------");
					bw.newLine();
					
					bw.write("+++ POND +++");
					bw.newLine();
					meanT = 0;
					meanFobj = 0;
					meanWaste = 0;
					for (int i = 0; i < PRUEBAS; i++) {
						t = - System.nanoTime();
						h.GRASP(Heuristica.POND_GRASP);
						h.localSearch(Heuristica.ONE_SWAP, Heuristica.ANXIOUS_SAMPLING, Heuristica.POND_EVAL);
						//Metodo guay a evaluar
						t += System.nanoTime();
						meanT = (meanT + t) / 2;
						meanFobj = (p.getSolution().getArea() + meanFobj) / 2;
						meanWaste = ((double)p.getSolution().getObjF() / (double)p.getSolution().getArea()
								+ meanWaste) / 2;
					}
					bw.newLine();
					bw.write(meanT / 1000000 + " Obj " + meanFobj + " Waste " + meanWaste);
					bw.newLine();*/
					bw.write("------------------------------------------------------------");
					bw.newLine();
					bw.write("------------------------------------------------------------");
					bw.newLine();
					bw.write("------------------------------------------------------------");
					bw.newLine();
					bw.write("------------------------------------------------------------");
					bw.newLine();
					bw.write("------------------------------------------------------------");
					bw.newLine();
					
					System.out.println(dataset);
			    }
			}
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
