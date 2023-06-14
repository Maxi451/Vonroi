package it.tristana.vonroidiagram;

import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.BiConsumer;

public class VertexMap<K, V> {

	private SortedSet<V> rootValues;
	private SortedMap<K, SortedSet<V>> map;

	public VertexMap() {
		this.rootValues = new TreeSet<>();
		this.map = new TreeMap<>();
	}

	public void add(K key, V value) {
		if (key == null) {
			rootValues.add(value);
			return;
		}

		SortedSet<V> vertices = map.get(key);
		if (vertices == null) {
			vertices = new TreeSet<>();
			map.put(key, vertices);
		}
		vertices.add(value);
	}

	@Override
	public String toString() {
		return "root = " + rootValues + "\nMap = " + map;
	}

	public void forEach(BiConsumer<? super K, ? super SortedSet<V>> action) {
		action.accept(null, copy(rootValues));
		map.forEach((k, v) -> action.accept(k, copy(v)));
	}

	public void forEachSet(BiConsumer<? super K, ? super V> action) {
		rootValues.forEach(v -> action.accept(null, v));
		map.forEach((k, v) -> v.forEach(e -> action.accept(k, e)));
	}

	public SortedSet<V> getRootValues() {
		return fromKey(null);
	}

	public SortedSet<V> fromKey(K key) {
		if (key == null) {
			return copy(rootValues);
		}

		SortedSet<V> set = map.get(key);
		return set == null ? null : copy(set);
	}

	public SortedSet<V> getAllValues() {
		SortedSet<V> result = new TreeSet<>(rootValues);
		map.forEach((k, v) -> result.addAll(v));
		return result;
	}

	private static <T> SortedSet<T> copy(SortedSet<T> set) {
		return new TreeSet<>(set);
	}
}
