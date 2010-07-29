package net.vidageek.tellme.messaging;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public final class MessageEnqueuerCallback implements MethodInterceptor {
	private final MessageQueue queue;

	public MessageEnqueuerCallback(MessageQueue queue) {
		this.queue = queue;
	}

	@Override
	public Object intercept(Object receiver, Method method, Object[] args,
			MethodProxy proxy) throws Throwable {
		queue.addMessage(new MethodCallMessage(receiver, method, args));
		return null;
	}
}