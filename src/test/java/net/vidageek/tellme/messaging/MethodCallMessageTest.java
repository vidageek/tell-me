package net.vidageek.tellme.messaging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import net.vidageek.mirror.dsl.Mirror;
import net.vidageek.tellme.testing.LessThanMatcher;

import org.hamcrest.Matcher;
import org.junit.Test;

public class MethodCallMessageTest {
	public static final long TIME_TO_SPEND_ON_SLOW_METHOD = 1000;

	@Test
	public void callsTheTargetObjectWithCorrectArgumentsInANewThread() throws Exception {
		Object arg1 = new Object();
		Object arg2 = new Object();

		Method slowMethod = new Mirror().on(ClassWithASlowMethod.class).reflect().method("slowMethod").withArgs(Object.class, Object.class);

		ClassWithASlowMethod destination = new ClassWithASlowMethod();

		MethodCallMessage message = new MethodCallMessage(destination, slowMethod, new Object[] {arg1, arg2});

		long currentTimeMillis = System.currentTimeMillis();

		message.execute();

		assertThat(System.currentTimeMillis() - currentTimeMillis, lessThan(TIME_TO_SPEND_ON_SLOW_METHOD));

		Thread.sleep(TIME_TO_SPEND_ON_SLOW_METHOD);

		assertTrue("Should have called the method", destination.calledSlowMethod());
		assertEquals("First argument should be the same", arg1, destination.getArg1());
		assertEquals("Second argument should be the same", arg2, destination.getArg2());
	}

	@Test
	public void callsTheTargetObjectWithoutArguments() throws Exception {
		Method anotherMethod = new Mirror().on(ClassWithASlowMethod.class).reflect().method("anotherMethod").withoutArgs();

		ClassWithASlowMethod destination = new ClassWithASlowMethod();

		MethodCallMessage message = new MethodCallMessage(destination, anotherMethod, new Object[] {});

		message.execute();

		Thread.sleep(TIME_TO_SPEND_ON_SLOW_METHOD);

		assertTrue("Should have called the method", destination.calledAnotherMethod());
	}

	private Matcher<Long> lessThan(final long biggerLong) {
		return new LessThanMatcher<Long>(biggerLong);
	}
}

class ClassWithASlowMethod {
	private boolean calledSlowMethod = false;
	private Object arg1 = null;
	private Object arg2 = null;
	private boolean calledAnotherMethod = false;

	public void slowMethod(Object arg1, Object arg2) throws Exception {
		this.calledSlowMethod = true;
		this.arg1 = arg1;
		this.arg2 = arg2;
		Thread.sleep(MethodCallMessageTest.TIME_TO_SPEND_ON_SLOW_METHOD);
	}

	public void anotherMethod() {
		this.calledAnotherMethod = true;
	}

	public boolean calledSlowMethod() {
		return calledSlowMethod;
	}

	public Object getArg1() {
		return arg1;
	}

	public Object getArg2() {
		return arg2;
	}

	public boolean calledAnotherMethod() {
		return calledAnotherMethod;
	}
}