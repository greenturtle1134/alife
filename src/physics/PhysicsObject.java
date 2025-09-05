package physics;

public abstract class PhysicsObject {
	protected World world;
	private double x, y;
	private double dx, dy;
	private double ddx, ddy;

	public PhysicsObject(World world, double x, double y, double dx, double dy) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.ddx = 0;
		this.ddy = 0;
	}

	public World getWorld() {
		return world;
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getDx() {
		return dx;
	}

	public void setDx(double dx) {
		this.dx = dx;
	}

	public double getDy() {
		return dy;
	}

	public void setDy(double dy) {
		this.dy = dy;
	}

	public void addForce(double ddx, double ddy) {
		this.ddx += ddx;
		this.ddy += ddy;
	}

	public void move() {
		this.dx += this.ddx;
		this.dy += this.ddy;
		this.ddx = 0;
		this.ddy = 0;
		this.x += this.dx;
		this.y += this.dy;
	}

}
