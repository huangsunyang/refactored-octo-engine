package game;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class Menu extends JMenuBar{
	protected JFrame f;
	protected JMenu menuGames = new JMenu("Games");
	protected JMenu menuHelp = new JMenu("Help");
	protected JOptionPane exitPane = new JOptionPane();
	public Menu(JFrame f) {
		this.f = f;
		setSize(1200, MainFrame.MENUBARHEIGHT);
		setLocation(0, 0);
		//游戏菜单的三个按钮，分别控制重新开始 暂停和退出
		JMenuItem itemStart = new JMenuItem("Restart");
		JMenuItem itemPause = new JMenuItem("Pause");
		JMenuItem itemEnd = new JMenuItem("Exit");
		menuGames.add(itemStart);
		menuGames.add(itemPause);
		menuGames.add(itemEnd);
		//帮助菜单
		JMenuItem itemHelp = new JMenuItem("KeyHelp");
		menuHelp.add(itemHelp);
		//添加次级菜单
		add(menuGames);
		add(menuHelp);
		exitPane.setLocation(0,0);
		//退出时弹出对话框
		itemEnd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(JOptionPane.showConfirmDialog(exitPane, "Are you sure?", "Comfirm", JOptionPane.YES_NO_OPTION) == 0)
					f.dispose();
			}
		});
		//暂停继续功能的实现
		itemPause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(!MainFrame.isPaused()) {
					((JMenuItem)(e.getSource())).setText("Continue");
					MainFrame.setPaused(true);
				}
				else {
					((JMenuItem)(e.getSource())).setText("Pause");
					MainFrame.setPaused(false);
				}
			}
		});
		//restart功能的实现
		itemStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				MainFrame.setRestart(true);
				MainFrame.setPaused(false);
				itemPause.setText("Pause");
			}
		});
	}
}
