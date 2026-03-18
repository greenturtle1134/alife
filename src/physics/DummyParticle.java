package physics;

import java.awt.Color;
import java.awt.Graphics;

import cell.Cell;
import display.DrawContext;

public class DummyParticle extends Particle {

	public DummyParticle(World world, Vector pos, Vector vel, Entity owner, long createdTime) {
		super(world, pos, vel, owner, createdTime);
	}

	@Override
	public boolean interact(Cell e) {
		System.out.println(e.pos());
		return false;
	}
	
	@Override
	public void draw(DrawContext c) {
		Graphics g = c.g;
		g.setColor(Color.BLACK);
		
		int x = c.toZoom(this.pos.x);
		int y = c.toZoom(this.pos.y);
		
		g.fillRect(x-1, y-1, 3, 3);
	}

}
