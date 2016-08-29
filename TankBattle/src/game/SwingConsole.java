package game;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class SwingConsole {
	public static void run(JFrame f, final int w, final int h) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f.setTitle(f.getClass().getSimpleName());
				f.setSize(w, h);
				f.setVisible(true);
			}
		});
	}
}
