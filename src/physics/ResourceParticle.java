package physics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Map;

import cell.Cell;
import cell.Substance;
import display.DrawContext;

public class ResourceParticle extends Particle {
	
	Map<Substance, Double> contents;

	public ResourceParticle(World world, Vector pos, Vector vel, Entity owner, long createdTime, Map<Substance, Double> contents) {
		super(world, pos, vel, owner, createdTime);
		this.contents = contents;
	}
	
	@Override
	public boolean interact(Cell e) {
		return false;
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
