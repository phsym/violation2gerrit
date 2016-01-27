package com.github.phsym.violation2gerrit.comments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * List of review comments
 * @author pierresy
 *
 */
public class CommentList implements Iterable<CountableComment> {

	private ArrayList<CountableComment> _inner;

	/**
	 * Create an empty list
	 */
	public CommentList() {
		_inner = new ArrayList<>();
	}

	/**
	 * @return
	 * @see java.util.ArrayList#size()
	 */
	public int size() {
		return _inner.size();
	}

	/**
	 * @return
	 * @see java.util.ArrayList#isEmpty()
	 */
	public boolean isEmpty() {
		return _inner.isEmpty();
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.ArrayList#contains(java.lang.Object)
	 */
	public boolean contains(Object o) {
		return _inner.contains(o);
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.ArrayList#indexOf(java.lang.Object)
	 */
	public int indexOf(Object o) {
		return _inner.indexOf(o);
	}

	/**
	 * @return
	 * @see java.util.ArrayList#toArray()
	 */
	public Object[] toArray() {
		return _inner.toArray();
	}

	/**
	 * @param a
	 * @return
	 * @see java.util.ArrayList#toArray(java.lang.Object[])
	 */
	public <T> T[] toArray(T[] a) {
		return _inner.toArray(a);
	}

	/**
	 * @return
	 * @see java.util.AbstractCollection#toString()
	 */
	public String toString() {
		return _inner.toString();
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.ArrayList#get(int)
	 */
	public CountableComment get(int index) {
		return _inner.get(index);
	}

	/**
	 * @param e
	 * @return
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
	public boolean add(Comment e) {
		Objects.requireNonNull(e, "Cannot add a null Comment");
		Optional<CountableComment> c = _inner.stream().filter((com) -> e.isSimilar(com)).findFirst();
		if(c.isPresent())
			c.get().incrementCount();
		else
			_inner.add(new CountableComment(e));
		return true;
	}

	/**
	 * 
	 * @see java.util.ArrayList#clear()
	 */
	public void clear() {
		_inner.clear();
	}

	/**
	 * @return
	 * @see java.util.Collection#stream()
	 */
	public Stream<CountableComment> stream() {
		return _inner.stream();
	}

	/**
	 * @return
	 * @see java.util.Collection#parallelStream()
	 */
	public Stream<CountableComment> parallelStream() {
		return _inner.parallelStream();
	}

	/**
	 * @return
	 * @see java.util.ArrayList#iterator()
	 */
	@Override
	public Iterator<CountableComment> iterator() {
		return _inner.iterator();
	}

	/**
	 * @param action
	 * @see java.util.ArrayList#forEach(java.util.function.Consumer)
	 */
	public void forEach(Consumer<? super CountableComment> action) {
		_inner.forEach(action);
	}
}
