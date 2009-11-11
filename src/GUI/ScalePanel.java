package GUI;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class ScalePanel extends JPanel implements ActionListener {
	private int x;
	private int y;
	
	public ScalePanel () {
		this.setLayout(new GridLayout(2, 4));
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
		
		JTextField txtx = new JTextField ("0");
		txtx.setActionCommand("txtx");
		txtx.addActionListener(this);
		
		JTextField txty = new JTextField ("0");
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
	}
}
