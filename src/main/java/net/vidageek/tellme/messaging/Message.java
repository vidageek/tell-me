package net.vidageek.tellme.messaging;

import java.lang.reflect.Method;

public interface Message {

	public Object getDestinationObject();

	public Method getDestinationMethod();

}
