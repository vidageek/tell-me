package net.vidageek.tellme.messaging;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodProxy;
import net.vidageek.mirror.dsl.Mirror;
import net.vidageek.tellme.messaging.MessageEnqueuerCallback;
import net.vidageek.tellme.messaging.MessageQueue;
import net.vidageek.tellme.testing.MessageMatcher;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MessageEnqueuerCallbackTest {

	private @Mock MessageQueue queue;
	private @Mock MethodProxy methodProxy;
	private MessageEnqueuerCallback enqueuer;
	private MyProxifiedObject myObject;

	@Before
	public void setUp() throws Exception {
		enqueuer = new MessageEnqueuerCallback(queue);
		myObject = new MyProxifiedObject();
	}

	@Test
	public void putsAMessageForTheReceiverOfMethodCallOnTheQueue() throws Throwable {
		Method method = new Mirror().on(MyProxifiedObject.class).reflect().method("methodThatDoesNotReturnAnything").withoutArgs();

		enqueuer.intercept(myObject, method, new Object[] {}, methodProxy);

		verify(queue).addMessage(argThat(isAMessageTo(myObject).calling(method)));
	}

	private MessageMatcher isAMessageTo(Object destination) {
		return new MessageMatcher(destination);
	}
}

class MyProxifiedObject {
	public void methodThatDoesNotReturnAnything() {}
}
