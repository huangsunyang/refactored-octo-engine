package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

public class Missile {
	Position curtPos;
	protected int size = 10;				//子弹大小
	protected int speed = 15;				//子弹速度
	protected Direction dir;				//子弹方向
	protected int damage = 5;				//子弹威力
	protected final int FULLDAMAGE = 15;	//子弹的最大威力
	protected int property;				//子弹归属 TODO: 同一队的子弹互相不受攻击 也可用颜色区分
	protected Color color = Color.ORANGE;
	protected boolean alive = true;
	protected Group group = Group.UNDEFINED;
	Background bg;
	//构造函数
	public Missile(Background bg) {
		curtPos = new Position(0, 0);
		this.dir = Direction.D;
		this.bg = bg;
	}
	//封装与获取
	public boolean isAlive() { return alive; }
	public int getSize() { return size; }
	public int getSpeed() { return speed; } 
	public int getDamage() {return damage; }
	public void setDamage(int dmg) {damage = dmg; }
	public Direction getDir() {return dir; }
	public int getCurtPosX() {return curtPos.getX(); }
	public int getCurtPosY() {return curtPos.getY(); }
	public Group getGroup() { return group; }
	public void setGroup(Group group) { this.group = group; }
	public Shape getRec() { return new Rectangle(curtPos.getX(), curtPos.getY(), size, size); }
	public Position getCurtPos() {return curtPos; }
	public void setLife(boolean alive) { this.alive = alive; }
	public void setSize(int size) { this.size = size; }
	public void setCurtPosX(int x) { curtPos.setX(x); }
	public void setCurtPosY(int x) { curtPos.setY(x); }
	public void setCurtPos(int x, int y) {curtPos.setX(x); curtPos.setY(y); }
	public void setDir(Direction dir) { this.dir = dir; }
	//绘制子弹
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval(curtPos.getX(), curtPos.getY(), size, size);
		move(dir);
	}
	//子弹运动轨迹
	public void move(Direction dir) {
		double p = Math.sqrt(2) / 2;
		int leanSpeed = (int) (p * speed);
		switch (dir) {
			case L: curtPos.add(-speed, 0); break;
			case R: curtPos.add(speed, 0); break;
			case D: curtPos.add(0, speed); break;
			case U: curtPos.add(0, -speed); break;
			case LU: curtPos.add(-leanSpeed, -leanSpeed); break;
			case RU: curtPos.add(leanSpeed, -leanSpeed); break;
			case LD: curtPos.add(-leanSpeed, leanSpeed); break;
			case RD: curtPos.add(leanSpeed, leanSpeed); break;
			default: break;
		}
	}
	//判断子弹射出panel
	boolean outOfBackground(Background bg) {
		if(outOfLeftUpBackground(bg)) {
			bg.exploxions.add(new Explosion(getCurtPosX(), getCurtPosY(), bg));
			bg.missiles.remove(this);
			return true;
		} else if(outOfRightDownBackground(bg)) {
			bg.exploxions.add(new Explosion(getCurtPosX(), getCurtPosY(), bg));
			bg.missiles.remove(this);
			return true;
		}
		return false;
	}
	//判断子弹射向左上
	boolean outOfLeftUpBackground(JPanel bg) {
		return curtPos.getX() <= 0 || curtPos.getY() <= 0;
	}
	//判断子弹射向右下
	boolean outOfRightDownBackground(JPanel bg) {
		return curtPos.getX() >= bg.getWidth() || curtPos.getY() >= bg.getHeight();
	}
	//判断是否击中坦克
	public boolean hitTank(Tanks tank) { 
		if(tank.isAlive() && tank.getGroup() != group && this.getRec().intersects(tank.getRec())) {
			int dmg = Math.min(damage, tank.getLife());
			tank.setLife(tank.getLife() - dmg);
			this.setLife(false);
			Explosion e = new Explosion(getCurtPosX(), getCurtPosY(), bg);
			bg.exploxions.add(e);
			bg.missiles.remove(this);
			return true;
		}
		return false;
	}
	//判断是否击中一堆坦克
	public void hitTank(List<Tanks> tanks) {
		for (Tanks tank : tanks) {
			hitTank(tank);
		}
	}
	
}
