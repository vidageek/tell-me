package net.vidageek.tellme.example;

public class ExampleConsumer {
	public void consume(ExampleProducer producer) {
		producer.produce(this);
		System.out.println("Finished calling producer at " + System.currentTimeMillis());
	}

	public void tellResult(long sum) {
		System.out.println("The result is " + sum);
		System.out.println("Current time millis now is " + System.currentTimeMillis());
	}

}
