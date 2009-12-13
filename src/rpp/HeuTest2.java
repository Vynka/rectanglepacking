package rpp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HeuTest2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Problem p = null;
		Heuristica h = null;
		long meanT = 0;
		long meanFobj = 0;
		Solution best = null;
		double meanWaste = 0;
		
		long t;
		int PRUEBAS = 1;
		File f = null;
		FileWriter obs = null;
		BufferedWriter bw = null;

		
		try {
			
			f = new File("./Tests/PSR.txt");
		    obs = new FileWriter(f);
		    bw = new BufferedWriter(obs);

		    for (int q = 1; q <= 7; q++) {
			    for (int j = 1; j <= 3; j++) {
			    	String dataset = "./datasets/C" + q + "/" + j + ".dat";
					p = new Problem (dataset);
					h = new Heuristica(p, Heuristica.WASTE_EVAL);
										
					bw.newLine();
					bw.write("TIEMPO EN MILISEGUNDOS");
					bw.newLine();
					bw.write("+++++++++++++++++++++++++++++++++++++++++++");
					bw.newLine();
					bw.write(dataset + "   Optimal: " + p.getAreaRec());
					bw.newLine();
					bw.write("+++++++++++++++++++++++++++++++++++++++++++");
					bw.newLine();
					bw.write(PRUEBAS + " tests");
					bw.newLine();
	
					bw.write("+++ AREA +++");
					bw.newLine();
					meanT = 0;
					meanFobj = 0;
					meanWaste = 0; best = null;
					for (int i = 0; i < PRUEBAS; i++) {
						t = - System.currentTimeMillis();
					
						h.pureRandomSearch(1, Heuristica.NUMBER_OF_TIMES, Heuristica.AREA_EVAL);

						//Metodo guay a evaluar
						t += System.currentTimeMillis();
						meanT = (meanT + t) / 2;
						if (best == null) {
							best = p.getSolution().clone();
						} else if (p.getSolution().getArea() < best.getArea()) {
							best = p.getSolution().clone();
						}
						meanFobj = (p.getSolution().getArea() + meanFobj) / 2;
						meanWaste = ((double)p.getSolution().getObjF() / (double)p.getSolution().getArea()
									+ meanWaste) / 2;
					}
					bw.newLine();
					bw.write(meanT / 1000000 + " Area " + meanFobj + " Waste " + meanWaste);
					bw.newLine();
					
					bw.newLine();
					bw.write(best + "");
					bw.newLine();
					for (int i = 0; i < best.getOrder().length; i++) {
						bw.write(best.getOrder(i) + " ");
					}
					
					
					bw.newLine();
					bw.newLine();
					bw.write("------------------------------------------------------------");
					bw.newLine();					
					/*
					bw.write("+++ WASTE +++");
					bw.newLine();
					meanT = 0;
					meanFobj = 0;
					meanWaste = 0; best = null;
					for (int i = 0; i < PRUEBAS; i++) {
						t = - System.currentTimeMillis();
					
						h.GRASP(Heuristica.WASTE_GRASP);

						//Metodo guay a evaluar
						t += System.currentTimeMillis();
						meanT = (meanT + t) / 2;
						if (best == null) {
							best = p.getSolution().clone();
						} else if (p.getSolution().getArea() < best.getArea()) {
							best = p.getSolution().clone();
						}
						meanFobj = (p.getSolution().getArea() + meanFobj) / 2;
						meanWaste = ((double)p.getSolution().getObjF() / (double)p.getSolution().getArea()
									+ meanWaste) / 2;
					}
					bw.newLine();
					bw.write(meanT / 1000000 + " Area " + meanFobj + " Waste " + meanWaste);
					bw.newLine();
					bw.newLine();
					bw.write(best + "");
					bw.newLine();
					for (int i = 0; i < best.getOrder().length; i++) {
						bw.write(best.getOrder(i) + " ");
					}
					*/
					/*
					t = - System.currentTimeMillis();
					p.setSolution(best);
					h.localSearch(Heuristica.ONE_SWAP, Heuristica.ANXIOUS_SAMPLING, Heuristica.WASTE_EVAL);
					t += System.currentTimeMillis();
					
					bw.newLine();
					bw.write("Tiempo de LS: " + t);
					bw.newLine();
					bw.write(best + "");
					bw.newLine();
					for (int i = 0; i < best.getOrder().length; i++) {
						bw.write(best.getOrder(i) + " ");
					}
					bw.newLine();
					bw.newLine();
					 */
					
					bw.write("------------------------------------------------------------");
					bw.newLine();					
					bw.write("+++ MIXED +++");
					bw.newLine();
					meanT = 0;
					meanFobj = 0;
					meanWaste = 0; best = null;
					for (int i = 0; i < PRUEBAS; i++) {
						t = - System.currentTimeMillis();
					
						h.pureRandomSearch(1, Heuristica.NUMBER_OF_TIMES, Heuristica.MIXED_EVAL);

						//Metodo guay a evaluar
						t += System.currentTimeMillis();
						meanT = (meanT + t) / 2;
						if (best == null) {
							best = p.getSolution().clone();
						} else if (p.getSolution().getArea() < best.getArea()) {
							best = p.getSolution().clone();
						}
						meanFobj = (p.getSolution().getArea() + meanFobj) / 2;
						meanWaste = ((double)p.getSolution().getObjF() / (double)p.getSolution().getArea()
									+ meanWaste) / 2;
					}
					bw.newLine();
					bw.write(meanT / 1000000 + " Area " + meanFobj + " Waste " + meanWaste);
					bw.newLine();
					 
					bw.newLine();
					bw.write(best + "");
					bw.newLine();
					for (int i = 0; i < best.getOrder().length; i++) {
						bw.write(best.getOrder(i) + " ");
					}
					
					
					
					
					bw.write("------------------------------------------------------------");
					bw.newLine();
					
					bw.write("+++ POND +++");
					bw.newLine();
					meanT = 0;
					meanFobj = 0;
					meanWaste = 0; best = null; best = null;
					for (int i = 0; i < PRUEBAS; i++) {
						t = - System.currentTimeMillis();
					
						h.GRASP(Heuristica.POND_GRASP);
						//Metodo guay a evaluar
						t += System.currentTimeMillis();
						meanT = (meanT + t) / 2;
						if (best == null) {
							best = p.getSolution().clone();
						} else if (p.getSolution().getArea() < best.getArea()) {
							best = p.getSolution().clone();
						}
						meanFobj = (p.getSolution().getArea() + meanFobj) / 2;
						meanWaste = ((double)p.getSolution().getObjF() / (double)p.getSolution().getArea()
									+ meanWaste) / 2;
					}
					bw.newLine();
					bw.write(meanT / 1000000 + " Area " + meanFobj + " Waste " + meanWaste);
					bw.newLine();
					bw.newLine();
					bw.write(best + "");
					bw.newLine();
					for (int i = 0; i < best.getOrder().length; i++) {
						bw.write(best.getOrder(i) + " ");
					}
					
					p.setSolution(best);
					h.simulatedAnnealingSearch(Heuristica.ONE_SWAP, Heuristica.ANXIOUS_SAMPLING, 0.05, 1, Heuristica.POND_EVAL);
					
					bw.newLine();
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
