package it.tristana.vonroidiagram;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public record Interval(double min, double max) implements Comparable<Interval> {

	public Interval(double min, double max) {
		this.min = Math.min(min, max);
		this.max = Math.max(min, max);
	}

	@Override
	public int compareTo(Interval other) {
		int cmp = Double.compare(min, other.min);
		return cmp == 0 ? Double.compare(max, other.max) : cmp;
	}

	@Override
	public String toString() {
		return "[" + min + " - " + max + ")";
	}

	@Override
	public int hashCode() {
		return Double.hashCode(min) * 0x1F + Double.hashCode(max);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}

		if (!(other instanceof Interval interval)) {
			return false;
		}

		return min == interval.min && max == interval.max;
	}

	public double length() {
		return max - min;
	}

	public static SortedSet<Interval> merge(SortedSet<Interval> intervals) {
		SortedSet<Interval> result = new TreeSet<>();
		intervals.forEach(interval -> {
			if (interval.length() == 0) {
				return;
			}

			Interval min = getContaining(result, interval.min);
			Interval max = getContaining(result, interval.max);
			if (min == null) {
				if (max == null) {
					result.add(interval);
					return;
				}

				result.remove(max);
				result.add(new Interval(interval.min, max.max));
				return;
			}
			result.remove(min);
			if (max == null) {
				result.add(new Interval(min.min, interval.max));
				return;
			}

			result.remove(max);
			result.add(new Interval(min.min, max.max));
		});
		removeFullyContained(result);
		return result;
	}

	public static SortedSet<Interval> compress(SortedSet<Interval> intervals, Interval span) {
		SortedSet<Interval> result = new TreeSet<>();
		intervals.forEach(interval -> {
			if (interval.min >= span.min) {
				result.add(new Interval(interval.min, Math.min(interval.max, span.max)));
			}

			if (interval.max < span.max) {
				result.add(new Interval(span.min, interval.max));
			}
		});
		return result;
	}

	public static SortedSet<Interval> getFreeIntervals(SortedSet<Interval> intervals, Interval span) {
		SortedSet<Interval> result = new TreeSet<>();
		if (intervals.size() == 0) {
			result.add(span);
			return result;
		}

		double last = span.min;
		Iterator<Interval> iterator = intervals.iterator();
		while (iterator.hasNext()) {
			Interval next = iterator.next();
			if (next.max <= last) {
				continue;
			}

			if (next.min >= span.max) {
				break;
			}
			
			if (next.min > last) {
				result.add(new Interval(last, next.min));
			}
			last = next.max;
		}
		if (last < span.max) {
			result.add(new Interval(last, span.max));
		}
		return result;
	}

	private static void removeFullyContained(SortedSet<Interval> intervals) {
		Iterator<Interval> iterator = intervals.iterator();
		while (iterator.hasNext()) {
			Interval next = iterator.next();
			for (Interval test : intervals) {
				if (test == next) {
					continue;
				}

				if (isFullyContained(test, next)) {
					iterator.remove();
					break;
				}
			}
		}
	}

	private static boolean isFullyContained(Interval big, Interval small) {
		return small.min >= big.min && small.max <= big.max;
	}

	private static Interval getContaining(SortedSet<Interval> intervals, double value) {
		for (Interval interval : intervals) {
			if (contains(interval, value)) {
				return interval;
			}
		}

		return null;
	}

	private static boolean contains(Interval interval, double value) {
		return interval.min <= value && interval.max > value;
	}
}
