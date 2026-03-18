package physics;

import java.awt.Color;
import java.awt.Graphics;

import cell.Cell;
import display.DrawContext;
import display.Drawable;

/**
 * A Particle is a point entity that travels in straight lines and interacts with cells it contacts.
 * A field createdTime is provided to facilitate
 * 
 */
public abstract class Particle extends Entity implements Positionable, Drawable {
	public Vector pos;
	public Vector vel;
	public long createdTime;
	public Entity owner;
	
	public Particle(World world, Vector pos, Vector vel, Entity owner, long createdTime) {
		super(world);
		this.pos = pos;
		this.vel = vel;
		this.owner = owner;
		this.createdTime = createdTime;
	}
	
	public Particle(World world, Vector pos, Vector vel, Entity owner) {
		this(world, pos, vel, owner, world.time());
	}
	
	public abstract boolean interactCell(Cell c);
	
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
