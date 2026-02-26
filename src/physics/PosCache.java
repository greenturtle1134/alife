package physics;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;

/**
 * Stores entities with position for fast querying of entities around a particular point. Uses a hash table dividing the area into square cells.
 */
public class PosCache<T extends Positionable> {
	
	private int gridSize;
	private ListMultimap<IntPair, T> map;
	private HashMap<T, IntPair> reverseMap;
	
	public PosCache(int gridSize) {
		this.gridSize = gridSize;
		this.map = MultimapBuilder.hashKeys().linkedListValues().build();
		this.reverseMap = new HashMap<T, IntPair>();
	}
	
	/**
	 * Gets the grid cell corresponding to a certain vector. This is computed by casting both parameters to ints, then dividing by the gridSize.
	 * @param v - the Vector to compute
	 * @return IntPair this vector is associated with
	 */
	public IntPair getGrid(Vector v) {
		return new IntPair((int) v.x / gridSize, (int) v.y / gridSize);
	}
	
	/**
	 * Finds grid squares within a radius of a vector. Any grid square that overlaps this radius should be included.
	 * @param v - the vector to search from
	 * @param radius - the radius to search
	 * @return a Stream of the grid squares
	 */
	public Stream<IntPair> gridsInRadius(Vector v, double radius) {
		IntPair centerGrid = getGrid(v);
		int cx = centerGrid.x;
		int cy = centerGrid.y;
		int r = ((int) radius / gridSize) + 1;
		int n = 2*r+1;
		IntPair[] res = new IntPair[n * n];
		for (int x = 0; x < 2*r+1; x++) {
			for (int y = 0; y < 2*r+1; y++) {
				res[n*x+y] = new IntPair(cx - r + x, cy - r + y);
			}
		}
		return Arrays.stream(res);
	}
	
	/**
	 * Adds a new object to the cache
	 * @param c - object to insert
	 */
	public void add(T c) {
		IntPair grid = getGrid(c.pos());
		map.put(grid, c);
		reverseMap.put(c, grid);
	}
	
	/**
	 * Erases an object from the cache
	 * @param c - object to delete
	 */
	public void remove(T c) {
		IntPair grid = reverseMap.get(c); // Should always work if we correctly enforce object state; will throw a NullPointer if it somehow goes wrong
		map.remove(grid, c);
		reverseMap.remove(c);
	}
	
	/**
	 * 
	 */
	public void update() {
		for (Map.Entry<T, IntPair> entry : reverseMap.entrySet()) {
			IntPair current = getGrid(entry.getKey().pos());
			if (!(current.equals(entry.getValue()))) {
				map.remove(entry.getValue(), entry.getKey());
				map.put(current, entry.getKey());
				entry.setValue(current);
			}
		}
	}
	
	/**
	 * Queries all entities within a given radius of a given point
	 * @param pos - the point
	 * @param radius - the radius
	 * @return a Stream of entities within the radius
	 */
	public Stream<T> radiusQuery(Vector pos, double radius) {
		Stream<IntPair> gridCells = gridsInRadius(pos, radius);
		return gridCells.flatMap(x -> map.get(x).stream()).filter(x -> Vector.dist(x.pos(), pos) <= radius);
	}
	
	/**
	 * Queries all entities within a given radius of a given entity, excluding the entity itself.
	 * @param e - the entity to query
	 * @param radius - the radius
	 * @return a Stream of entities within the radius
	 */
	public Stream<T> radiusQuery(T e, double radius) {
		return radiusQuery(e.pos(), radius).filter(x -> x != e);
	}
	
	/**
	 * Deletes all entries in this cache
	 */
	public void clear() {
		map.clear();
		reverseMap.clear();
	}
	
	// OK it feels stupid because this has definitely been written before, but it makes sense here
	public static class IntPair {
		public final int x, y;
		
		public IntPair(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof IntPair)) {
				return false;
			}
			IntPair that = (IntPair) o;
			return (this.x == that.x) && (this.y == that.y);
		}
		
		@Override
		public int hashCode() {
			return 31 * x + y;
		}
		
		@Override
		public String toString() {
			return "(" + x + ", " + y + ")";
		}
	}
}
