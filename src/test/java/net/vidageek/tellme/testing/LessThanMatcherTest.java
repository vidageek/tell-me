package net.vidageek.tellme.testing;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class LessThanMatcherTest {
	private static final int BIGGER = 100;
	private LessThanMatcher<Integer> matcher;

	@Before
	public void setUp() throws Exception {
		matcher = new LessThanMatcher<Integer>(BIGGER);
	}

	@Test
	public void matchesASmallerItem() throws Exception {
		assertTrue("Should match smaller item", matcher.matches(BIGGER - 1));
	}

	@Test
	public void doesNotMatchAnEqualItem() throws Exception {
		assertFalse("Should not match an equal item", matcher.matches(BIGGER));
	}

	@Test
	public void doesNotMatchABiggerItem() throws Exception {
		assertFalse("Should not match a bigger item", matcher.matches(BIGGER + 1));
	}
}
