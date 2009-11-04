package rpp;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class PropertiesDialog extends JDialog implements ActionListener {
	
	//Aqui deberia ir una referencia a un objeto Parametros que seria el que guardase los parametros dados por el usuario
	//y que seria enviado por MainFrame a Heuristica.
	JButton acceptbut = new JButton("Aceptar");
	JButton cancelbut = new JButton("Cancelar");
	JLabel blaolbl = new JLabel("Parametros aqui y eso.");
	
	public PropertiesDialog(MainFrame owner) {
		super(owner, "Properties", true);
		this.setSize(200, 200);
		this.setLocation(owner.getLocation());
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLayout(new FlowLayout());
		
		this.add(blaolbl);
		acceptbut.setActionCommand("aceptar");
		acceptbut.addActionListener(this);
		this.add(acceptbut);
		cancelbut.setActionCommand("cancelar");
		cancelbut.addActionListener(this);
		this.add(cancelbut);
		
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "aceptar") {
			System.out.println("Choy guay");
			this.dispose();
		}
		else if(e.getActionCommand() == "cancelar") {
			System.out.println("Po vale");
			this.dispose();
		}
	}
}
