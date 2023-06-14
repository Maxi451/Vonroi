package it.tristana.vonroidiagram;

import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

public record Arc(Point focus) implements Comparable<Arc> {

	@Override
	public int compareTo(Arc other) {
		return focus.compareTo(other.focus);
	}

	@Override
	public int hashCode() {
		return focus.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}

		if (!(other instanceof Arc arc)) {
			return false;
		}

		return focus.equals(arc.focus);
	}

	@Override
	public String toString() {
		return "<Arc focus=" + focus + ">";
	}

	public String toString(double directixY) {
		double a = a(focus.y(), directixY);
		double b = b(focus.x(), focus.y(), directixY);
		double c = c(focus.x(), focus.y(), directixY);
		return String.format(Locale.ENGLISH, "y = %.2fx^2 + %.2fx + %.2f", a, b, c);
	}

	public double getYFromX(double x, double directixY) {
		double a = a(focus.y(), directixY);
		double b = b(focus.x(), focus.y(), directixY);
		double c = c(focus.x(), focus.y(), directixY);
		return a * sq(x) + b * x + c;
	}

	public SortedSet<Point> intersect(Arc other, double y) {
		SortedSet<Point> intersections = new TreeSet<>();
		Point otherFocus = other.focus;
		if (focus.y() == y || otherFocus.y() == y) {
			return intersections;
		}

		double a0 = a(focus.y(), y);
		double b0 = b(focus.x(), focus.y(), y);
		double c0 = c(focus.x(), focus.y(), y);
		double a1 = a(otherFocus.y(), y);
		double b1 = b(otherFocus.x(), otherFocus.y(), y);
		double c1 = c(otherFocus.x(), otherFocus.y(), y);
		double a = a0 - a1;
		double b = b0 - b1;
		double c = c0 - c1;

		if (a == 0) {
			if (b == 0) {
				return intersections;
			}

			double x0 = (c1 - c0) / (b0 - b1);
			double y0 = b0 * x0 + c0;
			intersections.add(new Point(x0, y0));
			return intersections;
		}

		if (a < 0) {
			a = -a;
			b = -b;
			c = -c;
		}

		double delta = sq(b) - 4 * a * c;
		if (delta < 0) {
			return intersections;
		}

		double sqrt = Math.sqrt(delta);
		double x0 = (-b - sqrt) / (2 * a);
		double y0 = a0 * sq(x0) + b0 * x0 + c0;
		intersections.add(new Point(x0, y0));
		if (sqrt > 0) {
			double x1 = (-b + sqrt) / (2 * a);
			double y1 = a0 * sq(x1) + b0 * x1 + c0;
			intersections.add(new Point(x1, y1));
		}
		return intersections;
	}

	public static Point getFocus(double a, double b, double c) {
		if (a == 0) {
			throw new IllegalArgumentException("'a' can't be 0!");
		}

		double x = -b / (2 * a);
		double y = (1 - sq(b) + 4 * a * c) / (4 * a);
		return new Point(x, y);
	}

	public static double getDirectix(double a, double b, double c) {
		if (a == 0) {
			throw new IllegalArgumentException("'a' can't be 0!");
		}

		return - (1 + sq(b) - 4 * a * c) / (4 * a);
	}

	public static Arc getBeachlineArc(SortedSet<Arc> arcs, double x, double directixY) {
		double maxY = 0;
		Arc arc = null;
		for (Arc test : arcs) {
			double y = test.getYFromX(x, directixY);
			if (y > maxY) {
				arc = test;
				maxY = y;
			}
		}
		return arc;
	}

	private static double a(double fy, double y) {
		return 1d / (2d * (fy - y));
	}

	private static double b(double fx, double fy, double y) {
		return -fx / (fy - y);
	}

	private static double c(double fx, double fy, double y) {
		return (sq(fx) + sq(fy) - sq(y)) / (2d * (fy - y));
	}

	private static double sq(double x) {
		return x * x;
	}
}
