package rpp;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;


public class DrawPanel extends JPanel {
	/**
	 * Atributo p: Referencia a un objeto Problem.
	 */
	private Problem p;
	/**
	 * Atributo af: Objeto de tipo AffineTransform para situar el centro de coordenadas y la escala.
	 */
	private AffineTransform af;
	
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
	 * Metodo paintComponent: Dibuja los rectangulos de la solucion.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		Solution sol = p.getSolution();
		int x = (int) this.getSize().getWidth();
		int y = (int) this.getSize().getHeight();
		int scalex = x / sol.getBase();
		int scaley = y / sol.getHeight();
		
		af = new AffineTransform();
		af.translate(0, scaley);
		af.scale(scalex, scaley); //Se situa el origen en la esquina inf izq y se escala al tamanio
								  //del rectangulo solucion
		
		g2.setTransform(af);
		
		int orden[] = sol.getOrder();
		int j;
		Rectangle2D.Float recsol = new Rectangle2D.Float();
		for (int i = 0; i < p.getRectangleSize(); i++) {
			j = orden[i];
			Point punto = p.getRectangle(j).getTopLeft(); //Se va cogiendo cada rectangulo por orden de la solucion.
			Rectangle2D.Float rec = new Rectangle2D.Float(punto.getX(), punto.getY(), p.getRectangle(j).getBase(), p.getRectangle(j).getHeight());
			recsol.add(rec); //Y se añade al rectangulo solucion, el resultado es el menor rectangulo que contenga a ambos.
		}
		
		g2.draw(recsol);
	}
}