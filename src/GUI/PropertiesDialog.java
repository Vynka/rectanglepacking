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
									"Simulated annealing search", "GRASP", "Tabu Search"};
	final String[] initstrings = {"Random", "Deterministic 01", "Deterministic 02", "Mixed 01", "Mixed 02"};
	final String[] evaluationstrings = {"Waste", "Area", "BothSqrtN", "Ponderated"};
	final String[] coolingFactorStrings = {"0.01","0.05","0.1","0.8","0.95","0.99"};
	final String[] graspstrings = {"Area", "Diagonal", "Mixed", "Ponderated", "Waste"};
	final String[] tabustrings = {"Constant", "Square root", "Simple Random Dinamic", "Frecuency based"};
	
	/**
	 * ComboBoxes para la seleccion de parametros de la heuristica.
	 */
	JComboBox neighborlist = new JComboBox(neighborstrings);
	JComboBox stoplist = new JComboBox(stopstrings);
	JComboBox samplinglist = new JComboBox(samplingstrings);
	JComboBox searchlist = new JComboBox(searchstrings);
	JComboBox initlist = new JComboBox(initstrings);
	JComboBox evallist = new JComboBox(evaluationstrings);
	JComboBox factorList = new JComboBox(coolingFactorStrings);
	JComboBox grasplist = new JComboBox(graspstrings);
	JComboBox tabulist = new JComboBox(tabustrings);
	
	/**
	 * Etiquetas de los ComboBoxes
	 */
	JLabel neighlbl = new JLabel("Espacios de entorno:");
	JLabel stoplbl = new JLabel("Criterio de parada:");
	JLabel samplinglbl = new JLabel("Muestreo del entorno:");
	JLabel evallbl = new JLabel("Evaluacion:");
	JLabel initlbl = new JLabel("Tipo de inicializacion:");
	JLabel factorlbl = new JLabel("Factor de enfriamiento:");
	JLabel timeslbl = new JLabel("Numero de repeticiones: ");
	JLabel grasplbl = new JLabel("Tipo de GRASP: ");
	JLabel tabulbl = new JLabel("Tipo de tabu tenure: ");
	
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
		
		this.add(new JLabel("Metodo de resolucion:"));
		this.add(searchlist);
		searchlist.setActionCommand("search");
		searchlist.setSelectedIndex(dialoghop.getProcedure());
		searchlist.addActionListener(this);
		
		this.add(neighlbl);
		neighlbl.setVisible(false);
		this.add(neighborlist);
		neighborlist.setActionCommand("neighbor");
		neighborlist.setSelectedIndex(dialoghop.getNeighbors());
		neighborlist.addActionListener(this);
		neighborlist.setVisible(false);
		
		this.add(stoplbl);
		stoplbl.setVisible(false);
		this.add(stoplist);
		stoplist.setActionCommand("stop");
		stoplist.setSelectedIndex(dialoghop.getStopCriteria());
		stoplist.addActionListener(this);
		stoplist.setVisible(false);
		
		this.add(samplinglbl);
		samplinglbl.setVisible(false);
		this.add(samplinglist);
		samplinglist.setActionCommand("sampling");
		samplinglist.addActionListener(this);
		samplinglist.setSelectedIndex(dialoghop.getSampling());
		samplinglist.setVisible(false);
		
		this.add(initlbl);
		initlbl.setVisible(false);
		this.add(initlist);
		initlist.setActionCommand("init");
		initlist.setSelectedIndex(dialoghop.getInitialization());
		initlist.addActionListener(this);
		initlist.setVisible(false);
		
		this.add(evallbl);
		evallbl.setVisible(false);
		this.add(evallist);
		evallist.setActionCommand("eval");
		evallist.setSelectedIndex(dialoghop.getEvaluationMode());
		evallist.addActionListener(this);
		evallist.setVisible(false);
		
		this.add(factorlbl);
		factorlbl.setVisible(false);
		this.add(factorList);
		factorList.setActionCommand("factor");
		factorList.setSelectedIndex(dialoghop.getCoolingFactor());
		factorList.addActionListener(this);
		factorList.setVisible(false);
		
		this.add(timeslbl);
		timeslbl.setVisible(false);
		timesField.setSize(100, 50);
		timesField.setText((new Integer(dialoghop.getTimes())).toString());
		this.add(timesField);
		timesField.setVisible(false);
		
		this.add(grasplbl);
		grasplbl.setVisible(false);
		this.add(grasplist);
		grasplist.setActionCommand("grasp");
		grasplist.setSelectedIndex(dialoghop.getGraspMode());
		grasplist.addActionListener(this);
		grasplist.setVisible(false);
		
		this.add(tabulbl);
		tabulbl.setVisible(false);
		this.add(tabulist);
		tabulist.setActionCommand("tabu");
		tabulist.setSelectedIndex(dialoghop.getTabuTenure());
		tabulist.addActionListener(this);
		tabulist.setVisible(false);
		
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
		displayOptions(searchlist.getSelectedIndex());
		int x = (int)(owner.getLocation().getX() + (int)((owner.getWidth() - this.getWidth()) / 2)); 
		int y = (int)(owner.getLocation().getY() + (int)((owner.getHeight() - this.getHeight()) / 2));
		this.setLocation(x, y);
	}
	
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "accept") {
			dialoghop.setTimes(new Integer(timesField.getText()).intValue());
			if ((neighborlist.getSelectedIndex() == HeuristicOptions.RANDOM_SWAP) &&
				(dialoghop.getSampling() != HeuristicOptions.RANDOM_SAMPLING)) {
				
				dialoghop.setSampling(HeuristicOptions.RANDOM_SAMPLING);
				samplinglist.setSelectedIndex(HeuristicOptions.RANDOM_SAMPLING);
				@SuppressWarnings("unused")
				FailDialog fd = new FailDialog(this, "Random swap only works with random sampling");
			}
			else if ((samplinglist.getSelectedIndex() == HeuristicOptions.RANDOM_SAMPLING) && 
					(neighborlist.getSelectedIndex() != HeuristicOptions.RANDOM_SWAP)) {
				
				dialoghop.setNeighbors(HeuristicOptions.RANDOM_SWAP);
				neighborlist.setSelectedIndex(HeuristicOptions.RANDOM_SWAP);
				@SuppressWarnings("unused")
				FailDialog fd = new FailDialog(this, "Random sampling only works with random swap");
			}
			this.setVisible(false);
		}
		else if(e.getActionCommand() == "cancel") {
			timesField.setText((new Integer(dialoghop.getTimes())).toString());
			this.setVisible(false);
		}
		else if(e.getActionCommand() == "neighbor") {
			dialoghop.setNeighbors(neighborlist.getSelectedIndex());
		}
		else if(e.getActionCommand() == "stop") {
			dialoghop.setStop(stoplist.getSelectedIndex());
		}
		else if(e.getActionCommand() == "sampling") {
			dialoghop.setSampling(samplinglist.getSelectedIndex());
		}
		else if(e.getActionCommand() == "search") {
			dialoghop.setProcedure(searchlist.getSelectedIndex());
			displayOptions(-1);
			displayOptions(searchlist.getSelectedIndex());
		}
		else if(e.getActionCommand() == "init") {
			dialoghop.setInitialization(initlist.getSelectedIndex());
		}
		else if(e.getActionCommand() == "eval") {
			dialoghop.setEvaluationMode(evallist.getSelectedIndex());
		}
		else if(e.getActionCommand() == "factor") {
			dialoghop.setCoolingFactor(factorList.getSelectedIndex());
		}
		else if(e.getActionCommand() == "grasp") {
			dialoghop.setGraspMode(grasplist.getSelectedIndex());
		}
		else if (e.getActionCommand() == "tabu") {
			dialoghop.setTabuTenure(tabulist.getSelectedIndex());
		}
	}
	
	private void displayOptions(int index) {
		switch (index) {
			case -1:
				neighlbl.setVisible(false);
				neighborlist.setVisible(false);
				samplinglbl.setVisible(false);
				samplinglist.setVisible(false);
				timeslbl.setVisible(false);
				timesField.setVisible(false);
				stoplbl.setVisible(false);
				stoplist.setVisible(false);
				initlbl.setVisible(false);
				initlist.setVisible(false);
				factorlbl.setVisible(false);
				factorList.setVisible(false);
				evallbl.setVisible(false);
				evallist.setVisible(false);
				grasplbl.setVisible(false);
				grasplist.setVisible(false);
				tabulbl.setVisible(false);
				tabulist.setVisible(false);
				break;
			case HeuristicOptions.PURE_RANDOM_SEARCH:
			case HeuristicOptions.RANDOM_SEARCH:
				timeslbl.setVisible(true);
				timesField.setVisible(true);
				stoplbl.setVisible(true);
				stoplist.setVisible(true);
				evallbl.setVisible(true);
				evallist.setVisible(true);
				break;
			case HeuristicOptions.MULTISTART_WITH_LOCAL_SEARCH:
				timeslbl.setVisible(true);
				timesField.setVisible(true);
				stoplbl.setVisible(true);
				stoplist.setVisible(true);
				initlbl.setVisible(true);
				initlist.setVisible(true);
			case HeuristicOptions.LOCAL_SEARCH:
				neighlbl.setVisible(true);
				neighborlist.setVisible(true);
				samplinglbl.setVisible(true);
				samplinglist.setVisible(true);
				evallbl.setVisible(true);
				evallist.setVisible(true);
				break;
			case HeuristicOptions.SIMULATED_ANNEALING_SEARCH:
				initlbl.setVisible(true);
				initlist.setVisible(true);
				neighlbl.setVisible(true);
				neighborlist.setVisible(true);
				samplinglbl.setVisible(true);
				samplinglist.setVisible(true);
				factorlbl.setVisible(true);
				factorList.setVisible(true);
				evallbl.setVisible(true);
				evallist.setVisible(true);
				break;
			case HeuristicOptions.GRASP:
				grasplbl.setVisible(true);
				grasplist.setVisible(true);
				break;
			case HeuristicOptions.TABU_SEARCH:
				tabulbl.setVisible(true);
				tabulist.setVisible(true);
				evallbl.setVisible(true);
				evallist.setVisible(true);
				break;
		}
	}
}
