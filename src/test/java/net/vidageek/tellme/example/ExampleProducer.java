package net.vidageek.tellme.example;

import net.vidageek.tellme.Async;

public class ExampleProducer {

	@Async
	public void produce(ExampleConsumer consumerToTell) {
		long sum = 0;
		for (long i = 0; i < 100000; i++) {
			sum += i;
		}
		consumerToTell.tellResult(sum);
	}
}
