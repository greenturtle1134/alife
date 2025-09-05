package physics;

import genome.DNA;
import genome.Program;

public class Cell extends PhysicsObject {
	private double facing;
	private double swimFB, swimLR;
	private DNA dna;
	private Program program;

	public Cell(World world, double x, double y, double facing, DNA dna) {
		super(world, x, y, 0, 0);
		this.facing = facing;
		this.swimFB = 0;
		this.swimLR = 0;
		this.dna = dna;
		this.program = Program.parseProgram(dna);
		// TODO: This uses a model where we parse a new program for each cell, which is not the one I want to use!
	}
	
	public double getFacing() {
		return facing;
	}
	
	public double setFacing() {
		return facing;
	}

	public double getSwimFB() {
		return swimFB;
	}

	public void setSwimFB(double swimFB) {
		this.swimFB = swimFB;
	}

	public double getSwimLR() {
		return swimLR;
	}

	public void setSwimLR(double swimLR) {
		this.swimLR = swimLR;
	}

	public DNA getDna() {
		return dna;
	}
	
	public void setDna(DNA dna) {
		this.dna = dna;
		this.program = Program.parseProgram(dna);
	}

	public Program getProgram() {
		return program;
	}

	public void tick() {
		// TODO: Extreme dummy implementation right now
		
		// Cell accelerates by swimming
		this.addForce(swimFB*Math.cos(facing) + swimLR*Math.sin(facing), swimFB*Math.sin(facing) + swimLR*Math.cos(facing));
	}
}
