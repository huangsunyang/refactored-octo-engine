package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class BloodBar {
	private Position curPos;
	private int width;
	private int height = 10;
	private int dis = 3;
	private double percent;
	private int life;
	private Color color = Color.red;
	private Color textColor = Color.WHITE;
//	private Position[] poses = {
//			new Position(350, 300), new Position(360, 300), new Position(375, 275),
//			new Position(400, 200), new Position(360, 270), new Position(350, 300)
//	}
	//构造函数
	public BloodBar(Tanks tank) {
		width = tank.getSize();
		curPos = new Position(tank.getCurPosX(), tank.getCurPosY()- dis - height);
		life = tank.getLife();
		percent = tank.getLife() * 1.0 / tank.getFullLife();
		color = tank.getTankColor();
	}
	//更新位置
	void update(Tanks tank) {
		curPos.setX(tank.getCurPosX());
		curPos.setY(tank.getCurPosY()- dis - height);
		life = tank.getLife();
		percent = tank.getLife() * 1.0 / tank.getFullLife();
	}
	//绘制血条
	public void drawBloodBar(Graphics g) {
		Color c = g.getColor();
		g.setColor(color);
		g.drawRect(curPos.getX(), curPos.getY(), width, height);
		g.fillRect(curPos.getX(), curPos.getY(), (int)(width * percent), height);
		g.setFont(new Font("宋体", Font.BOLD, 10));
		g.setColor(textColor);
		g.drawString(Integer.toString(life), curPos.getX() + width / 2, curPos.getY() + height);
		g.setColor(c);
	}
}
