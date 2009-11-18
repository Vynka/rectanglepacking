package GUI;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import rpp.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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
         * Parametros para la heuristica.
         */
        HeuristicOptions hop;
        
        /**
         * Boton que activa el dibujo de los rectangulos.
         */
        private JButton shbut = new JButton ("Show");
        
        /**
         * Boton que manda a calcular el resultado del problema.
         */
        private JButton calcbut = new JButton("Calculate");
        
        private JLabel fobjlbl = new JLabel("F Obj = ");
        private JLabel areareclbl = new JLabel("Area rec = ");
        
        /**
         * Selector de archivo.
         */
        private final JFileChooser fc = new JFileChooser("./datasets");
        
        /**
         * Referencia a la clase Problem. 
         */
        private Problem r = null;
        
        /**
         *  Referencia a la clase Heuristica.
         */
        private Heuristica h = null;
        
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
                
            JPanel southpan = new JPanel();
            southpan.setLayout(new BoxLayout(southpan, BoxLayout.X_AXIS));
            this.add(southpan, BorderLayout.SOUTH);
            
            JPanel lblpan = new JPanel();
            lblpan.setLayout(new BoxLayout(lblpan, BoxLayout.Y_AXIS));
            lblpan.add(fobjlbl);
            lblpan.add(areareclbl);
            southpan.add(lblpan);
            
            southpan.add(Box.createHorizontalGlue());
            
            southpan.add(calcbut);
            calcbut.setActionCommand("calc");
            calcbut.addActionListener(this);
            
            southpan.add(shbut);
            shbut.setActionCommand("show");
            shbut.addActionListener(this);
            
            southpan.add(Box.createHorizontalGlue());
            
            ScalePanel sp = new ScalePanel(this);
            southpan.add(sp);
            
            hop = new HeuristicOptions("");
            pd = new PropertiesDialog(this);
            dp.setProblem(r);
        }

        
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand() == "properties") {
				pd.setVisible(true);
				dp.repaint();
			}
			else if (e.getActionCommand() == "close") {
				System.exit(0);
			}
			else if (e.getActionCommand() == "file") {
				int returnVal = fc.showOpenDialog(this);
		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            hop.setFileName(file.getAbsolutePath());
		            r = new Problem(file.getAbsolutePath());
		            h = new Heuristica(r);
		            dp.setProblem(r);
		        }
			}
			else if (e.getActionCommand() == "show") {
				if (hop.getFileName() != "") {
					dp.setShowable(true);
					dp.repaint();
				}
				else {
					System.out.println("File not selected");
				}
			}
			else if (e.getActionCommand() == "calc") {
				if (hop.getFileName() != "") {
					h.callProcedure(hop);
					Solution sol = r.getSolution();
					fobjlbl.setText("F Obj = " + sol.getObjF());
					areareclbl.setText("Area rec = " + sol.getArea());
				}
				else {
					System.out.println("File not selected.");
				}
			}
		}
		
		public HeuristicOptions getOptions() {
			return hop;
		}
		
		public void setOptions(HeuristicOptions h) {
			hop = h;
		}
		
		void setScaleDraw(double x, double y) {
			dp.setScale(x, y);
			dp.repaint();
		}
}