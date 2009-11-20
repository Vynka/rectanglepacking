package GUI;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import rpp.HeuristicOptions;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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
	final String[] searchstrings = {"Pure random seach", "Random search", "Local search", "Multistart with local search",
									"Simulated annealing search"};
	final String[] initstrings = {"Random", "Deterministic 01", "Deterministic 02", "Mixed 01", "Mixed 02"};
	final String[] evaluationstrings = {"Waste", "Area", "BothSqrtN", "BothRandom"};
	
	/**
	 * ComboBoxes para la seleccion de parametros de la heuristica.
	 */
	JComboBox neighborlist = new JComboBox(neighborstrings);
	JComboBox stoplist = new JComboBox(stopstrings);
	JComboBox samplinglist = new JComboBox(samplingstrings);
	JComboBox searchlist = new JComboBox(searchstrings);
	JComboBox initlist = new JComboBox(initstrings);
	JComboBox evallist = new JComboBox(evaluationstrings);
	
	/**
	 * Campo de obtencion de las repeticiones
	 */
	JTextField timesField = new JTextField();
	
	/**
	 * Constructor.
	 * @param owner
	 */
	public PropertiesDialog(MainFrame owner) {
		super(owner, "Properties", true);
		this.setLayout(new GridLayout(0, 2));
		dialoghop = owner.getOptions();
		dialogowner = owner;
		
		this.add(new JLabel("Espacios de entorno:"));
		this.add(neighborlist);
		neighborlist.setActionCommand("neighbor");
		neighborlist.setSelectedIndex(dialoghop.getNeighbors());
		neighborlist.addActionListener(this);
		
		this.add(new JLabel("Criterio de parada:"));
		this.add(stoplist);
		stoplist.setActionCommand("stop");
		stoplist.setSelectedIndex(dialoghop.getStopCriteria());
		stoplist.addActionListener(this);
		
		this.add(new JLabel("Muestreo del entorno:"));
		this.add(samplinglist);
		samplinglist.setActionCommand("sampling");
		samplinglist.setSelectedIndex(dialoghop.getSampling());
		samplinglist.addActionListener(this);
		
		this.add(new JLabel("Metodo de resolucion:"));
		this.add(searchlist);
		searchlist.setActionCommand("search");
		searchlist.setSelectedIndex(dialoghop.getProcedure());
		searchlist.addActionListener(this);
		
		this.add(new JLabel("Tipo de inicializacion:"));
		this.add(initlist);
		initlist.setActionCommand("init");
		initlist.setSelectedIndex(dialoghop.getInitialization());
		initlist.addActionListener(this);
		
		this.add(new JLabel("Evaluacion:"));
		this.add(evallist);
		evallist.setActionCommand("eval");
		evallist.setSelectedIndex(dialoghop.getEvaluationMode());
		evallist.addActionListener(this);
		
		this.add(new JLabel("Numero de repeticiones: "));
		timesField.setSize(100, 50);
		timesField.setText((new Integer(dialoghop.getTimes())).toString());
		this.add(timesField);
		
		this.add(Box.createGlue());
		
		JPanel aux = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		acceptbut.setActionCommand("accept");
		acceptbut.addActionListener(this);
		aux.add(acceptbut);
		cancelbut.setActionCommand("cancel");
		cancelbut.addActionListener(this);
		aux.add(cancelbut);
		this.add(aux);
		
		this.pack();
		int x = (int)(owner.getLocation().getX() + (int)((owner.getWidth() - this.getWidth()) / 2)); 
		int y = (int)(owner.getLocation().getY() + (int)((owner.getHeight() - this.getHeight()) / 2));
		this.setLocation(x, y);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "accept") {
			dialoghop.setTimes(new Integer(timesField.getText()).intValue());
			this.setVisible(false);
		}
		else if(e.getActionCommand() == "cancel") {
			timesField.setText((new Integer(dialoghop.getTimes())).toString());
			this.setVisible(false);
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
		else if(e.getActionCommand() == "init") {
			System.out.println("Initialization: " + initlist.getSelectedIndex());
			dialoghop.setInitialization(initlist.getSelectedIndex());
		}
		else if(e.getActionCommand() == "eval") {
			System.out.println("Evaluacion: " + evallist.getSelectedIndex());
			dialoghop.setEvaluationMode(evallist.getSelectedIndex());
		}
	}
}
