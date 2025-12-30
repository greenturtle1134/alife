package test;

import java.util.List;

import physics.Vector;

public abstract class Dist2D<P extends Positionable> {
	
	protected List<P> elements;
	
	public Dist2D(List<P> elements) {
		this.elements = elements;
	}
	
	public abstract Iterable<P> query(Vector pos, double radius);
	
}
