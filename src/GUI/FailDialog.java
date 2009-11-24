package GUI;

import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class FailDialog extends JDialog implements ActionListener {
	private JButton acceptbutton = new JButton("Aceptar");
	private JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 10));

	//Constructor
	public FailDialog(Window owner, String mensaje) {
		super(owner, "Error");
		this.setModal(true);
		this.setLocation(owner.getLocation());
		this.setSize(350, 100);
		panel.add(new JLabel(mensaje));
		panel.add(acceptbutton);
		this.getContentPane().add(panel);
		acceptbutton.setActionCommand("aceptararf");
		acceptbutton.addActionListener(this);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		this.setVisible(false);
		dispose();
	}
}
