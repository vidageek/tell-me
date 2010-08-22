package net.vidageek.tellme;

import java.util.ArrayList;
import java.util.Collection;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
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
		try {
			String mangledMethodName = copyOriginalMethodWithMangledName(methodToDesynchronize);
			methodToDesynchronize.setBody("{" +
					"java.lang.reflect.Method thisMethod = $class.getDeclaredMethod(\"" + mangledMethodName + "\", $sig);" +
					"net.vidageek.tellme.Desynchronizer.getMessageQueue().addMessage(new net.vidageek.tellme.messaging.MethodCallMessage(this, thisMethod, $args));" +
					"return ($r) null; }");
		} catch (CannotCompileException e) {
			throw new DesynchronizationException(e);
		}
	}

	private String copyOriginalMethodWithMangledName(CtMethod methodToDesynchronize) throws CannotCompileException {
		CtClass declaringClass = methodToDesynchronize.getDeclaringClass();
		String mangledMethodName = mangleMethodName(methodToDesynchronize);
		CtMethod originalMethodWithMangledName = CtNewMethod.copy(methodToDesynchronize, mangledMethodName, declaringClass, null);
		declaringClass.addMethod(originalMethodWithMangledName);
		return mangledMethodName;
	}

	private String mangleMethodName(CtMethod methodToDesynchronize) {
		return methodToDesynchronize.getName() + "$$__desynchronized__";
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
