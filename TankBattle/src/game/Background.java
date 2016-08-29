package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class Background extends JPanel{
	Random rand = new Random();
	Tanks tank;
	List<Missile> missiles = new LinkedList<Missile>();
	List<Explosion> exploxions = new LinkedList<Explosion>(); 
	List<Item> items = new LinkedList<Item>();
	List<Tanks> npcTanks = new LinkedList<Tanks>();
	TankType tankType = TankType.Tank;
	String[] tankTypeList = {"Tank", "LaserTank"};
	public void setTankType(TankType e) { tankType = e; }
	//构造函数
	public Background() {
		chooseTank();
		setLocation(0, MainFrame.MENUBARHEIGHT);
		setSize(900, 900);
		for(int i = 1; i <= 5; i++) {
			npcTanks.add(new NpcTanks(rand.nextInt(1000), rand.nextInt(1000), this));
		}
	}
	public void initial() {
		missiles.clear();
		exploxions.clear();
		items.clear();
		npcTanks.clear();
		chooseTank();
		for(int i = 1; i <= 5; i++) {
			npcTanks.add(new NpcTanks(rand.nextInt(1000), rand.nextInt(1000), this));
		}
	}
	//主要绘图函数
	public void paintComponent(Graphics g) {
		//创建背景
		super.paintComponent(g);
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
		tank.draw(g);      //创建我的坦克
		for (Tanks tank2 : npcTanks) {
			tank.collideTank(tank2);
		}
		for (int i = 0; i < npcTanks.size(); i++) {
			for (int j = i + 1; j < npcTanks.size(); j++) {
				npcTanks.get(i).collideTank(npcTanks.get(j));
			}
		}
		paintNpcTanks(g);
		paintMissiles(g); //绘制导弹
		paintExplosions(g); //绘制爆炸效果
		if(rand.nextInt(10000) <= 50) items.add(new Item(this)); //随掉落物品
		generatiItem(g);	//显示物品
		if(npcTanks.size() < 5)
			for(int i = 0; i < 1; i++) {
				npcTanks.add(new NpcTanks(rand.nextInt(this.getWidth()) , rand.nextInt(this.getHeight()), this));
			}
		}
	public void paintNpcTanks(Graphics g) {
		for(Tanks tank : npcTanks) {
			tank.draw(g);
		}
	}
	//绘制导弹
	public void paintMissiles(Graphics g) {
		for (Missile missile : missiles) {
			missile.outOfBackground(this);
			missile.hitTank(tank);
			missile.hitTank(npcTanks);
			missile.draw(g);
		}
	}
	//绘制爆炸效果
	public void paintExplosions(Graphics g) {
		for (Explosion explosion : exploxions) {
			explosion.hitTank(tank);
			explosion.hitTank(npcTanks);
			explosion.draw(g);
		}
	}
	//生成物品
	public void generatiItem(Graphics g) {
		for (Item item : items) {
			item.draw(g);
			item.eaten(tank);
			item.eaten(npcTanks);
		}
	}
	//选择坦克
	public void chooseTank() {	
		String str = (String) JOptionPane.showInputDialog(null, "Choose your tank!", "TankChoosing...",
				JOptionPane.QUESTION_MESSAGE, null, tankTypeList, null);
		if(str.equals("Tank")) {setTankType(TankType.Tank);}
		else if(str.equals("LaserTank")) {setTankType(TankType.LaserTank);}
		switch (tankType) {
			case Tank: tank = new Tanks(300, 300, this); break;
			case LaserTank:tank = new LaserTank(300, 300, this); break;
			default: break;
		}
	}
}
