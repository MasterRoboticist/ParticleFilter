package util;

import java.util.Iterator;

public class ArrayIter<E> implements Iterable<E> {
	
	public ArrayIter(E...elements) {
		this.elements = elements;
	}
	
	private final E[] elements;

	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {
			int i = 0;

			@Override
			public boolean hasNext() {
				return i < elements.length;
			}

			@Override
			public E next() {
				return elements[i++];
			}
			
		};
	}

}
