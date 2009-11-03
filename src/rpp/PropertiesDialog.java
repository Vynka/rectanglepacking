package rpp;

import javax.swing.JButton;
import javax.swing.JDialog;

@SuppressWarnings("serial")
public class PropertiesDialog extends JDialog {
	
	//Aqui deberia ir una referencia a un objeto Parametros que seria el que guardase los parametros dados por el usuario
	//y que seria enviado por MainFrame a Heuristica.
	
	public PropertiesDialog(MainFrame owner) {
		super(owner, "Properties", true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
}
