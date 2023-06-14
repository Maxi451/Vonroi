package it.tristana.vonroidiagram;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JPanel;

public class Board extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final Color POINT_COLOR = Color.RED;
	private static final Color ARC_COLOR = Color.BLACK;
	private static final int HALF_POINT_SIZE = 5 / 2;

	private Point[] points;
	private VertexMap<Arc, Arc> arcs;
	private Point min;
	private Point max;
	private int sweepline;

	public Board(Point[] points, int width, int height) {
		this.points = points;
		this.min = new Point(0, 0);
		this.max = new Point(width, height);
		Vonroi vonroi = new Vonroi(points, this.min, this.max);
		vonroi.run();
		this.arcs = vonroi.getArcsGraph();
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLUE);
		int minX = (int) min.x();
		int minY = (int) min.y();
		int maxX = (int) max.x();
		int maxY = (int) max.y();
		//int sweepline = maxY / 2;
		g2.drawLine(0, sweepline, maxX, sweepline);
		Interval span = new Interval(minX, maxX);

		g2.setColor(ARC_COLOR);
		SortedMap<Arc, SortedSet<Interval>> rendered = new TreeMap<>();
		SortedSet<Arc> arcsField = arcs.getAllValues();
		arcsField.forEach(arc -> {
			drawArc(g2, arc, sweepline);
			SortedSet<Interval> notRenderedIntervals = new TreeSet<>();
			Point focus = arc.focus();
			for (Arc other : arcsField) {
				if (arc == other || other.focus().y() > sweepline) {
					continue;
				}

				SortedSet<Point> intersections = arc.intersect(other, sweepline);
				int size = intersections.size();
				if (size == 0) {
					notRenderedIntervals.add(span);
					continue;
				}

				Point first = intersections.first();
				if (size == 1) {
					notRenderedIntervals.add(focus.x() < first.x() ? new Interval(first.x(), maxX) : new Interval(minX, first.x()));
					continue;
				}

				Point second = intersections.last();
				if (first.x() > second.x()) {
					Point tmp = first;
					first = second;
					second = tmp;
				}

				if (focus.y() < other.focus().y()) {
					notRenderedIntervals.add(new Interval(first.x(), second.x()));
				} else {
					notRenderedIntervals.add(new Interval(minX, first.x()));
					notRenderedIntervals.add(new Interval(second.x(), maxX));
				}
			}
			rendered.put(arc, Interval.getFreeIntervals(Interval.merge(notRenderedIntervals), span));
		});

		g2.setColor(Color.GREEN);
		rendered.forEach((arc, intervals) -> {
			intervals.forEach(interval -> {
				drawArc(g2, arc, (int) interval.min(), minY, (int) interval.max(), maxY, sweepline);
			});
		});

		g2.setColor(POINT_COLOR);
		for (Point point : points) {
			g2.fillOval((int) point.x() - HALF_POINT_SIZE, (int) point.y() - HALF_POINT_SIZE, HALF_POINT_SIZE * 2, HALF_POINT_SIZE * 2);
		}
	}
	
	public void increaseSweepline() {
		sweepline = (sweepline + 1) % (int) max.y();
	}

	private void drawArc(Graphics2D g2, Arc arc, int directixY) {
		drawArc(g2, arc, (int) min.x(), (int) min.y(), (int) max.x(), (int) max.y(), directixY);
	}

	private static void drawArc(Graphics2D g2, Arc arc, int minX, int minY, int maxX, int maxY, int directixY) {
		List<Point> points = new LinkedList<>();
		for (int x = minX; x < maxX; x ++) {
			double y = arc.getYFromX(x, directixY);
			if (x >= minX && y >= minY && x < maxX && y < maxY) {
				points.add(new Point(x, (int) y));
			}
		}
		Point last = null;
		for (Point point : points) {
			if (last != null) {
				g2.drawLine((int) last.x(), (int) last.y(), (int) point.x(), (int) point.y());
			}
			last = point;
		}
	}

	private static Point mid(Point p1, Point p2) {
		return new Point((p1.x() + p2.x()) / 2, (p1.y() + p2.y()) / 2);
	}
}
