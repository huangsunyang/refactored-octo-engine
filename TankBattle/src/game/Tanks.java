package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints.Key;
import java.awt.event.KeyEvent;
import java.sql.Savepoint;
import java.util.HashMap;
import java.util.Map;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

public class Tanks {
	protected Position curtPos;	//当前坐标
	protected Position prePos;	//前一帧坐标
	protected int life = 100;		//血量
	protected int FULLLIFE = 100;	//满血血量
	protected int size = 30;		//坦克大小（方形， 今后可能扩展为矩形等）
	protected Direction speedDir = Direction.STOP;		//方向，即该瞬间坦克的速度方向
	protected Direction staticDir = Direction.D;		//方向，即坦克炮管的朝向
	protected int speed = 2;		//速度
	protected int FULLSPEED = 5;
	protected int SPEEDSTEP = 1;
	protected int damage = 0;
	protected int weight = 5;
	protected int FULLDAMAGE = 20;
	protected int DAMAGESTEP = 5;
	protected int tubeLen = 24;	//炮管长度
	protected Background bg;		//背景框
	protected int superFireTimes = 5;		//大招的次数限制
	protected int killNum = 0;
	protected int totalDamage = 0;
	protected int FULLSUPERFIRETIMES = superFireTimes;
	protected int SUPERFIRETIMESTEP = 2;
	protected Color tankColor = new Color(232, 122, 144);
	protected Color tubeColor = Color.PINK; 
	protected BloodBar bloodbar; 
	protected Group group = Group.PLAYER1;
	protected boolean zPressed = false;
	protected boolean xPressed = false;
	protected Missile missile;
	protected Map<String, Boolean> logDirection = new HashMap<String, Boolean>() {	//记录按键
		{
			put("LEFT", false);
			put("RIGHT", false);
			put("UP", false);
			put("DOWN", false);
		}
	};
	//构造函数，设置坦克的出现位置，传入其所在的背景
	public Tanks(int x, int y, Background bg) {
		curtPos = new Position(x, y);
		prePos = new Position(x, y);
		this.bg = bg;
		bloodbar = new BloodBar(this);
		missile = new Missile(bg);
	}
	//获取坦克的中心点
	public Position getCentre() {
		return new Position(curtPos.getX() + size / 2, curtPos.getY() + size / 2);
	}
	public int getCentreX() {
		return curtPos.getX() + size / 2;
	}
	public int getCentreY() {
		return curtPos.getY() + size / 2;
	}
	//封装与获取
	public int getWeight() { return weight;}
	public Group getGroup() { return group; }
	public void setGroup(Group group) { this.group = group; }
	public boolean isAlive() { return life > 0; }
	public int getLife() { return life; }
	public int getFullLife() { return FULLLIFE; }
	public int getCurPosX() { return curtPos.getX(); }
	public int getCurPosY() { return curtPos.getY(); }
	public Position getCurPos() { return new Position(curtPos); }
	public int getSize() { return size; }
	public Rectangle getRec() { return new Rectangle(curtPos.getX(), curtPos.getY(), size, size); }
	public void setLife(int life) { this.life = life; }
	public void setSpeed(int speed) { this.speed = speed; }
	public Color getTankColor() { return tankColor; }
	public void setTankColor(Color color) {tankColor = color;}
	public void setDirection(Direction dir) { speedDir = dir; }
	public Direction getSpeedDir() {return speedDir; }
	public int getDamage() {return damage; }
	public int getFullDamage() {return FULLDAMAGE; }
	public void setDamage(int dmg) {damage = dmg; }
	public int getFullSpeed() {return FULLSPEED; }
	public int getSpeed() {return speed; }
	public int getSuperFire() {return superFireTimes; }
	public int getFullSuperFire() {return FULLSUPERFIRETIMES; }
	public int getKillNum() { return killNum; }
	public int getTotalDamage() {return totalDamage; }
	public void addTotalDamage(int dmg) {totalDamage += dmg; }
	public void addKillNum(int a) { killNum += a; }
	//获取坦克炮管顶点
	public Position getTubePoint() {
		switch (staticDir) {
		case L: return new Position(getCentreX() - tubeLen, getCentreY()); 
		case R: return new Position(getCentreX() + tubeLen, getCentreY()); 
		case D: return new Position(getCentreX(), getCentreY() + tubeLen);
		case U: return new Position(getCentreX(), getCentreY() - tubeLen);
		case LU: return new Position(getCentreX() - tubeLen, getCentreY() - tubeLen);
		case RU: return new Position(getCentreX() + tubeLen, getCentreY() - tubeLen);
		case LD: return new Position(getCentreX() - tubeLen, getCentreY() + tubeLen);
		case RD: return new Position(getCentreX() + tubeLen, getCentreY() + tubeLen);
		default: return new Position(getCentreX(), getCentreY());
		}
	}
	public int getTubePosX() { return getTubePoint().getX(); }
	public int getTubePosY() { return getTubePoint().getY(); }
	//屏幕上绘制一个tank
	public void draw(Graphics g) {
		if(!isAlive() && group == Group.PLAYER1) {
			gameOver(g); 
			return;
		}
		Color c = g.getColor();
		g.setColor(getTankColor());
		g.fillOval(getCurPosX(), getCurPosY(), getSize(), getSize());		//绘制坦克
		paintTube(g);	//绘制炮管
		paintBloodBar(g);	//绘制血条
		g.setColor(c);	//改回颜色
		move();			//更新坦克位置
	}
	//绘制血条
	void paintBloodBar(Graphics g) {
		Color c = g.getColor();
		bloodbar.drawBloodBar(g);
		bloodbar.update(this);
		g.setColor(c);
	}
	//判断炮管的方向并绘制
	void paintTube(Graphics g) {
		Color c = g.getColor();
		g.setColor(tubeColor);
		Direction tempDir = speedDir;
		if(speedDir == Direction.STOP) tempDir = staticDir;
		double p = Math.sqrt(2) / 2;
		int leanLen = (int) (p * tubeLen);
		switch (tempDir) {
			case L: g.drawLine(getCentreX(), getCentreY(), getCentreX() - tubeLen, getCentreY()); break;
			case R: g.drawLine(getCentreX(), getCentreY(), getCentreX() + tubeLen, getCentreY()); break;
			case D: g.drawLine(getCentreX(), getCentreY(), getCentreX(), getCentreY() + tubeLen); break;
			case U: g.drawLine(getCentreX(), getCentreY(), getCentreX(), getCentreY() - tubeLen); break;
			case LU: g.drawLine(getCentreX(), getCentreY(), getCentreX() - leanLen, getCentreY() - leanLen); break;
			case RU: g.drawLine(getCentreX(), getCentreY(), getCentreX() + leanLen, getCentreY() - leanLen);break;
			case LD: g.drawLine(getCentreX(), getCentreY(), getCentreX() - leanLen, getCentreY() + leanLen);break;
			case RD:g.drawLine(getCentreX(), getCentreY(), getCentreX() + leanLen, getCentreY() + leanLen);break;
			default:break;
		}
		g.setColor(c);
	}
	//静止的表示方法
	public void stay() {
		
	}
	//带方向的移动方法
	public void move(Direction dir) {
		switch (dir) {
			case L: curtPos.add(-speed, 0); break;
			case R: curtPos.add(speed, 0); break;
			case D: curtPos.add(0, speed); break;
			case U: curtPos.add(0, -speed); break;
			case LU: curtPos.add(-speed, -speed); break;
			case RU: curtPos.add(speed, -speed); break;
			case LD: curtPos.add(-speed, speed); break;
			case RD: curtPos.add(speed, speed); break;
			default:	break;
		}
	}
	//判断坦克是否触碰便捷
	public boolean collideWall() {
		return curtPos.getX() < 0 || curtPos.getY() < 0 ||
				curtPos.getX() + size > bg.getWidth() ||
				curtPos.getY() + size > bg.getHeight();
	}
	//通过记录正在按下的键盘判断每一帧的位移
	public void move() {
		prePos.set(curtPos);
		if(speedDir != Direction.STOP)	staticDir = speedDir;	//如果当前状态并不是静止，则更新静止状态
		//防止坦克超出边界
		if(curtPos.getX() < 0) curtPos.setX(0); 
		if(curtPos.getY() < 0) curtPos.setY(0);
		if(curtPos.getX() + size > bg.getWidth()) curtPos.setX(bg.getWidth() - size);
		if(curtPos.getY() + size > bg.getHeight()) curtPos.setY(bg.getHeight() - size);
		//判断移动方向和距离
		move(speedDir);
	}
	//监控键盘按下
	public void keyPressed(KeyEvent e) {
		if(!isAlive()) return;
		int key = e.getKeyCode();
		switch (key) {
			case KeyEvent.VK_LEFT:	logDirection.put("LEFT", true); break;
			case KeyEvent.VK_RIGHT:	logDirection.put("RIGHT", true); break;
			case KeyEvent.VK_UP:	logDirection.put("UP", true); break;
			case KeyEvent.VK_DOWN:	logDirection.put("DOWN", true); break;
			case KeyEvent.VK_Z:		if(!zPressed) { fire(); zPressed = true; } break;
			case KeyEvent.VK_X:		if(superFireTimes > 0 && !xPressed) { superFire(); xPressed = true; } break;
			default:
				break;
		}
		getDirection();
	}
	//监控键盘释放
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:	logDirection.put("LEFT", false); break;
		case KeyEvent.VK_RIGHT:	logDirection.put("RIGHT", false); break;
		case KeyEvent.VK_UP:	logDirection.put("UP", false); break;
		case KeyEvent.VK_DOWN:	logDirection.put("DOWN", false); break;
		case KeyEvent.VK_Z:		zPressed = false; break;
		case KeyEvent.VK_X:		xPressed = false; break;
		default:
			break;
		}
		getDirection();
	}
	//通过当前被按下的键确定方向
	public void getDirection() {
		if(logDirection.get("LEFT") && !logDirection.get("RIGHT") && 
		    !logDirection.get("UP") && !logDirection.get("DOWN")) {
			speedDir = Direction.L;
		} else if (!logDirection.get("LEFT") && logDirection.get("RIGHT") && 
			    !logDirection.get("UP") && !logDirection.get("DOWN")){
			speedDir = Direction.R;
		} else if (!logDirection.get("LEFT") && !logDirection.get("RIGHT") && 
			    !logDirection.get("UP") && logDirection.get("DOWN")){
			speedDir = Direction.D;
		} else if (!logDirection.get("LEFT") && !logDirection.get("RIGHT") && 
			    logDirection.get("UP") && !logDirection.get("DOWN")){
			speedDir = Direction.U;
		} else if (logDirection.get("LEFT") && !logDirection.get("RIGHT") && 
			    !logDirection.get("UP") && logDirection.get("DOWN")){
			speedDir = Direction.LD;
		} else if (!logDirection.get("LEFT") && logDirection.get("RIGHT") && 
			    !logDirection.get("UP") && logDirection.get("DOWN")){
			speedDir = Direction.RD;
		} else if (logDirection.get("LEFT") && !logDirection.get("RIGHT") && 
			    logDirection.get("UP") && !logDirection.get("DOWN")){
			speedDir = Direction.LU;
		} else if (!logDirection.get("LEFT") && logDirection.get("RIGHT") && 
			    logDirection.get("UP") && !logDirection.get("DOWN")){
			speedDir = Direction.RU;
		} else if (!logDirection.get("LEFT") && !logDirection.get("RIGHT") && 
			    !logDirection.get("UP") && !logDirection.get("DOWN")){
			speedDir = Direction.STOP;
		}
	}
	//发射子弹
	Missile fire() {
		return fire(staticDir);
	}
	//发射子弹 带子弹方向参数
	Missile fire(Direction dir) {
		Missile msl = new Missile(bg);
		msl.setGroup(group);
		int x = getTubePosX() - msl.getSize() / 2;
		int y = getTubePosY() - msl.getSize() / 2;
		msl.setCurtPos(x, y);
		msl.setDir(dir);
		msl.setDamage(msl.getDamage() + damage);
		bg.missiles.add(msl);
		return msl;
	}
	//大招1：超各个方向发射一枚子弹（后期出现多个角色可能会替换）
	void superFire() {
		for(Direction dir : Direction.values()) {
			if(dir != Direction.STOP) fire(dir);
		}
		superFireTimes--;
	}
	//坦克吃到物品后的变化
	void eat(ItemType t) {
		switch (t) {
		case ALLBLOOD: setLife(FULLLIFE); break;
		case BLOOD: setLife(Math.min(100, life + (int)(FULLLIFE / 3))); break;
		case POWER: setDamage(Math.min(FULLDAMAGE, damage + DAMAGESTEP));break;
		case SPEED: setSpeed(Math.min(speed + SPEEDSTEP, FULLSPEED)); break;
		case SKILL: superFireTimes = Math.min(superFireTimes + SUPERFIRETIMESTEP, FULLSUPERFIRETIMES); break;
		default: break;
		}
	}
	//坦克与坦克相撞
	public void collideTank(Tanks tank) {
		if(isAlive() && tank.isAlive() && getRec().intersects(tank.getRec())) {
			if(getGroup() != tank.getGroup()) {
				int dif = weight - tank.getWeight();
				setLife(life - 1 - Math.max(0, -dif));
				tank.setLife(tank.getLife() - 1 - Math.max(0, dif));
			}
			move(Direction.counterDirection(speedDir));
			tank.move(Direction.counterDirection(tank.getSpeedDir()));
			move(Direction.counterDirection(speedDir));
			tank.move(Direction.counterDirection(tank.getSpeedDir()));
			move(Direction.counterDirection(speedDir));
			tank.move(Direction.counterDirection(tank.getSpeedDir()));
		}
	}
	//绘制游戏结束画面
	public void gameOver(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("黑体", Font.BOLD, Math.min(bg.getHeight(), bg.getWidth()) / 6));
		g.drawString("GAME OVER", bg.getX() + bg.getWidth() / 2 - (int)(g.getFont().getSize() * 2.5), bg.getY() + bg.getHeight() / 2);
	}
}
