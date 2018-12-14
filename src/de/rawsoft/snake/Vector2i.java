package de.rawsoft.snake;

public class Vector2i {

	protected int x;
	protected int y;

	public Vector2i() {}

	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void add(int x, int y) {
		addX(x);
		addY(y);
	}

	public void addX(int x) {
		this.x += x;
	}

	public void addY(int y) {
		this.y += y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public Vector2i clone() {
		return new Vector2i(getX(), getY());
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Vector2i && ((Vector2i) obj).getX() == getX() && ((Vector2i) obj).getY() == getY())
			return true;
		return false;
	}
}
