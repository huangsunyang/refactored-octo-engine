package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class NpcTanks extends Tanks{
	protected int step = 5;
	//构造函数
	public NpcTanks(int x, int y, Background bg) {
		super(x, y, bg);
		speed = 1;
		tankColor = new Color(102, 186, 183);
		group = Group.NPC;
		bloodbar = new BloodBar(this);
		weight = 4;
	}
	public void move() {
		super.move();
		speed = 1;
		tankColor = new Color(102, 186, 183);;
		group = Group.NPC;
		Random rand = new Random();
		if(step == 0) {
			step = rand.nextInt(50) + 25;
			speedDir = Direction.randomDirection();
		}
		if(collideWall()) speedDir = Direction.counterDirection(speedDir);
		step--;
		if(rand.nextInt(10000) < 180) fire();
	}
	public void draw(Graphics g) {
		if(!isAlive()) {
			bg.npcTanks.remove(this);
			bg.tank.addKillNum(1);
		}
		super.draw(g);
	}
	
}
