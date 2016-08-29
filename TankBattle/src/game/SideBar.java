package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.Box.Filler;

public class SideBar extends JPanel{
	Tanks tank;
	Background bg;
	MainFrame f;
	public SideBar(Background bg) {
		//super();
		this.bg = bg;
		this.tank = bg.tank; 
	}
	public void paintComponent(Graphics g) {
		setBackground(new Color(119, 150, 154));
		super.paintComponent(g);
		//绘制当前能力表
		g.setColor(Color.WHITE);
		g.setFont(new Font("黑体", Font.BOLD, 20));
		int gap = g.getFont().getSize();
		int startPosX = 20, startPosY = 800;
		int startPosX2 = 160, startPosY2 = 790;
		g.drawString("Type:        " + tank.getClass().getSimpleName(), startPosX, startPosY);
		g.drawString("Power: ",startPosX, startPosY + gap);
		drawSkills(g, tank.getDamage(), tank.getFullDamage(), startPosX2, startPosY2 + gap, Color.yellow);
		g.drawString("Speed: ", startPosX, startPosY + 2 * gap);
		drawSkills(g, tank.getSpeed(), tank.getFullSpeed(), startPosX2, startPosY2 + 2 * gap, new Color(129, 199, 212));
		g.drawString("SuperFire: ", startPosX, startPosY + 3 * gap);
		drawSkills(g, tank.getSuperFire(), tank.getFullSuperFire(), startPosX2, startPosY2 + 3 * gap, new Color(112, 124, 116));
		//绘制战斗数据
		g.drawString("KillNum:           " + tank.getKillNum() , startPosX, 20);
	}
	public void drawSkills(Graphics g, int currentValue, int fullValue, int x, int y, Color d) {
		Color c = g.getColor();
		Font f = g.getFont();
		g.setColor(d);
		int height =  g.getFont().getSize() / 2;
		int width = 100;
		g.drawRect(x, y, width, height);
		g.fillRect(x, y, (int)(width * (Math.max(0, currentValue) * 1.0 / fullValue)), height);
		g.setFont(new Font("黑体", Font.BOLD, 12));
		g.setColor(Color.WHITE);
		g.drawString("" + currentValue + "\\" + fullValue, x + 70, y + 10);
		g.setColor(c);
		g.setFont(f);
	}
}
