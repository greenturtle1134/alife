package display;

public interface StatusReporter {
	public void set(String s);
	
	public static StatusReporter NULL = s -> {};
}
