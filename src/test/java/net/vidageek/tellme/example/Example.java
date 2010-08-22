package net.vidageek.tellme.example;

import net.vidageek.tellme.Desynchronizer;
import net.vidageek.tellme.messaging.DefaultMessageExecutor;
import net.vidageek.tellme.messaging.MessageExecutor;
import net.vidageek.tellme.messaging.MessageQueue;
import net.vidageek.tellme.messaging.SimpleMessageQueue;

public class Example {

	public static void main(String[] args) throws Exception {
		MessageQueue messageQueue = new SimpleMessageQueue();
		Desynchronizer.setMessageQueue(messageQueue);

		MessageExecutor executor = new DefaultMessageExecutor(messageQueue);

		Desynchronizer desynchronizer = new Desynchronizer();
		desynchronizer.desynchronize("net.vidageek.tellme.example.ExampleProducer");
		desynchronizer.desynchronize("net.vidageek.tellme.example.ExampleConsumer");

		executor.start();

		ExampleProducer producer = new ExampleProducer();
		ExampleConsumer consumer = new ExampleConsumer();

		consumer.consume(producer);
	}

}
