package net.vidageek.tellme.testing;

import java.lang.reflect.Method;

import net.vidageek.tellme.messaging.Message;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;

public final class MessageMatcher extends TypeSafeMatcher<Message> implements
		Matcher<Message> {

	private final Object expectedDestination;
	private Method expectedMethod = null;

	public MessageMatcher(Object destination) {
		if (destination == null) {
			throw new IllegalArgumentException("Null destination for expected message");
		}
		this.expectedDestination = destination;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("A message for ");
		description.appendValue(expectedDestination);
		if (expectedMethod != null) {
			description.appendText(String.format(" method '%s'", expectedMethod.toGenericString()));
		}
	}

	@Override
	public boolean matchesSafely(Message received) {
		boolean matches = true;
		if (expectedMethod != null) {
			matches = matches && expectedMethod.equals(received.getDestinationMethod());
		}
		return matches && expectedDestination.equals(received.getDestinationObject());
	}

	public MessageMatcher calling(Method method) {
		this.expectedMethod = method;
		return this;
	}

}
