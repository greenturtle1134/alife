package cell;

public enum Substance {
	NRG(0),
	BODY(1),
	NUCLEIC(2),
	CHLOROPHYLL(3);
	public static final int SUBSTANCE_COUNT = 4;

	public final int id;
	private Substance(int id) {
		this.id = id;
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
