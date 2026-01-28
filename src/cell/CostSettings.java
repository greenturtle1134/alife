package cell;

public class CostSettings {
	private double[] substanceCosts;
	private double[] substanceRefunds;
	private double[] substanceMaints;
	private double[] capacityFactors;
	private double movementCost; // Cost per unit of move intent?
	private double rotationCost; // Cost to do a full rotation

	public CostSettings(double[] substanceCosts, double[] substanceRefunds, double[] substanceMaints,
			double[] capacityFactors, double movementCost, double rotationCost) {
		this.substanceCosts = substanceCosts;
		this.substanceRefunds = substanceRefunds;
		this.substanceMaints = substanceMaints;
		this.capacityFactors = capacityFactors;
		this.movementCost = movementCost;
		this.rotationCost = rotationCost;
	}

	public double getCost(int s) {
		return substanceCosts[s];
	}
	
	public void setCost(int s, double x) {
		substanceCosts[s] = x; 
	}
	
	public double getRefund(int s) {
		return substanceRefunds[s];
	}
	
	public void setRefund(int s, double x) {
		substanceRefunds[s] = x; 
	}
	
	public double getMaint(int s) {
		return substanceMaints[s];
	}
	
	public void setMaint(int s, double x) {
		substanceMaints[s] = x; 
	}

	public double getCapacityFactor(int s) {
		return capacityFactors[s];
	}
	
	public void setCapacityFactor(int s, double x) {
		capacityFactors[s] = x; 
	}
	
	public double getMovementCost() {
		return movementCost;
	}

	public void setMovementCost(double movementCost) {
		this.movementCost = movementCost;
	}

	public double getRotationCost() {
		return rotationCost;
	}

	public void setRotationCost(double rotationCost) {
		this.rotationCost = rotationCost;
	}

	public static CostSettings defaults() {
		// Substances: Nrg, Body, DNA, Chlorophyll
		double[] substanceCosts = {1, 1, 0.1, 2};
		double[] substanceRefunds = {1, 0.8, 0.05, 1};
		double[] substanceMaints = {0, 0, 0, 0};
		double[] capacityFactors = {1, 1, 1, 1};
		return new CostSettings(substanceCosts, substanceRefunds, substanceMaints, capacityFactors, 1, 0.1);
	}
}
