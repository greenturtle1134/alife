package test;

import physics.Ball;
import physics.Vector;
import physics.World;

public class Test {

	public static void main(String[] args) {
		World world = new World(40, 40);
		Ball ball1 = new Ball(world, new Vector(10, 20), new Vector(1, 0), 10);
		Ball ball2 = new Ball(world, new Vector(30, 20), new Vector(-1, 0), 10);
		world.addEntity(ball1);
		world.addEntity(ball2);
		
		for (int i=0; i<100; i++) {
			System.out.println("Ball 1: " + ball1.getPos() + " " + ball1.getVel() + " Ball 2: " + ball2.getPos() + " " + ball2.getVel());
			world.tick();
		}
	}

}
