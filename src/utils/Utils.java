package utils;

public class Utils {
	
	/**
	 * Rounds a double to an int. I coded this helper method because Math.round natively turns double into long and I kept needing an int.
	 */
	public static int round(double x) {
		return (int) Math.round(x);
	}
	
	public static final double FRACTION_THRESHOLD = 0.0001;
	
	/**
	 * Checks if a quantity is nearly zero (or negative)
	 */
	public static boolean nearZero(double x) {
		return x < FRACTION_THRESHOLD;
	}
	
	/**
	 * Linearly maps a double from 0 to 1 to an int between 0 and upper in a way such that values approximating 0 are mapped to 0 and approximating 1 are mapped to upper.
	 * Meant to compensate for floating point errors when converting doubles to ints.
	 * The width of these approximate regions is determined by FRACTION_THRESHOLD
	 * @param x - The quantity
	 * @param upper - The upper bound
	 * @return mapped value
	 */
	public static int fractionRound(double x, int upper) {
		if (0 <= x && x < FRACTION_THRESHOLD) {
			return 0;
		}
		if (x <= 1 && x > 1 - FRACTION_THRESHOLD) {
			return upper;
		}
		return (int) Math.floor(x * upper);
	}

}
