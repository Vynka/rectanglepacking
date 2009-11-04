package rpp;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class FileDialog extends JDialog implements ActionListener {
	
	JTextField filename = new JTextField("", 20);
	JButton acceptbut = new JButton("Aceptar");
	JButton cancelbut = new JButton("Cancelar");
	
	public FileDialog(MainFrame owner) {
		super(owner, "File", true);
		this.setSize(300, 100);
		this.setLocation(owner. getLocation());
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLayout(new FlowLayout());
		
		this.add(filename);
		acceptbut.setActionCommand("fdaccept");
		acceptbut.addActionListener(this);
		acceptbut.addActionListener(owner);
		this.add(acceptbut);
		cancelbut.setActionCommand("cancel");
		cancelbut.addActionListener(this);
		this.add(cancelbut);
		
		this.setVisible(true);
	}

	
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "fdaccept") {
			System.out.println(filename.getText());
			this.dispose();
		}
		else if(e.getActionCommand() == "cancel") {
			System.out.println("Po vale");
			this.dispose();
		}
	}
}
