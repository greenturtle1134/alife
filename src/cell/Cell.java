package cell;

import static utils.Utils.round;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;

import display.DrawContext;
import genome.DNA;
import genome.Program;
import physics.BallEntity;
import physics.Vector;
import physics.World;

public class Cell extends BallEntity {
	/**
	 * Facing direction of the cell, in radians from the x-axis
	 */
	private double facing;
	
	// DNA and program variables
	private DNA dna;
	private Program program;
	private int i;
	private int wait;
	
	// "Internals" - fully under control of program
	public int[] memory;
	public double moveF;
	public double moveR;
	public double rotRequest;
	
	// "State" - physical state of cell; currently described using just the substance system
	public double[] substances;
	
	/**
	 * "Full constructor": creates a Cell object by specifying all fields. Copies of the substance and memory arrays will be made.
	 */
	public Cell(
			World world,
			Vector pos,
			Vector vel,
			double facing,
			DNA dna,
			int i,
			int wait,
			double[] substances,
			int[] memory,
			double moveF,
			double moveR,
			double rotRequest
			) {
		super(world, pos, vel);
		this.facing = facing;
		this.dna = dna;
		this.i = i;
		this.setWait(wait);
		this.program = Program.parseProgram(dna);
		this.substances = Arrays.copyOf(substances, Substance.SUBSTANCE_COUNT);
		this.memory = Arrays.copyOf(memory, 64);
		this.moveF = moveF;
		this.moveR = moveR;
		this.rotRequest = rotRequest;
	}
	
	/**
	 * "Physical constructor": initializes Cell with only physical values, internal values set to defaults. A copy of the substance array will be made.
	 */
	public Cell(
			World world,
			Vector pos,
			Vector vel,
			double facing,
			DNA dna,
			double[] substances
			) {
		super(world, pos, vel);
		this.facing = facing;
		this.dna = dna;
		this.i = 0;
		this.setWait(0);
		this.program = Program.parseProgram(dna);
		this.substances = Arrays.copyOf(substances, Substance.SUBSTANCE_COUNT);;
		this.memory = new int[64];
		this.moveF = 0;
		this.moveR = 0;
		this.rotRequest = 0;
	}
	
	/**
	 * "Nrg-body constructor": initializes a cell with only standard resources and defaults the rest of the substances
	 */
	public Cell(
			World world,
			Vector pos,
			Vector vel,
			double facing,
			DNA dna,
			double nrg,
			double body
			) {
		super(world, pos, vel);
		this.facing = facing;
		this.dna = dna;
		this.i = 0;
		this.setWait(0);
		this.program = Program.parseProgram(dna);
		this.substances = new double[Substance.SUBSTANCE_COUNT];
		substances[0] = nrg;
		substances[1] = body;
		this.memory = new int[64];
		this.moveF = 0;
		this.moveR = 0;
		this.rotRequest = 0;
	}
	
	public double getFacing() {
		return facing;
	}

	public void setFacing(double facing) {
		this.facing = facing;
	}

	public int getI() {
		return i;
	}
	
	public void setI(int i) {
		this.i = i;
	}

	public DNA getDna() {
		return dna;
	}

	public Program getProgram() {
		return program;
	}

	public int getWait() {
		return wait;
	}

	public void setWait(int wait) {
		this.wait = wait;
	}
	
	public double getSubstance(int s) {
		return this.substances[s];
	}
	
	public void setSubstance(int s, double x) {
		// This method will NOT enforce limits for now
		this.substances[s] = x;
	}
	
	/**
	 * Changes substance amount; unlike the setSubstance method this DOES respect limits.
	 * Negatives values can be supplied to remove
	 * @param s - substance ID to modify
	 * @param x - amount to change it by
	 * @return the amount by which the substance was successfully changed, in case it's necessary
	 */
	public double addSubstance(int s, double x) {
		if (substances[s] + x > getCapacity(s)) {
			x = getCapacity(s) - substances[s];
		}
		else if (substances[s] + x < 0) {
			x = substances[s];
		}
		substances[s] += x;
		return x;
	}

