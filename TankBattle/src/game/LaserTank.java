package game;

import java.awt.Color;

public class LaserTank extends Tanks {
	public LaserTank(int x, int y, Background bg) {
		super(x, y, bg);
		weight = 2;
		speed = 3;
		FULLSPEED = 6;
		tankColor = Color.cyan;
		life = 80;
		FULLLIFE = 80;
		damage = -4;
		FULLDAMAGE = 12;
		DAMAGESTEP = 2;
		bloodbar = new BloodBar(this);
	}
	//发射子弹
	Missile fire() {
		return fire(staticDir);
	}
	//发射子弹 带子弹方向参数
	Missile fire(Direction dir) {
		Missile msl = new LaserMissile(bg);
		msl.setGroup(group);
		int x = getTubePosX();
		int y = getTubePosY();
		msl.setCurtPos(x, y);
		msl.setDir(dir);
		msl.setDamage(msl.getDamage() + damage);
		bg.missiles.add(msl);
		return msl;
	}
	//大招1：使用环绕大范围攻击
	void superFire() {
		Missile msl = new SuperLaserMissile(bg);
		msl.setGroup(group);
		int x = getCentreX();
		int y = getCentreY();
		msl.setCurtPos(x, y);
		msl.setDamage(msl.getDamage() + damage);
		bg.missiles.add(msl);
		superFireTimes--;
	}
}
