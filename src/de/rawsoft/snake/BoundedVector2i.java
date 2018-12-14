package de.rawsoft.snake;

public class BoundedVector2i extends Vector2i {

	private int boundX;
	private int boundY;

	public BoundedVector2i(int boundX, int boundY) {
		this.boundX = boundX;
		this.boundY = boundY;
	}

	public BoundedVector2i(int x, int y, int boundX, int boundY) {
		this(boundX, boundY);
		this.x = x;
		this.y = y;
	}

	@Override
	public void addX(int x) {
		int newX = this.x + x;
		newX = newX > boundX ? 0 : newX < 0 ? boundX : newX;
		this.x = newX;
	}

	@Override
	public void addY(int y) {
		int newY = this.y + y;
		newY = newY > boundY ? 0 : newY < 0 ? boundY : newY;
		this.y = newY;
	}

	@Override
	public void setX(int x) {
		x = x > boundY ? 0 : x < 0 ? boundY : x;
		this.x = x;
	}

	@Override
	public void setY(int y) {
		y = y > boundY ? 0 : y < 0 ? boundY : y;
		this.y = y;
	}

	public Vector2i toUnboundVector() {
		return new Vector2i(this.x, this.y);
	}

	@Override
	public BoundedVector2i clone() {
		return new BoundedVector2i(getX(), getY(), boundX, boundY);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BoundedVector2i && ((BoundedVector2i) obj).getX() == getX() && ((BoundedVector2i) obj).getY() == getY())
			return true;
		return false;
	}
}
