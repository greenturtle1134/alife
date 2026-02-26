package physics;

import java.awt.Color;
import java.awt.Graphics;

import display.DrawContext;

public class Particle {
	public World world;
	public Vector pos;
	public Vector vel;
	public double lifespan;
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

	public void draw(DrawContext c) {
		Graphics g = c.g;
		g.setColor(Color.BLACK);
		
		int x = c.toZoom(this.pos.x);
		int y = c.toZoom(this.pos.y);
		
		g.fillRect(x, y, 1, 1);
	}
}
