package net.vidageek.tellme.testing;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import net.vidageek.mirror.dsl.ClassController;
import net.vidageek.mirror.dsl.Mirror;
import net.vidageek.tellme.messaging.Message;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MessageMatcherTest {
	private MessageMatcher matcher;
	private @Mock Message receivedMessage;
	private Method someMethod, anotherMethod;

	@Before
	public void setUp() throws Exception {
		matcher = new MessageMatcher(this);
		ClassController<MessageMatcherTest> testClassController = new Mirror().on(MessageMatcherTest.class);
		someMethod = testClassController.reflect().method("setUp").withoutArgs();
		anotherMethod = testClassController.reflect().method("toString").withoutArgs();
	}

	@Test
	public void matchesAnyMessageToDestinationObjectIfMethodNotDeclared() throws Exception {
		when(receivedMessage.getDestinationObject()).thenReturn(this);
		assertTrue(matcher.matchesSafely(receivedMessage));
	}

	@Test
	public void doesNotMatchMessageToObjectNotEqualToDestination() throws Exception {
		when(receivedMessage.getDestinationObject()).thenReturn("");
		assertFalse(matcher.matchesSafely(receivedMessage));
	}

	@Test
	public void doesNotMatchMessageToDestinationObjectButDifferentMethod() throws Exception {
		matcher.calling(someMethod);

		when(receivedMessage.getDestinationObject()).thenReturn(this);
		when(receivedMessage.getDestinationMethod()).thenReturn(anotherMethod);

		assertFalse(matcher.matchesSafely(receivedMessage));
	}

	@Test
	public void matchesMessageToTheSameObjectAndMethod() throws Exception {
		matcher.calling(someMethod);

		when(receivedMessage.getDestinationObject()).thenReturn(this);
		when(receivedMessage.getDestinationMethod()).thenReturn(someMethod);

		assertTrue(matcher.matchesSafely(receivedMessage));
	}

	@Test(expected = IllegalArgumentException.class)
	public void cannotCreateMatcherWithNullDestination() throws Exception {
		new MessageMatcher(null);
	}
}
