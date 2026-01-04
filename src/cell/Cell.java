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
	private double facing;
	
	private DNA dna;
	private Program program;
	private int i;
	
	private CellInternals internals;
	
	public Cell(
			World world,
			Vector pos,
			Vector vel,
			double facing,
			DNA dna,
			int i,
			CellInternals internals
			) {
		super(world, pos, vel);
		this.facing = facing;
		this.dna = dna;
		this.program = Program.parseProgram(dna);
		
		this.internals = internals;
		this.i = i;
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
	
	public CellInternals getInternals() {
		return internals;
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
		if (i < internals.memory.length) {
			return internals.memory[i];
		}
		else {
			return 0;
		}
	}
	
	public void memSet(int i, int x) {
		if (i < internals.memory.length) {
			internals.memory[i] = x;
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
		// TODO current imported from TestDraw
		
		Graphics g = c.getG();
		double zoom = c.getZoom();
		
		int x = round(this.pos.x * zoom);
		int y = round(this.pos.y * zoom);
		int r = round(this.radius() * zoom);
		g.drawOval(x - r, y - r, 2 * r, 2 * r);
		
		int x1 = round(Math.cos(facing) * this.radius() * zoom);
		int y1 = round(Math.sin(facing) * this.radius() * zoom);
		
		g.drawLine(x, y, x + x1, y + y1);
	}
	
	@Override
	public double radius() {
		// TODO implement radius
		return 10;
	}

	@Override
	public double mass() {
		// TODO implement mass
		return 1;
	}

	@Override
	public void tick() {
		this.run(1);
	}
	
	public Vector moveAcc() {
		return Vector.fromOrientation(facing, internals.moveF, internals.moveR);
	}
}
