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
}
