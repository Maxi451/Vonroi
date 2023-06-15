package it.tristana.vonroidiagram;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeSet;

import it.tristana.vonroidiagram.event.Event;
import it.tristana.vonroidiagram.event.SiteEvent;

public class Vonroi implements Runnable {

	private Queue<Event> eventQueue;
	private TreeSet<Arc> beachline;
	private VertexMap<Arc, Arc> arcs;
	private Point min;
	private Point max;

	public Vonroi(Point[] sites, Point min, Point max) {
		this.eventQueue = new PriorityQueue<>();
		for (Point site : sites) {
			this.eventQueue.add(new SiteEvent(site));
		}
		this.beachline = new TreeSet<>();
		this.arcs = new VertexMap<>();
		this.min = min;
		this.max = max;
	}

	@Override
	public void run() {
		Event event = null;
		do {
			event = eventQueue.poll();
			if (event == null) {
				break;
			}

			if (event instanceof SiteEvent site) {
				Point point = site.getPoint();
				Arc arc = Arc.getBeachlineArc(beachline, point.x(), point.y());
				if (arc != null) {
					beachline.add(arc);
				}
				arcs.add(arc, new Arc(point));
				//checkCircleEvents();
			} else {
				//createVertex(event.getPoint());
			}
		} while (event != null);
	}

	public VertexMap<Arc, Arc> getArcsGraph() {
		return arcs;
	}
}
