package net.vidageek.tellme.testing;

import org.hamcrest.Description;
import org.junit.internal.matchers.TypeSafeMatcher;

public final class LessThanMatcher<T extends Comparable<T>> extends TypeSafeMatcher<T> {
	private final T bigger;

	public LessThanMatcher(T bigger) {
		this.bigger = bigger;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("smaller than " + bigger);
	}

	@Override
	public boolean matchesSafely(T item) {
		return item.compareTo(bigger) < 0;
	}
}