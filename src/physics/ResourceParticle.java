package physics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cell.Cell;
import cell.Substance;
import display.DrawContext;

public class ResourceParticle extends Particle {
	
	Map<Substance, Double> contents;

	public ResourceParticle(World world, Vector pos, Vector vel, Entity owner, long createdTime, Map<Substance, Double> contents) {
		super(world, pos, vel, owner, createdTime);
		this.contents = contents;
	}
	
	public ResourceParticle(World world, Vector pos, Vector vel, Entity owner, long createdTime, Substance s, double v) {
		super(world, pos, vel, owner, createdTime);
		this.contents = new HashMap<>();
		this.contents.put(s, v);
	}
	
	@Override
	public boolean interact(Cell e) {
		for (Entry<Substance, Double> entry: contents.entrySet()) {
			e.addSubstance(entry.getKey().id, entry.getValue());
		}
		return true;
	}
	
	@Override
	public void draw(DrawContext c) {
		Graphics g = c.g;
		g.setColor(Color.BLACK);
		
		int x = c.toZoom(this.pos.x);
		int y = c.toZoom(this.pos.y);
		
		g.fillRect(x, y, 1, 1);
	}

}
