package utils;

public class Utils {
	
	/**
	 * Rounds a double to an int. I coded this helper method because Math.round natively turns double into long and I kept needing an int.
	 */
	public static int round(double x) {
		return (int) Math.round(x);
	}

}
