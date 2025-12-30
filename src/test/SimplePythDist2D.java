package test;

import java.util.LinkedList;
import java.util.List;

import physics.Vector;

public class SimplePythDist2D<P extends Positionable> extends Dist2D<P> {

	public SimplePythDist2D(List<P> elements) {
		super(elements);
	}

	@Override
	public Iterable<P> query(Vector pos, double radius) {
		List<P> res = new LinkedList<P>();
		for(P e : elements) {
			Vector p = e.getPos();
			if (Vector.dist(p,  pos) <= radius) {
				res.add(e);
			}
		}
		return res;
	}

}
