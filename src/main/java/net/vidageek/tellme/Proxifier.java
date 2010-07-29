package net.vidageek.tellme;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.NoOp;

public final class Proxifier {

	private static final List<Method> OBJECT_METHODS = Arrays.asList(Object.class.getDeclaredMethods());
	private static final CallbackFilter IGNORE_BRIDGE_AND_OBJECT_METHODS = new CallbackFilter() {
		@Override
		public int accept(Method method) {
			return method.isBridge() || !method.isAnnotationPresent(Async.class) || OBJECT_METHODS.contains(method) ? 1 : 0;
		}
	};

	private final MethodInterceptor messageEnqueuerCallback;

	public Proxifier(MethodInterceptor enqueuer) {
		this.messageEnqueuerCallback = enqueuer;
	}

	public <T> T proxify(Class<T> toProxify) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(toProxify);
		enhancer.setCallbackFilter(IGNORE_BRIDGE_AND_OBJECT_METHODS);
		enhancer.setCallbacks(new Callback[] {messageEnqueuerCallback, NoOp.INSTANCE});
		return (T) enhancer.create();
	}

}
