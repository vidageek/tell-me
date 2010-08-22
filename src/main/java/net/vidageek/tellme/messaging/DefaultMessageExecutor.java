package net.vidageek.tellme.messaging;


public class DefaultMessageExecutor implements MessageExecutor {

	private final MessageQueue messageQueue;

	public DefaultMessageExecutor(MessageQueue messageQueue) {
		this.messageQueue = messageQueue;
	}

	@Override
	public void start() {
		Runnable consumer = new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Message messageToExecute = messageQueue.getNext();
						messageToExecute.execute();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};

		Thread consumerThread = new Thread(consumer);
		consumerThread.setDaemon(true);
		consumerThread.start();
	}
}