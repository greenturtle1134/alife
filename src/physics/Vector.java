package physics;

// Fine I'll do it myself
public class Vector {
	public double x, y;
	
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Vector addition, mutating this vector
	 * @param that - vector to add
	 * @return itself after operation
	 */
	public Vector add(Vector that) {
		this.x += that.x;
		this.y += that.y;
		return this;
	}
	
	/**
	 * Vector subtraction, mutating this vector
	 * @param that - vector to subtract
	 * @return itself after operation
	 */
	public Vector sub(Vector that) {
		this.x -= that.x;
		this.y -= that.y;
		return this;
	}
	
	/**
	 * Multiplication of vector by scalar, mutating this vector
	 * @param a - scalar to multiply by
	 * @return itself after operation
	 */
	public Vector mult(double a) {
		this.x *= a;
		this.y *= a;
		return this;
	}
	
	/**
	 * Vector addition
	 */
	public static Vector add(Vector v1, Vector v2) {
		return new Vector(v1.x + v2.x, v1.y + v2.y);
	}
	
	/**
	 * Vector subtraction
	 */
	public static Vector sub(Vector v1, Vector v2) {
		return new Vector(v1.x - v2.x, v1.y - v2.y);
	}
	
	/**
	 * Multiplication of vector by scalar
	 */
	public static Vector mult(Vector v, double a) {
		return new Vector(v.x * a, v.y * a);
	}
	
	/**
	 * Dot product of two vectors
	 */
	public static double dot(Vector v1, Vector v2) {
		return v1.x * v2.x + v1.y * v2.y;
	}
	
	/**
	 * Pythagorean distance between two vectors
	 */
	public static double dist(Vector v1, Vector v2) {
		double dx = v1.x - v2.x;
		double dy = v1.y - v2.y;
		return Math.sqrt(dx * dx + dy * dy);
	}
	
	/**
	 * Pythagorean L2 norm of a vector
	 */
	public double norm() {
		return Math.sqrt(this.x*this.x + this.y*this.y);
	}
	
	/**
	 * Turns this vector into a unit vector
	 * @return itself after operation
	 */
	public Vector normalize() {
		return this.mult(1 / this.norm());
	}
	
	/**
	 * Creates a zero vector
	 */
	public static Vector getZeroVector() {
		return new Vector(0, 0);
	}
	
	/**
	 * Zeros out the current vector
	 * @return itself after operation
	 */
	public Vector zero() {
		this.x = 0;
		this.y = 0;
		return this; 
	}
	
	@Override
	public String toString() {
		return "(" + this.x + ", " + this.y + ")";
	}
}
