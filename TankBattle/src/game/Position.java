package game;

public class Position {
	private int x;
	private int y;
	//构造函数
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public Position(Position pos) {
		x = pos.getX();
		y = pos.getY();
	}
	//封装与获取
	public int getX() { return x; }
	public void setX(int x) { this.x = x; }
	public int getY() { return y; }
	public void setY(int y) { this.y = y; }
	public void set(Position pos) {
		this.x = pos.getX();
		this.y = pos.getY();
	}
	public void add(int x, int y) {this.x += x; this.y += y; }
}
