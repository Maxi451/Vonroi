package it.tristana.vonroidiagram;

public record Vertex(Point p1, Point p2) {

	@Override
	public String toString() {
		return "{" + p1 + " -> " + p2 + "}";
	}

	@Override
	public int hashCode() {
		return p1.hashCode() * 0x1F + p2.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}

		if (!(other instanceof Vertex vertex)) {
			return false;
		}

		return p1.equals(vertex.p1) && p2.equals(vertex.p2);
	}
}