	/**
	 * Runs the cell's program for a number of steps.
	 * This method should only perform computation, no updates to physical variables.
	 * @param steps = Number of steps to run for
	 */
	public void run(int steps) {
		for (int j = 0; j < steps; j++) {
			if (this.wait > 0) { // Either because this cycle set it, or it's left over from another cycle
				break;
			}
			this.program.getStatements()[this.i].exec(this);
			this.i++;
			if (this.i >= this.program.getStatements().length) {
				this.i = 0;
			}
		}
		if (this.wait > 0) {
			this.wait--; // Wait of 1 only stops the current execution
		}
	}

	/**
	 * Gets cell's memory at a particular index
	 * @param i - index to retrieve
	 * @return value of memory at that index
	 */
	public int memGet(int i) {
		if (i < memory.length) {
			return memory[i];
		}
		else {
			return 0;
		}
	}
	
	/**
	 * Writes to cell memory
	 * @param i - index to write to
	 * @param x = value to write
	 */
	public void memSet(int i, int x) {
		if (i < memory.length) {
			memory[i] = x;
		}
	}
	
	/**
	 * Moves the execution pointer forward or backward by a given amount
	 * @param x - amount to change it by
	 */
	public void deltaJump(int x) {
		this.i += x;
	}
	
	/**
	 * Jumps to a particular label.
	 * Specifically, sets the pointer to that label; the default +1 advancement then moves it to the command after that label.
	 * @param label - label to jump to
	 */
	public void labelJump(int label) {
		if (this.program.getLabels().containsKey(label)) {
			this.i = this.program.getLabels().get(label);
		}
	}

	@Override
	public void draw(DrawContext c) {
		Graphics g = c.getG();
		
		g.setColor(Color.BLACK);
		
		int x = c.toZoom(this.pos.x);
		int y = c.toZoom(this.pos.y);
		int r = c.toZoom(this.radius());
		g.drawOval(x - r, y - r, 2 * r, 2 * r);
		
		int x1 = c.toZoom(Math.cos(facing) * this.radius());
		int y1 = c.toZoom(Math.sin(facing) * this.radius());
		g.drawLine(x, y, x + x1, y + y1);
		
		drawArc(c, x, y, this.nrg() / this.getCapacity(Substance.NRG.id), 0.5);
		
		g.setColor(Color.GREEN);
		drawArc(c, x, y, this.getSubstance(Substance.CHLOROPHYLL.id) / this.getCapacity(Substance.CHLOROPHYLL.id), 0.8);
	}
	
	/**
	 * Helper to draw an indicator arc within the cell
	 * @param c - DrawContext to use
	 * @param x = x location of cell center
	 * @param y - y location of cell center
	 * @param arcRatio - value to display on the arc, from 0 to 1
	 * @param arcRadius - fraction of the cell's radius to use for the arc, from 0 to 1
	 */
	public void drawArc(DrawContext c, int x, int y, double arcRatio, double arcRadius) {
		Graphics g = c.getG();
		
		int r = c.toZoom(this.radius() * arcRadius);
		g.drawArc(x - r, y - r, 2 * r, 2 * r, round(-(facing / Math.PI + arcRatio) * 180), round(360 * arcRatio));
	}
	
	@Override
	public double radius() {
		return Math.sqrt(body());
	}

	@Override
	public double mass() {
		return body();
	}

	@Override
	public void tick() {
		this.run(1);
	}
	
	/**
	 * Retrieves the move intent of this cell.
	 * Takes into account movement limits but not any scaling factor
	 * (i.e. result is in "cell's program space").
	 */
	public Vector moveAcc() {
		// TODO insert limit control here
		return Vector.fromOrientation(facing, moveF, moveR);
	}
	
	/**
	 * Retrieves the rotation intent of this cell, from -1 to 1 in units of revolutions
	 */
	public double rotation() {
		// TODO insert limit control here
		return this.rotRequest;
	}
	
