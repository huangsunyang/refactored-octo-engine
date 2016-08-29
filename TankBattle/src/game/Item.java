package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;
import java.util.Random;

public class Item {
	Position curtPos;
	ItemType type;
	int size;
	Color color;
	Background bg;
	boolean alive = true;
	//构造函数
	public Item(int x, int y, ItemType type) {
		this.type = type;
		setProperty();
		curtPos = new Position(x, y);
	}
	public Item() {
		ItemType type = ItemType.randomItemType();
		this.type = type;
		setProperty();
		curtPos = new Position(0, 0);
	}
	public Item(Background bg) {
		this();
		this.bg = bg; 
		placeItem(bg);
	}
	//封装与获取
	public int getCurtPosX() {return curtPos.getX(); }
	public int getCurtPosY() {return curtPos.getY(); }
	public Rectangle getRec() { return new Rectangle(getCurtPosX(), getCurtPosY(), size, size); }
	public void setSize(int size) { this.size = size; }
	public void setCurtPosX(int x) { curtPos.setX(x); }
	public void setCurtPosY(int x) { curtPos.setY(x); }
	public void setCurtPos(int x, int y) {curtPos.setX(x); curtPos.setY(y); }
	//根据物品的类别设置其大小和颜色
	void setProperty() {
		switch (type) {
		case ALLBLOOD:	size = 15;	color = Color.red;		break;
		case BLOOD:		size = 10;	color = Color.red;		break;
		case SPEED:		size = 10;	color = Color.BLUE;		break;
		case POWER:		size = 10;	color = Color.YELLOW;	break;
		case SKILL:		size = 10;	color = Color.GRAY;		break;
		default:
			break;
		}
	}
	//随机选取item类型
//	ItemType randomItemType() {
//		Random rand = new Random();
//		ItemType[] allItems = ItemType.values();
//		ItemType item = allItems[rand.nextInt(allItems.length)];
//		return item;
//	}
	//随机出现item
	public Position placeItem(Background bg) {
		Random rand = new Random();
		int x = rand.nextInt(bg.getWidth() - size) + bg.getX();
		int y = rand.nextInt(bg.getHeight() - size) + bg.getY();
		setCurtPos(x, y);
		return new Position(x, y);
	}
	//绘制物体
	public void draw(Graphics g) {
		//if(!alive) return;
		Color c = g.getColor();
		g.setColor(color);
		g.fillOval(getCurtPosX(), getCurtPosY(), size, size);
		g.setColor(c);
	}
	//判断是否与坦克相交
	public boolean eaten(Tanks tank) {
		if(alive && tank.getRec().intersects(getRec())){
			tank.eat(type);
			alive = false;
			bg.items.remove(this);
			return true;
		}
		return false;
	}
	public void eaten(List<Tanks> tanks) {
		for (Tanks tanks2 : tanks) {
			eaten(tanks2);
		}
	}
}
