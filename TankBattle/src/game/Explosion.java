package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

public class Explosion {
	Position curtPos;
	private int size[] = {4, 7, 12, 18, 26, 32, 49, 30, 14, 6};	//爆炸各个阶段的直径
	private int step = 0;
	private int damage = 1;
	private Color color = Color.YELLOW;
	private boolean isAlive() {return step < size.length;}
	private Background bg;
	//构造函数
	public Explosion() {
		
	}
	public Explosion(int x, int y, Background bg) {
		curtPos = new Position(x, y);
		this.bg = bg;
	}
	//封装与获取
	public Rectangle getRec() { return new Rectangle(getCurtPosX(), getCurtPosY(), size[step], size[step]);}
	public int getDmg() { return damage; }
	public int getCurtPosX() {return curtPos.getX(); }
	public int getCurtPosY() {return curtPos.getY(); }
	public Color getColor() { return color; } 
	public Position getCurtPos() {return curtPos; }
	public int getCurSize() { return size[step]; }
	public int getSize() {return 50; }
	public void setCurtPosX(int x) { curtPos.setX(x); }
	public void setCurtPosY(int x) { curtPos.setY(x); }
	public void setColor(Color color) {this.color = color; }
	//绘制爆炸效果
	public void draw(Graphics g) {
		if(!isAlive()) { 
			bg.exploxions.remove(this);
			return;
		}
		Color c = g.getColor();
		g.setColor(color);
		g.fillOval(getCurtPosX() - size[step] / 2, getCurtPosY() - size[step] / 2, size[step], size[step]);
		g.setColor(c);
		step++;
		
	}
	//判断爆炸是否击中坦克
	public boolean hitTank(Tanks tank) { 
		if(isAlive() && tank.isAlive() && this.getRec().intersects(tank.getRec())) {
			tank.setLife(tank.getLife() - damage);
			return true;
		}
		return false;
	}
	//判断爆炸是否击中一组坦克
	public void hitTank(List<Tanks> tanks) {
		for (Tanks tanks2 : tanks) {
			hitTank(tanks2);
		}
	}
}