	/**
	 * Gets the cell's capacity for a substance.
	 * This is the actual physical capacity; exceptions in the program level are handled elsewhere.
	 * Uncapped quantities (i.e. body) will return Double.POSITIVE_INFINITY.
	 * @param s - the substance to query
	 * @return the capacity
	 */
	public double getCapacity(int s) {
		if (s == Substance.BODY.id) {
			// Body has no cap and this should never be called.
			return Double.POSITIVE_INFINITY;
		}
		else {
			// Default assuming linear capacity all substances. Nonlinear may be added later
			return body() * world.costSettings.getCapacityFactor(s);
		}
	}
	
	/**
	 * Gets current nrg of the cell
	 */
	public double nrg() {
		return substances[Substance.NRG.id];
	}
	
	/**
	 * Gets current body of the cell
	 */
	public double body() {
		return substances[Substance.BODY.id];
	}
	
	/**
	 * Builds a substance. Takes into account remaining capacity and nrg available.
	 * Redirects to burn if amount is negative. Forbids building nonsensical substances.
	 * @param s - Substance to build
	 * @param amount - Amount of substance to build
	 */
	public void build(int s, double amount) {
		if (s == Substance.NRG.id) {
			return;
		}
		if (amount == 0.0) {
			return;
		}
		if (amount < 0) {
			burn(s, -amount);
		}
		
		// If insufficient nrg, clamp it
		if (nrg() < amount * world.costSettings.getCost(s)) {
			amount = nrg() / world.costSettings.getCost(s);
		}
		
		// If insufficient capacity, clamp it
		// Concerning DNA: I think I'm going to keep the getCapacity as the true capacity and make an exception for reported capacity
		if (substances[s] + amount > getCapacity(s)) {
			amount = getCapacity(s) - substances[s];
		}
		
		// Verified that nrg will not go negative and substance will not exceed capacity; do the build
		this.substances[s] += amount;
		this.substances[Substance.NRG.id] -= amount * world.costSettings.getCost(s);
	}
	
	/**
	 * Burns a substance. Takes into account remaining substance. If burning requires energy, remaining nrg taken into account as well. Forbids burning nonsensical substances.
	 * @param s - Substance to burn
	 * @param amount - Amount of substance to burn
	 */
	public void burn(int s, double amount) {
		if (s == Substance.NRG.id) {
			return; // NRG cannot be burnt, that's stupid
		}
		if (amount == 0.0) {
			return;
		}
		if (amount < 0) {
			burn(s, -amount);
		}
		
		// If insufficient substance, clamp it
		if (substances[s] < amount) {
			amount = substances[s];
		}
		
		// If costs energy to burn and insufficient nrg, clamp it
		if (world.costSettings.getRefund(s) < 0 && nrg() < -world.costSettings.getRefund(s) * amount) {
			amount = nrg() / (-world.costSettings.getRefund(s));
		}
		
		// Verified that this amount can be burned
		this.substances[s] -= amount;
		
		// Give or take energy, or set to the max if overflow
		if (nrg() + world.costSettings.getRefund(s) * amount < getCapacity(Substance.NRG.id)) {
			this.substances[Substance.NRG.id] += world.costSettings.getRefund(s) * amount;
		}
		else {
			this.substances[Substance.NRG.id] = getCapacity(Substance.NRG.id);
		}
	}
	
	/**
	 * Get the total nrg spend by this cell on this tick, given its current movement intents and substance counts
	 * @return
	 */
	public double costs() {
		// TODO Very simplistic cost model right now! When buildables are added will have to take them into account
		double res = (Math.abs(moveF) + Math.abs(moveR)) * world.costSettings.getMovementCost() + this.rotRequest * world.costSettings.getRotationCost();
		for (int i = 0; i<substances.length; i++) {
			res += substances[i] * world.costSettings.getMaint(i);
		}
		return res;
	}
}
