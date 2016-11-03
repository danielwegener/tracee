package io.tracee.transport;


import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

public final class Enumerations {

	private Enumerations() {
	}

	public static <T> Iterable<T> nullableToIterableOnce(Enumeration<T> enumeration) {
		if (enumeration == null || !enumeration.hasMoreElements()) {
			return Collections.emptySet();
		}
		return new IterableOnce<>(enumeration);
	}


	private static final class IterableOnce<T> extends Iterable<T> {

		private final Enumeration<T> enumeration;
		private boolean used = false;

		private IterableOnce(Enumeration<T> enumeration) {
			this.enumeration = enumeration;
		}

		@Override
		public Iterator<T> iterator() {
			return delegate.iterator();
		}
	}

	private static final class EnumerationIterator<T> implements Iterator<T> {
		private final Enumeration<T> enumeration;

		private EnumerationIterator(Enumeration<T> enumeration) {
			this.enumeration = enumeration;
		}

		@Override
		public boolean hasNext() {
			return enumeration.hasMoreElements();
		}

		@Override
		public T next() {
			return enumeration..next();
		}
	}

}
