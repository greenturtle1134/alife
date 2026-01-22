package cell;

public class CostSettings {
	private double[] substanceCosts;
	private double[] substanceRefunds;
	private double[] substanceMaints;
	private double[] capacityFactors;
	
	public CostSettings(
			double[] substanceCosts,
			double[] substanceRefunds,
			double[] substanceMaints,
			double[] capacityFactors
			) {
		this.substanceCosts = substanceCosts;
		this.substanceRefunds = substanceRefunds;
		this.substanceMaints = substanceMaints;
		this.capacityFactors = capacityFactors;
	}

	public double getCost(Substance s) {
		return substanceCosts[s.id];
	}
	
	public void setCost(Substance s, double x) {
		substanceCosts[s.id] = x; 
	}
	
	public double getRefund(Substance s) {
		return substanceRefunds[s.id];
	}
	
	public void setRefund(Substance s, double x) {
		substanceRefunds[s.id] = x; 
	}
	
	public double getMaint(Substance s) {
		return substanceMaints[s.id];
	}
	
	public void setMaint(Substance s, double x) {
		substanceMaints[s.id] = x; 
	}

	public double getCapacityFactor(Substance s) {
		return capacityFactors[s.id];
	}
	
	public void setCapacityFactor(Substance s, double x) {
		capacityFactors[s.id] = x; 
	}
	
	public static CostSettings defaults() {
		double[] substanceCosts = {1, 1, 0.1, 2};
		double[] substanceRefunds = {1, 0.8, 0, 1};
		double[] substanceMaints = {0, 0, 0, 0};
		double[] capacityFactors = {1, 1, 1, 1};
		return new CostSettings(substanceCosts, substanceRefunds, substanceMaints, capacityFactors);
	}
}
