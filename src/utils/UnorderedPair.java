package utils;

/**
 * Represents an unordered pair of elements (i.e. compares equal to the reverse-ordered pair)
 *
 * @param <T> - the type of element to contain
 */
public final class UnorderedPair<T> {
	public final T a, b;
	
	public UnorderedPair(T a, T b) {
		this.a = a;
		this.b = b;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		try {
			@SuppressWarnings("unchecked")
			UnorderedPair<T> that = (UnorderedPair<T>) o;
			return (this.a.equals(that.a) && this.b.equals(that.b)) || (this.a.equals(that.b) && this.b.equals(that.a));
		}
		catch (ClassCastException e) {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return a.hashCode() + b.hashCode();
	}
	
	@Override
	public String toString() {
		return "(" + a.toString() + ", " + b.toString() + ")";
	}
}
