package test;

import physics.Ball;
import physics.Vector;
import physics.World;

public class Test {

	public static void main(String[] args) {
		// The first collision test
		World world = new World(40, 40);
		Ball ball1 = new Ball(world, new Vector(10, 19), new Vector(1, 0), 10);
		Ball ball2 = new Ball(world, new Vector(30, 21), new Vector(-1, 0), 10);
		world.addEntity(ball1);
		world.addEntity(ball2);
		
		for (int i=0; i<100; i++) {
			System.out.println("Ball 1: " + ball1.getPos() + " " + ball1.getVel() + " Ball 2: " + ball2.getPos() + " " + ball2.getVel());
			world.tick();
		}
		
//		// The copying speed test
//		Vector[] vectors = new Vector[1000];
//		long start = System.nanoTime();
//		for (int i=0; i<vectors.length; i++) {
//			vectors[i] = new Vector(0, 0);
//		}
//		double createTime = (System.nanoTime() - start) / 1e9;
//		Vector test = new Vector(Math.random(), Math.random());
//		start = System.nanoTime();
//		for (int i=0; i<vectors.length; i++) {
//			vectors[i].set(test);
//		}
//		double copyTime = (System.nanoTime() - start) / 1e9;
//		System.out.println(createTime + " v.s. " + copyTime);
	}

}
