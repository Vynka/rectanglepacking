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
         * Dialogo que salta al clicar en Opciones -> Properties.
         */
        private PropertiesDialog pd = null;
        
        /**
         * Dialogo que salta al clicar en Opciones -> File...
         */
        private FileDialog fd = null;
        
        /**
         * Parametros para la heuristica.
         */
        HeuristicOptions hop;
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
                om.mProperties.setActionCommand("properties");
                om.mProperties.addActionListener(this);
                om.mExit.setActionCommand("close");
                om.mExit.addActionListener(this);
                om.mFile.setActionCommand("file");
                om.mFile.addActionListener(this);
                
                dp = new DrawPanel();
                this.add(dp, BorderLayout.CENTER);
                
                //Testeo
                Problem r = new Problem("datasets/C4/1.dat");
        		System.out.println(r);
        		Heuristica h = new Heuristica(r);
        		System.out.println("La solucion inicial es -> " + r.getSolution());
        		h.multistartSearch(5, Heuristica.ONE_SWAP, Heuristica.GREEDY_SAMPLING, Solution.RANDOM, Heuristica.OUT_UNLESS_BETTER);
        		System.out.println("La solucion del metodo MSS con busqueda GREEDY es -> " + r.getSolution());
        		//h.pureRandomSearch(500, Heuristica.NUMBER_OF_TIMES);
        		//System.out.println("La solucion del metodo PRS es -> " + r.getSolution());
        		//h.randomSearch(100, Heuristica.OUT_UNLESS_BETTER);
        		//System.out.println("La solucion del metodo RS -> " + r.getSolution());
                dp.setProblem(r);
        }

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand() == "properties") {
				pd = new PropertiesDialog(this);
			}
			else if (e.getActionCommand() == "close") {
				System.exit(0);
			}
			else if (e.getActionCommand() == "file") {
				fd = new FileDialog(this);
			}
		}
}