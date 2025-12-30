package test;

import java.util.ArrayList;
import java.util.List;

import physics.Vector;

public class DistSpeedTest {
	
	public static class TestPoint implements Positionable {
		private Vector pos;
		
		public TestPoint() {
			this.pos = new Vector(Math.random(), Math.random());
		}
		
		@Override
		public Vector getPos() {
			return pos;
		}
	}
	
	public static void runTest(int N, int Q, double radius) {
		List<TestPoint> points = new ArrayList<TestPoint>();
		for (int i=0; i<N; i++) {
			points.add(new TestPoint());
		}
		
		Vector[] queries = new Vector[Q];
		for (int i=0; i<Q; i++) {
			queries[i] = new Vector(Math.random(), Math.random());
		}
		
		System.out.println("N=" + N + ", Q=" + Q + ", r=" + radius);
		
//		double[] times = new double[Q];
		long start = System.nanoTime();
		Dist2D<TestPoint> test = new SimplePythDist2D<TestPoint>(points);
		double setupTime = System.nanoTime() - start;
		System.out.println("Setup time: " + setupTime / 1000000 + " ms");

		double sum = 0;
		double sqsum = 0;
		for (int i=0; i<Q; i++) {
			start = System.nanoTime();
			test.query(queries[i], radius);
			double thisTime = System.nanoTime() - start;
			sum += thisTime;
			sqsum += thisTime * thisTime;
		}
		
		System.out.println("Time per query: " + (sum / Q / 1000000) + "ms");
	}

	public static void main(String[] args) {
		runTest(10000, 10000, 0.1);
	}

}
