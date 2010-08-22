package net.vidageek.tellme.messaging;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

public class DefaultMessageExecutorTest {
	private MessageQueue messageQueue;
	private DefaultMessageExecutor executor;

	@Before
	public void setUp() throws Exception {
		messageQueue = new SimpleMessageQueue();
		executor = new DefaultMessageExecutor(messageQueue);
		executor.start();
	}

	@Test
	public void executesAMessageWhenThereIsOne() throws Exception {
		Message message = mock(Message.class);
		messageQueue.addMessage(message);

		Thread.sleep(1000); // wait to "guarantee" execution

		verify(message).execute();
	}

	@Test
	public void executesManyMessages() throws Exception {
		Message firstMessage = mock(Message.class, "firstMessage");
		Message secondMessage = mock(Message.class, "secondMessage");
		Message thirdMessage = mock(Message.class, "thirdMessage");

		messageQueue.addMessage(firstMessage);
		messageQueue.addMessage(secondMessage);
		messageQueue.addMessage(thirdMessage);

		Thread.sleep(1000); // wait to "guarantee" execution

		verify(firstMessage).execute();
		verify(secondMessage).execute();
		verify(thirdMessage).execute();
	}
}
