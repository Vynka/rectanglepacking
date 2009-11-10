package GUI;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import rpp.HeuristicOptions;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class PropertiesDialog extends JDialog implements ActionListener {
	
	/**
	 * Botones para aceptar/cancelar cambios.
	 */
	JButton acceptbut = new JButton("Aceptar");
	JButton cancelbut = new JButton("Cancelar");
	/**
	 * Referencias a objetos de MainFrame.
	 */
	HeuristicOptions dialoghop = null;
	MainFrame dialogowner;
	/**
	 * Constantes para los ComboBox, sus indices deben coincidir con los valores de HeuristicOption
	 */
	final String[] neighborstrings = {"Random swap", "One swap", "Swap with last"};
	final String[] stopstrings = {"Out unless better", "Number of times"};
	final String[] samplingstrings = {"Greedy sampling", "Anxious sampling", "Random sampling", "No sampling"};
	final String[] searchstrings = {"Pure random seach", "Random search", "Local search", "Multistart with local search"};
	/**
	 * ComboBoxes para la seleccion de parametros de la heuristica.
	 */
	JComboBox neighborlist = new JComboBox(neighborstrings);
	JComboBox stoplist = new JComboBox(stopstrings);
	JComboBox samplinglist = new JComboBox(samplingstrings);
	JComboBox searchlist = new JComboBox(searchstrings);
	
	/**
	 * Constructor.
	 * @param owner
	 */
	public PropertiesDialog(MainFrame owner) {
		super(owner, "Properties", true);
		this.setSize(220, 260);
		this.setLocation(owner.getLocation());
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		dialoghop = owner.getOptions();
		dialogowner = owner;
		
		this.add(new JLabel("Espacios de entorno:"));
		this.add(neighborlist);
		neighborlist.setActionCommand("neighbor");
		neighborlist.addActionListener(this);
		
		this.add(new JLabel("Criterio de parada:"));
		this.add(stoplist);
		stoplist.setActionCommand("stop");
		stoplist.addActionListener(this);
		
		this.add(new JLabel("Muestreo del entorno:"));
		this.add(samplinglist);
		samplinglist.setActionCommand("sampling");
		samplinglist.addActionListener(this);
		
		this.add(new JLabel("Metodo de resolucion:"));
		this.add(searchlist);
		searchlist.setActionCommand("search");
		searchlist.addActionListener(this);
		
		acceptbut.setActionCommand("accept");
		acceptbut.addActionListener(this);
		this.add(acceptbut);
		cancelbut.setActionCommand("cancel");
		cancelbut.addActionListener(this);
		this.add(cancelbut);
		
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "accept") {
			dialogowner.setOptions(dialoghop);
			this.dispose();
		}
		else if(e.getActionCommand() == "cancel") {
			this.dispose();
		}
		else if(e.getActionCommand() == "neighbor") {
			System.out.println("Entorno: " + neighborlist.getSelectedIndex());
			dialoghop.setNeighbors(neighborlist.getSelectedIndex());
		}
		else if(e.getActionCommand() == "stop") {
			System.out.println("Parada: " + stoplist.getSelectedIndex());
			dialoghop.setStop(stoplist.getSelectedIndex());
		}
		else if(e.getActionCommand() == "sampling") {
			System.out.println("Sampling: " + samplinglist.getSelectedIndex());
			dialoghop.setSampling(samplinglist.getSelectedIndex());
		}
		else if(e.getActionCommand() == "search") {
			System.out.println("Search: " + searchlist.getSelectedIndex());
			dialoghop.setProcedure(searchlist.getSelectedIndex());
		}
	}
}
