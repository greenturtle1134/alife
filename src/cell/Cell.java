package cell;

import static utils.Utils.round;

import java.awt.Graphics;

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
	
	// "Internals" - fully under control of program
	public int[] memory;
	public double moveF;
	public double moveR;
	
	// "State" - physical state of cell; currently described using just the substance system
	public double[] substances;
	
	/**
	 * "Full constructor": creates a Cell object by specifying all fields
	 */
	public Cell(
			World world,
			Vector pos,
			Vector vel,
			double facing,
			DNA dna,
			int i,
			double[] substances,
			int[] memory,
			double moveF,
			double moveR
			) {
		super(world, pos, vel);
		this.facing = facing;
		this.dna = dna;
		this.i = i;
		this.program = Program.parseProgram(dna);
		this.substances = substances;
		this.memory = memory;
		this.moveF = moveF;
		this.moveR = moveR;
	}
	
	/**
	 * "Physical constructor": initializes Cell with only physical values, internal values set to defaults
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
		this.program = Program.parseProgram(dna);
		this.substances = substances;
		this.memory = new int[64];
		this.moveF = 0;
		this.moveR = 0;
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
		this.program = Program.parseProgram(dna);
		this.substances = new double[Substance.SUBSTANCE_COUNT];
		substances[0] = nrg;
		substances[1] = body;
		this.memory = new int[64];
		this.moveF = 0;
		this.moveR = 0;
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

	public void run(int steps) {
		for (int j = 0; j < steps; j++) {
			this.program.getStatements()[this.i].exec(this);
			this.i++;
			if (this.i >= this.program.getStatements().length) {
				this.i = 0;
			}
		}
	}

	public int memGet(int i) {
		if (i < memory.length) {
			return memory[i];
		}
		else {
			return 0;
		}
	}
	
	public void memSet(int i, int x) {
		if (i < memory.length) {
			memory[i] = x;
		}
	}
	
	public void deltaJump(int x) {
		this.i += x;
	}
	
	public void labelJump(int label) {
		if (this.program.getLabels().containsKey(label)) {
			this.i = this.program.getLabels().get(label);
		}
	}

	@Override
	public void draw(DrawContext c) {
		Graphics g = c.getG();
		double zoom = c.getZoom();
		
		int x = round(this.pos.x * zoom);
		int y = round(this.pos.y * zoom);
		int r = round(this.radius() * zoom);
		g.drawOval(x - r, y - r, 2 * r, 2 * r);
		
		int x1 = round(Math.cos(facing) * this.radius() * zoom);
		int y1 = round(Math.sin(facing) * this.radius() * zoom);
		g.drawLine(x, y, x + x1, y + y1);
		
		drawArc(c, x, y, this.nrg() / this.getCapacity(Substance.NRG), 0.5);
	}
	
	public void drawArc(DrawContext c, int x, int y, double arcRatio, double arcRadius) {
		Graphics g = c.getG();
		double zoom = c.getZoom();
		
		int r = round(this.radius() * arcRadius * zoom);
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
	
	public Vector moveAcc() {
		return Vector.fromOrientation(facing, moveF, moveR);
	}
	
	public double getCapacity(Substance s) {
		if (s == Substance.BODY) {
			// Body has no cap and this should never be called. TODO: should this return a special value or throw an exception?
			return Double.POSITIVE_INFINITY;
		}
		else {
			// Default assuming linear capacity all substances. Nonlinear may be added later
			return body() * world.costSettings.getCapacityFactor(s);
		}
	}

	public double nrg() {
		return substances[Substance.NRG.id];
	}

	public double body() {
		return substances[Substance.BODY.id];
	}
	
	/**
	 * Builds a substance. Takes into account remaining capacity and nrg available. Redirects to burn if amount is negative. Forbids building nonsensical substances.
	 * @param s - Substance to build
	 * @param amount - Amount of substance to build
	 */
	public void build(Substance s, double amount) {
		if (s == Substance.NRG) {
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
		if (substances[s.id] + amount > getCapacity(s)) {
			amount = getCapacity(s) - substances[s.id];
		}
		
		// Verified that nrg will not go negative and substance will not exceed capacity; do the build
		this.substances[s.id] += amount;
		this.substances[Substance.NRG.id] -= amount * world.costSettings.getCost(s);
	}
	
	/**
	 * Burns a substance. Takes into account remaining substance. If burning requires energy, remaining nrg taken into account as well. Forbids burning nonsensical substances.
	 * @param s - Substance to burn
	 * @param amount - Amount of substance to burn
	 */
	public void burn(Substance s, double amount) {
		if (s == Substance.NRG) {
			return;
		}
		if (amount == 0.0) {
			return;
		}
		if (amount < 0) {
			burn(s, -amount);
		}
		
		// If insufficient substance, clamp it
		if (substances[s.id] < amount) {
			amount = substances[s.id];
		}
		
		// If costs energy to burn and insufficient nrg, clamp it
		if (world.costSettings.getRefund(s) < 0 && nrg() < -world.costSettings.getRefund(s) * amount) {
			amount = nrg() / (-world.costSettings.getRefund(s));
		}
		
		// Verified that this amount can be burned
		this.substances[s.id] -= amount;
		
		// Give or take energy, or set to the max if overflow
		if (nrg() + world.costSettings.getRefund(s) * amount < getCapacity(Substance.NRG)) {
			this.substances[Substance.NRG.id] += world.costSettings.getRefund(s) * amount;
		}
		else {
			this.substances[Substance.NRG.id] = getCapacity(Substance.NRG);
		}
	}
}
