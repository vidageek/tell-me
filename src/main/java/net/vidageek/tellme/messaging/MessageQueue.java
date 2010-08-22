package net.vidageek.tellme.messaging;

public interface MessageQueue {
	void addMessage(Message message);
	Message getNext() throws InterruptedException;
}
