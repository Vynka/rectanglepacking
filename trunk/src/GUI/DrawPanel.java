package GUI;

import javax.swing.JPanel;

import rpp.Point;
import rpp.Problem;
import rpp.Rectangle;
import rpp.Solution;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;


@SuppressWarnings("serial")
public class DrawPanel extends JPanel {
	final double Y_OFFSET = -1; //Offset para arreglar la diferencia entre el (0, 0) de los rectangulos y el (0, 0) del panel.
	/**
	 * Atributo p: Referencia a un objeto Problem.
	 */
	private Problem p;
	
    /**
     * Atributo af: Objeto de tipo AffineTransform para situar el centro de coordenadas y la escala.
     */
    private AffineTransform af;
    
    /**
     * 
     */
    private boolean showable = false;
    
	/**
	 * Constructor por defecto: Establece como color de fondo el blanco.
	 */
	public DrawPanel() {
		setBackground(Color.WHITE);
	}
	
	/**
	 * Setter de r.
	 * @param r
	 */
	public void setProblem(Problem r) {
		p = r;
	}
	
	/**
	 * Setter de showable
	 * @param s
	 */
	public void setShowable(boolean s) {
		showable = s;
	}
	
	/**
	 * Metodo paintComponent: Dibuja los rectangulos de la solucion.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
        if (showable) {
	        Solution sol = p.getSolution();
	        //double x = this.getSize().getWidth();
	        double y = this.getSize().getHeight() + Y_OFFSET;
	        //double scalex = getWidth() / 500;
	        //double scaley = getHeight() / 500;
	        
	        af = new AffineTransform();
	        af.translate(0, y);
	        //af.scale(scalex, scaley); //Se situa el origen en la esquina inf izq y se escala al tamanio
	                                  //del rectangulo solucion
	        
	        //Mirar de cambiar la escala con botones + y -
	        g2.setTransform(af);
	        
	        g2.setStroke(new BasicStroke(0.2f));
	        
	        int[] orden = sol.getOrder();
	
	        for (int i = 0; i < p.getRectangleSize(); i++) {
	        	Rectangle rec = p.getRectangle(orden[i]);
	        	Point topLeft = rec.getTopLeft();
	        	
	        	Rectangle2D.Float r = new Rectangle2D.Float(topLeft.getX(), -topLeft.getY(), rec.getBase(), rec.getHeight());
	            g2.setPaint(Color.GRAY);
	        	g2.fill(r);
	        	g2.setPaint(Color.BLACK);
	        	g2.draw(r);
	        }
	        g2.setPaint(Color.RED);
	        Rectangle2D.Float rsol = new Rectangle2D.Float(0, -sol.getHeight(), sol.getBase(), sol.getHeight());
	        g2.draw(rsol);
        }
	}
}