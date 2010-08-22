package net.vidageek.tellme.exceptions;

public class DesynchronizationException extends RuntimeException {

	private static final long serialVersionUID = 4746297552300462862L;

	public DesynchronizationException(Throwable rootCause) {
		super(rootCause);
	}
}
