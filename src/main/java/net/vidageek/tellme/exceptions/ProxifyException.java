package net.vidageek.tellme.exceptions;

public class ProxifyException extends RuntimeException {

	private static final long serialVersionUID = 4746297552300462862L;

	public ProxifyException(Throwable rootCause) {
		super(rootCause);
	}
}
