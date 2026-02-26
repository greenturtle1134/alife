package physics;

import cell.Cell;
import display.DrawContext;

public abstract class Particle {
	public World world;
	public Vector pos;
	public Vector vel;
	public long createdTime;
	
	public Particle(World world, Vector pos, Vector vel, long createdTime) {
		this.world = world;
		this.pos = pos;
		this.vel = vel;
		this.createdTime = createdTime;
	}
	
	public Particle(World world, Vector pos, Vector vel) {
		this(world, pos, vel, world.time());
	}

	public abstract void draw(DrawContext c);
	
	public abstract boolean isDead();
	
	public abstract void interactCell(Cell c);

//	Graphics g = c.g;
//	g.setColor(Color.BLACK);
//	
//	int x = c.toZoom(this.pos.x);
//	int y = c.toZoom(this.pos.y);
//	
//	g.fillRect(x, y, 1, 1);
}
