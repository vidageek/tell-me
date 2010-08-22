package net.vidageek.tellme;

import java.util.ArrayList;
import java.util.Collection;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import net.vidageek.tellme.exceptions.DesynchronizationException;
import net.vidageek.tellme.messaging.MessageQueue;

public final class Desynchronizer {
	private static MessageQueue defaultQueue;

	public static void setMessageQueue(MessageQueue queue) {
		defaultQueue = queue;
	}

	public static MessageQueue getMessageQueue() {
		return defaultQueue;
	}


	public void desynchronize(String className) {
		ClassPool pool = ClassPool.getDefault();
		try {
			CtClass classToDesynchronize = pool.get(className);
			Collection<CtMethod> methodsToDesynchronize = findMethodsToDesynchronize(classToDesynchronize);
			desynchronizeMethods(methodsToDesynchronize);
			classToDesynchronize.toClass(); // Loads the class into the current ClassLoader
		} catch (Exception e) {
			throw new DesynchronizationException(e);
		}

	}

	private void desynchronizeMethods(Collection<CtMethod> methodsToDesynchronize) {
		for (CtMethod methodToDesynchronize : methodsToDesynchronize) {
			desynchronizeMethod(methodToDesynchronize);
		}
	}

	private void desynchronizeMethod(CtMethod methodToDesynchronize) {
		String methodName = methodToDesynchronize.getName();
		try {
			methodToDesynchronize.setBody("{" +
					"java.lang.reflect.Method thisMethod = $class.getDeclaredMethod(\"" + methodName + "\", $sig);" +
					"net.vidageek.tellme.Desynchronizer.getMessageQueue().addMessage(new net.vidageek.tellme.messaging.MethodCallMessage(this, thisMethod, $args));" +
					"return ($r) null; }");
		} catch (CannotCompileException e) {
			throw new DesynchronizationException(e);
		}
	}

	private Collection<CtMethod> findMethodsToDesynchronize(CtClass classToDesynchronize) {
		Collection<CtMethod> methodsToDesynchronize = new ArrayList<CtMethod>();
		for (CtMethod method : classToDesynchronize.getDeclaredMethods()) {
			if (method.hasAnnotation(Async.class)) {
				methodsToDesynchronize.add(method);
			}
		}
		return methodsToDesynchronize;
	}
}
