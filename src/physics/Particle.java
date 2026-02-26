package physics;

import java.awt.Color;
import java.awt.Graphics;

import cell.Cell;
import display.DrawContext;
import display.Drawable;

public abstract class Particle extends Entity implements Positionable, Drawable {
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
	
	public abstract void interactCell(Cell c);
	
	@Override
	public Vector pos() {
		return pos;
	}
	
	@Override
	public void draw(DrawContext c) {
		// Sample implementation stored here for personal convenience
		// Will be deleted later
		Graphics g = c.g;
		g.setColor(Color.BLACK);
		
		int x = c.toZoom(this.pos.x);
		int y = c.toZoom(this.pos.y);
		
		g.fillRect(x, y, 1, 1);
	}

}
