package rpp;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Marco principal de la GUI, que servira como objeto de comunicacion entre la GUI y las clases para
 * la resolucion del problema, ademas de ser el contenedor principal y gestor de eventos.
 * 
 * @author Castor Miguel Perez Melian
 * @author Alberto Cabrera Perez
 * @author Javier Luis Moreno Villena
 * @author Alejandro Tejera Perez
 * @author Isaac Galan Estarico
 * @version 1.0
 * @since 1.0
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame implements ActionListener {
        /**
         * Atributo dp: Panel donde se representara la solucion. 
         */
        private DrawPanel dp;
        
        /**
         * Barra de menu del programa.
         */
        private OptionsMenu om;


        
        /**
         * Constructor por defecto, en el que se aniaden los objetos de la GUI y se le ajustan los parametros.
         */
        public MainFrame() {
                this.setDefaultCloseOperation(EXIT_ON_CLOSE);
                this.setTitle("RPP Solver");
                
                Toolkit kit = Toolkit.getDefaultToolkit();
                Dimension screenDim = kit.getScreenSize();

                // Se establece la posicion del marco
                int width = (screenDim.width / 2);
                int height = (screenDim.height / 2);
                this.setSize(width, height);
                this.setLocation(width / 2, height / 2);
                
                om = new OptionsMenu();
                this.add(om, BorderLayout.NORTH);
                
                dp = new DrawPanel();
                this.add(dp, BorderLayout.CENTER);
                
                //Testeo
                Problem r = new Problem("test2.dat");
        		System.out.println(r);
        		Heuristica h = new Heuristica(r);
        		System.out.println("La solucion inicial es -> " + r.getSolution());
        		/*h.multistartSearch(5, Heuristica.SWAP_WITH_LAST, Heuristica.ANXIOUS_SAMPLING, Solution.RANDOM, Heuristica.OUT_UNLESS_BETTER);
        		System.out.println("La solucion del metodo MSS con busqueda ANXIOUS es -> " + r.getSolution());*/
        		/*h.pureRandomSearch(5000000, Heuristica.NUMBER_OF_TIMES);
        		System.out.println("La solucion del metodo PRS es -> " + r.getSolution());*/
        		h.localSearch(Heuristica.ONE_SWAP, Heuristica.GREEDY_SAMPLING, Solution.RANDOM);
        		System.out.println("La solucion del metodo LS con muestreo GREEDY es -> " + r.getSolution());
                dp.setProblem(r);
        }

		public void actionPerformed(ActionEvent e) {
		}
}