package game;

import java.util.Random;

public enum Direction {
	L, LU, LD, R, RU, RD, U, D, STOP;
	public static Direction randomDirection() {
		Random rand = new Random();
		Direction[] allItems = Direction.values();
		Direction item = allItems[rand.nextInt(allItems.length)];
		return item;
	}
	public static Direction counterDirection(Direction dir) {
		switch (dir) {
			case L: return Direction.R; 
			case U: return Direction.D; 
			case R: return Direction.L; 
			case D: return Direction.U; 
			case LU: return Direction.RD; 
			case LD: return Direction.RU; 
			case RU: return Direction.LD;
			case RD: return Direction.LU; 
			default: return Direction.STOP;
		}
	}
}
