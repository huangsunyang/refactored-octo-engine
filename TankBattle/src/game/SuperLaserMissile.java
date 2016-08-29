package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

public class SuperLaserMissile extends LaserMissile {
	//构造函数
	public SuperLaserMissile(Background bg) {
		super(bg);
	}
	public double getDistance(Tanks tank) { 
		int x = tank.getCentreX() - getCurtPosX();
		int y = tank.getCentreY() - getCurtPosY();
		return Math.sqrt(x * x + y * y);
	}
	//绘制子弹
	public void draw(Graphics g) {
		if(!isAlive()) {
			bg.missiles.remove(this);
			return;
		}
		if(step >= len.length - 1) {
			setLife(false);
			return;
		}
		g.setColor(color);
		g.drawOval(curtPos.getX() - len[step] /2, curtPos.getY() - len[step] / 2, len[step],  len[step]);
		step++;
	}
	//判断是否击中坦克
	public boolean hitTank(Tanks tank) { 
		if(alive && tank.isAlive() && tank.getGroup() != group && getDistance(tank) <= len[step]) {
			int dmg = Math.min(damage, tank.getLife());
			tank.setLife(tank.getLife() - dmg);
			Explosion e = new Explosion(tank.getCentreX(), tank.getCentreY(), bg);
			bg.exploxions.add(e);
			return true;
		}
		return false;
	}
}
