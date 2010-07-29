package net.vidageek.tellme;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.vidageek.mirror.dsl.Mirror;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProxifierTest {
	private Proxifier proxifier;
	private MyObject myObject;
	private @Mock MethodInterceptor enqueuer;

	@Before
	public void setUp() throws Exception {
		proxifier = new Proxifier(enqueuer);
		myObject = proxifier.proxify(MyObject.class);
	}

	@Test
	public void proxifyAnnotatedMethodsToGenerateMessagesWhenTheyAreCalled() throws Throwable {
		Method annotatedMethod = new Mirror().on(MyObject.class).reflect().method("annotatedMethod").withoutArgs();

		myObject.annotatedMethod();

		verify(enqueuer).intercept(eq(myObject), eq(annotatedMethod), any(Object[].class), any(MethodProxy.class));
	}

	@Test
	public void doesNotProxifyObjectMethods() throws Throwable {
		Method objectMethod = new Mirror().on(MyObject.class).reflect().method("toString").withoutArgs();

		myObject.toString();

		verify(enqueuer, never()).intercept(eq(myObject), eq(objectMethod), any(Object[].class), any(MethodProxy.class));
	}

	@Test
	public void doesNotProxifyNotAnnotatedMethods() throws Throwable {
		Method notAnnotatedMethod = new Mirror().on(MyObject.class).reflect().method("notAnnotatedMethod").withoutArgs();

		myObject.notAnnotatedMethod();

		verify(enqueuer, never()).intercept(eq(myObject), eq(notAnnotatedMethod), any(Object[].class), any(MethodProxy.class));
	}
}

class MyObject {
	@Async
	public void annotatedMethod() {}
	public void notAnnotatedMethod() {}
}
