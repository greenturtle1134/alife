package cell;

public class WorldSettings {
	private double accelFactor; // One unit of cell acceleration request is this many units/tick^2 of acceleration. Everything else is just 1:1 for now
	private double dragFactor; // Multiplier of drag
	private double collisionFactor; // Multiplier on force applied to cells when colliding
	private double[] substanceCosts; // Cost to build one unit of a substance
	private double[] substanceRefunds; // Energy recouped by burning one unit of a substance
	private double[] substanceMaints; // Cost per tick to maintain one unit of substance
	private double[] capacityFactors; // Capacity of a substance is multiplied by this value
	private double movementCost; // Currently, cost to accelerate at 1 cell unit/tick^2; may change when flagella/cilia added
	private double rotationCost; // Cost of one unit of rotation (i.e. 1/64 of a half-revolution)

	public WorldSettings(double accelFactor, double dragFactor, double collisionFactor, double[] substanceCosts, double[] substanceRefunds, double[] substanceMaints,
			double[] capacityFactors, double movementCost, double rotationCost) {
		this.accelFactor = accelFactor;
		this.dragFactor = dragFactor;
		this.collisionFactor = collisionFactor;
		this.substanceCosts = substanceCosts;
		this.substanceRefunds = substanceRefunds;
		this.substanceMaints = substanceMaints;
		this.capacityFactors = capacityFactors;
		this.movementCost = movementCost;
		this.rotationCost = rotationCost;
	}
	
	public WorldSettings() {
		this.accelFactor = 1;
		this.dragFactor = 0.1;
		this.collisionFactor = 5;
		double[] substanceCosts = {1, 1, 0.1, 2};
		double[] substanceRefunds = {1, 0.8, 0.05, 1};
		double[] substanceMaints = {0, 0, 0, 0};
		double[] capacityFactors = {1, 1, 1, 1};
		this.substanceCosts = substanceCosts;
		this.substanceRefunds = substanceRefunds;
		this.substanceMaints = substanceMaints;
		this.capacityFactors = capacityFactors;
		this.movementCost = 1;
		this.rotationCost = 1;
	}

	public double getAccelFactor() {
		return accelFactor;
	}

	public void setAccelFactor(double distanceUnit) {
		this.accelFactor = distanceUnit;
	}

	public double getDragFactor() {
		return dragFactor;
	}

	public void setDragFactor(double dragFactor) {
		this.dragFactor = dragFactor;
	}

	public double getCollisionFactor() {
		return collisionFactor;
	}

	public void setCollisionFactor(double collisionFactor) {
		this.collisionFactor = collisionFactor;
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
}
