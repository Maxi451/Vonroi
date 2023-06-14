package it.tristana.vonroidiagram.event;

import it.tristana.vonroidiagram.Point;

public abstract class Event implements Comparable<Event> {

	protected Point point;
	
	public Event(Point point) {
		this.point = point;
	}
	
	@Override
	public int compareTo(Event other) {
		return point.compareTo(other.point);
	}

	public Point getPoint() {
		return point;
	}
}
