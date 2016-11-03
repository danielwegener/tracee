package io.tracee.testhelper;

import java.util.Enumeration;
import java.util.NoSuchElementException;

public final class SingletonEnumeration<T> implements Enumeration<T> {

	public SingletonEnumeration(T element) {
		this.element = element;
	}

	public static <T> SingletonEnumeration<T> of(T element) {
		return new SingletonEnumeration<>(element);
	}

	private boolean consumed = false;
	private final T element;

	@Override
	public boolean hasMoreElements() {
		return !consumed;
	}

	@Override
	public T nextElement() {
		if (consumed) {
			throw new NoSuchElementException();
		} else {
			consumed = true;
			return element;
		}
	}
}
