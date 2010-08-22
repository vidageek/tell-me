package net.vidageek.tellme.messaging;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SimpleMessageQueue implements MessageQueue {

	private final BlockingQueue<Message> internalQueue = new LinkedBlockingQueue<Message>();

	@Override
	public void addMessage(Message message) {
		internalQueue.add(message);
	}

	@Override
	public Message getNext() throws InterruptedException {
		return internalQueue.take();
	}

}
