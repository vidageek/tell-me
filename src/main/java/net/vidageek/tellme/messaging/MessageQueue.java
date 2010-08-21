package net.vidageek.tellme.messaging;

public abstract class MessageQueue {
	private static MessageQueue defaultQueue;

	public static void setDefault(MessageQueue queue) {
		defaultQueue = queue;
	}

	public static MessageQueue getDefault() {
		return defaultQueue;
	}

	public abstract void addMessage(Message message);
}
