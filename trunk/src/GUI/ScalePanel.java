package GUI;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class ScalePanel extends JPanel implements ActionListener {
	private Double x = new Double(1.0);
	private Double y = new Double(1.0);
	private MainFrame owner;
	private JTextField txtx = new JTextField (x.toString());
	private JTextField txty = new JTextField (y.toString());
	
	
	public ScalePanel (MainFrame own) {
		this.setLayout(new GridLayout(2, 4));
		owner = own;
		
		JButton plusx = new JButton("X+");
		plusx.setActionCommand("x+");
		plusx.addActionListener(this);
		
		JButton plusy = new JButton("Y+");
		plusy.setActionCommand("y+");
		plusy.addActionListener(this);
		
		JButton lessx = new JButton("X-");
		lessx.setActionCommand("x-");
		lessx.addActionListener(this);
		
		JButton lessy = new JButton("Y-");
		lessy.setActionCommand("y-");
		lessy.addActionListener(this);
		
		JButton defx = new JButton("DX");
		defx.setActionCommand("dx");
		defx.addActionListener(this);
		
		JButton defy = new JButton("DY");
		defy.setActionCommand("dy");
		defy.addActionListener(this);
		
		txtx.setActionCommand("txtx");
		txtx.addActionListener(this);
		
		txty.setActionCommand("txty");
		txty.addActionListener(this);
		
		add(plusx);
		add(lessx);
		add(defx);
		add(txtx);
		add(plusy);
		add(lessy);
		add(defy);
		add(txty);
	}


	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "x+") {x += 0.5;}
		else if (e.getActionCommand() == "y+") {y += 0.5;}
		else if (e.getActionCommand() == "x-") {x -= 0.5;}
		else if (e.getActionCommand() == "y-") {y -= 0.5;}
		else if (e.getActionCommand() == "dx") {x = 1.0;}
		else if (e.getActionCommand() == "dy") {y = 1.0;}
		else if (e.getActionCommand() == "txtx") {
			try {
				Double d = new Double(txtx.getText());
				x = d;
			}
			catch (NumberFormatException exc) {
				System.out.println("Fail");
			}
		}
		else if (e.getActionCommand() == "txty") {
			try {
				Double d = new Double(txty.getText());
				y = d;
			}
			catch (NumberFormatException exc) {
				System.out.println("Fail");
			}
		}
		txtx.setText(x.toString());
		txty.setText(y.toString());
		owner.setScaleDraw(x.doubleValue(), y.doubleValue());
	}
}
