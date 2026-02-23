package cell;

public enum Substance {
	NRG(0, "nrg"),
	BODY(1, "body"),
	NUCLEIC(2, "nucleic"),
	CHLOROPHYLL(3, "chloro");
	public static final int SUBSTANCE_COUNT = Substance.values().length;

	public final int id;
	public final String shortName;
	private Substance(int id, String shortName) {
		this.id = id;
		this.shortName = shortName;
	}
	
	public static int numToID(int x) {
		switch (x) {
		case 0:
			return NRG.id;
		case 1:
			return BODY.id;
		case 2:
			return NUCLEIC.id;
		case 3:
			return CHLOROPHYLL.id;
		}
		return -1;
	}
}
