package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.plaf.OptionPaneUI;

public class MainFrame extends JFrame {
	protected static boolean paused = false;
	protected static boolean restart = false;
	static Background b = new Background();
	static SideBar sideBar = new SideBar(b);
	Menu menu = new Menu(this);
	public static final int MENUBARHEIGHT = 50;
	TankType[] tankList = TankType.values();
	
	public MainFrame() {
		setLayout(null);
		sideBar.setLocation(b.getWidth(), MENUBARHEIGHT);
		sideBar.setSize(menu.getWidth() - b.getWidth(), b.getHeight());
		sideBar.setBackground(Color.GRAY);
		add(menu);
		add(sideBar);
		add(b);
		this.addKeyListener(new Keymonitor());
	}
	public void initial() {
		b.initial();
		sideBar.tank = b.tank;
	}
	public static void setPaused(boolean a) { paused = a; }
	public static boolean isPaused() { return paused; }
	public static void setRestart(boolean a) { restart = a; }
	public static boolean isRestart() { return restart; }
	private class Keymonitor extends KeyAdapter {
		public void keyReleased(KeyEvent e) {
			b.tank.keyReleased(e);
		}
		public void keyPressed(KeyEvent e) {
			b.tank.keyPressed(e);
		}
	}
	
	//运行以及实时更新
	public static void main(String[] args) {
		MainFrame f = new MainFrame();
		f.setResizable(false);
		SwingConsole.run(f, f.menu.getWidth(), f.menu.getHeight() + b.getHeight() + 30);
		Thread myThread = new Thread(new Runnable() {
			public void run() {
				while(true) {
					if(restart) {
						f.initial();
						restart = false;
					}
					if(!paused) {
						b.repaint();
						sideBar.repaint();
					}
					try {
						Thread.sleep(20);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			}
		});
		myThread.start();
	}
}
