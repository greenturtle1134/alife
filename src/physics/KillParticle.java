package physics;

import java.awt.Color;
import java.awt.Graphics;

import cell.Cell;
import display.DrawContext;

public class KillParticle extends Particle {
	
	public KillParticle(World world, Vector pos, Vector vel, Entity owner, long createdTime) {
		super(world, pos, vel, owner, createdTime);
	}

	@Override
	public boolean interact(Cell e) {
		if (!e.isDead()) {
			e.kill();
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public void draw(DrawContext c) {
		Graphics g = c.g;
		g.setColor(Color.RED);
		
		int x = c.toZoom(this.pos.x);
		int y = c.toZoom(this.pos.y);
		
		g.fillRect(x-1, y-1, 3, 3);
	}

}
