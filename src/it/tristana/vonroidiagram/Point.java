package it.tristana.vonroidiagram;

public record Point(double x, double y) implements Comparable<Point> {

	@Override
	public int compareTo(Point other) {
		int cmp = Double.compare(y, other.y);
		return cmp == 0 ? Double.compare(x, other.x) : cmp;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	@Override
	public int hashCode() {
		return Double.hashCode(x) * 0x1F + Double.hashCode(y);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}

		if (!(other instanceof Point point)) {
			return false;
		}

		return x == point.x && y == point.y;
	}
}