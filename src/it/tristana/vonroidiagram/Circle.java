package it.tristana.vonroidiagram;

import java.util.Locale;

public record Circle(Point center, double radius) {

	public static Circle of(Point p1, Point p2, Point p3) {
		double x1 = p1.x();
		double x2 = p2.x();
		double x3 = p3.x();
		double y1 = p1.y();
		double y2 = p2.y();
		double y3 = p3.y();

		double a = a(x1, x2, x3, y1, y2, y3);
		if (a == 0) {
			throw new IllegalArgumentException("'A' is zero!");
		}

		double b = b(x1, x2, x3, y1, y2, y3);
		double c = c(x1, x2, x3, y1, y2, y3);
		double d = d(x1, x2, x3, y1, y2, y3);
		return new Circle(getCenter(a, b, c), getRadius(a, b, c, d));
	}

	@Override
	public String toString() {
		return String.format(Locale.ENGLISH, "(x - %.2f)^2 + (y - %.2f)^2 = %.2f", center.x(), center.y(), sq(radius));
	}

	private static Point getCenter(double a, double b, double c) {
		return new Point(getCenterX(a, b), getCenterY(a, c));
	}

	private static double getRadius(double a, double b, double c, double d) {
		double delta = (sq(b) + sq(c) - 4 * a * d) / (4 * sq(a));
		if (delta < 0) {
			throw new IllegalArgumentException("Delta < 0");
		}

		return Math.sqrt(delta);
	}

	private static double getCenterX(double a, double b) {
		return -b / (2 * a);
	}

	private static double getCenterY(double a, double c) {
		return -c / (2 * a);
	}

	private static double a(double x1, double x2, double x3, double y1, double y2, double y3) {
		return x1 * (y2 - y3) - y1 * (x2 - x3) + x2 * y3 - x3 * y2;
	}

	private static double b(double x1, double x2, double x3, double y1, double y2, double y3) {
		return (sq(x1) + sq(y1)) * (y3 - y2) + (sq(x2) + sq(y2)) * (y1 - y3) + (sq(x3) + sq(y3)) * (y2 - y1);
	}

	private static double c(double x1, double x2, double x3, double y1, double y2, double y3) {
		return (sq(x1) + sq(y1)) * (x2 - x3) + (sq(x2) + sq(y2)) * (x3 - x1) + (sq(x3) + sq(y3)) * (x1 - x2);
	}

	private static double d(double x1, double x2, double x3, double y1, double y2, double y3) {
		return (sq(x1) + sq(y1)) * (x3 * y2 - x2 * y3) + (sq(x2) + sq(y2)) * (x1 * y3 - x3 * y1) + (sq(x3) + sq(y3)) * (x2 * y1 - x1 * y2);
	}

	private static double sq(double x) {
		return x * x;
	}
}
