package game;

import java.util.Random;

public enum ItemType {
	POWER, SPEED, BLOOD, ALLBLOOD, SKILL;
	public static ItemType randomItemType() {
		Random rand = new Random();
		ItemType[] allItems = ItemType.values();
		ItemType item = allItems[rand.nextInt(allItems.length)];
		return item;
	}
}

