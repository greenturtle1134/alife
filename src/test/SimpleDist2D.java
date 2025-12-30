package test;

import java.util.LinkedList;
import java.util.List;

import physics.Vector;

public class SimpleDist2D<P extends Positionable> extends Dist2D<P> {

	public SimpleDist2D(List<P> elements) {
		super(elements);
	}

	@Override
	public Iterable<P> query(Vector pos, double radius) {
		List<P> res = new LinkedList<P>();
		for(P e : elements) {
			Vector p = e.getPos();
			if (Math.abs(pos.x - p.x) <= radius && Math.abs(pos.y - p.y) <= radius) {
				res.add(e);
			}
		}
		return res;
	}

}
