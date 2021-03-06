package GUI;

import java.awt.FlowLayout;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class OptionsMenu extends JMenuBar {
	
	JMenu optionBut;
	JMenuItem mFile;
	JMenuItem mProperties;
	JMenuItem mExit;

	
	public OptionsMenu () {
		mFile = new JMenuItem("File...");
		mProperties = new JMenuItem("Properties");
		mExit = new JMenuItem("Exit");

		optionBut = new JMenu("Opciones");
		optionBut.add(mFile);
		optionBut.add(mProperties);
		optionBut.addSeparator();
		optionBut.add(mExit);

		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(optionBut);
	}
}
