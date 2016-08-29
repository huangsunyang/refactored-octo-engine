package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.lang.reflect.Array;

import javax.sound.sampled.Line;

public class LaserMissile extends Missile {
	protected int[] len = {8, 32, 89, 120, 154, 120, 90, 37, 18};
	protected int step = 0;
	public int getLen() {
		int result = -1;
		for (int i : len) {
			result = Math.max(result, i);
		}
		return result;
	}
	//构造函数
	public LaserMissile(Background bg) {
		super(bg);
		color = Color.CYAN;
	}
	public Shape getRec() { 
		switch (dir) {
		case D: return new Rectangle(curtPos.getX(), curtPos.getY(), 1, len[step]);
		case U: return new Rectangle(curtPos.getX(), curtPos.getY() - len[step], 1, len[step]);
		case L: return new Rectangle(curtPos.getX() - len[step], curtPos.getY(), len[step], 1);
		case R: return new Rectangle(curtPos.getX(), curtPos.getY(), len[step], 1);
		default: return new Rectangle(0, 0, 0, 0);
		}
		
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
		switch(dir) {
			case D: g.drawLine(curtPos.getX(), curtPos.getY(), curtPos.getX(), curtPos.getY() + len[step]); break;
			case U: g.drawLine(curtPos.getX(), curtPos.getY(), curtPos.getX(), curtPos.getY() - len[step]); break;
			case L: g.drawLine(curtPos.getX(), curtPos.getY(), curtPos.getX() - len[step], curtPos.getY()); break;
			case R: g.drawLine(curtPos.getX(), curtPos.getY(), curtPos.getX() + len[step], curtPos.getY()); break;
			default: break;
		}
		step++;
//		}
	}
	//判断是否击中坦克
		public boolean hitTank(Tanks tank) { 
			if(alive && tank.isAlive() && tank.getGroup() != group && this.getRec().intersects(tank.getRec())) {
				int dmg = Math.min(damage, tank.getLife());
				tank.setLife(tank.getLife() - dmg);
				Explosion e = new Explosion(tank.getCentreX(), tank.getCentreY(), bg);
				bg.exploxions.add(e);
				return true;
			}
			return false;
		}
}
