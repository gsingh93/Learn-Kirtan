package gsingh.learnkirtan;

import java.net.URL;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class HelpFrame {
	JFrame f;
	JMenuItem topics;

	public HelpFrame() {
		f = new JFrame("Menu Example");
		JMenuBar mbar = new JMenuBar();

		// a file menu
		JMenu file = new JMenu("File");
		JMenu help = new JMenu("Help");

		// add an item to the help menu
		help.add(topics = new JMenuItem("Help Topics"));

		// add the menu items to the menu bar
		mbar.add(file);
		mbar.add(help);
		// 1. create HelpSet and HelpBroker objects
		HelpSet hs = getHelpSet("Sample.hs");
		HelpBroker hb = hs.createHelpBroker();

		// 2. assign help to components
		CSH.setHelpIDString(topics, "top");

		// 3. handle events
		topics.addActionListener(new CSH.DisplayHelpFromSource(hb));
		// attach menubar to frame, set its size, and make it visible
		f.setJMenuBar(mbar);
		f.setVisible(true);
	}

	/**
	 * find the helpset file and create a HelpSet object
	 */
	public HelpSet getHelpSet(String helpsetfile) {
		HelpSet hs = null;
		ClassLoader cl = this.getClass().getClassLoader();
		try {
			URL hsURL = HelpSet.findHelpSet(cl, helpsetfile);
			hs = new HelpSet(null, hsURL);
		} catch (Exception ee) {
			System.out.println("HelpSet: " + ee.getMessage());
			System.out.println("HelpSet: " + helpsetfile + " not found");
		}
		return hs;
	}

	public static void main(String argv[]) {
		new HelpFrame();
	}
}
//
// import java.io.IOException;
// import java.net.URL;
//
// import javax.swing.JEditorPane;
// import javax.swing.JFrame;
// import javax.swing.JScrollPane;
//
// public class HelpFrame extends JFrame {
//
// private static final long serialVersionUID = 1L;
//
// JEditorPane helpDisplay = null;
//
// public HelpFrame() {
// helpDisplay = new JEditorPane();
// try {
// URL url = this.getClass().getClassLoader().getResource("help.html");
// helpDisplay.setPage(url);
// } catch (IOException e) {
// e.printStackTrace();
// }
// helpDisplay.setEditable(false);
// JScrollPane scrollPane = new JScrollPane(helpDisplay);
// add(scrollPane);
// setSize(500, 500);
// setLocation(400, 100);
// setVisible(true);
// }
// }
