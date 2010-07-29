package net.vidageek.tellme.messaging;

import java.lang.reflect.Method;
import java.util.Arrays;

public final class MethodCallMessage implements Message {

	private final Object destination;
	private final Method method;
	private final Object[] args;

	public MethodCallMessage(Object destination, Method method, Object[] args) {
		this.destination = destination;
		this.method = method;
		this.args = args;
	}

	@Override
	public Object getDestinationObject() {
		return destination;
	}

	@Override
	public Method getDestinationMethod() {
		return method;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("A MethodCallMessage for <");
		stringBuilder.append(destination);
		stringBuilder.append("> method '");
		stringBuilder.append(method.getName());
		stringBuilder.append("'");
		if (args.length > 0) {
			stringBuilder.append(" and args ");
			stringBuilder.append(Arrays.toString(args));
		}
		return stringBuilder.toString();
	}

}
