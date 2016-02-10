package io.github.phantamanta44.celestia.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import io.github.phantamanta44.celestia.CTMain;
import io.github.phantamanta44.celestia.core.ICTListener.ListenTo;
import sx.blah.discord.handle.Event;
import sx.blah.discord.handle.EventSubscriber;
import sx.blah.discord.handle.impl.events.MentionEvent;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.MessageSendEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;

@SuppressWarnings("unchecked")
public class EventDispatcher {

	private static final Map<Class<? extends ICTListener>, HandlerSignature> handlerSigMap = new HashMap<>();
	private static final List<ICTListener> handlers = new ArrayList<>();
	private static final Map<Class<? extends Event>, Function<Event, Object>> eventSigFuncs = new HashMap<>();
	
	static {
		eventSigFuncs.put(MentionEvent.class, e -> ((MentionEvent)e).getMessage());
		eventSigFuncs.put(MessageReceivedEvent.class, e -> ((MessageReceivedEvent)e).getMessage());
		eventSigFuncs.put(MessageSendEvent.class, e -> ((MessageSendEvent)e).getMessage());
		eventSigFuncs.put(ReadyEvent.class, e -> Boolean.TRUE);
	}
	
	public static void registerHandler(ICTListener handler) {
		handlers.add(handler);
		Class<? extends ICTListener> handlerClass = handler.getClass();
		if (!handlerSigMap.containsKey(handlerClass))
			handlerSigMap.put(handlerClass, new HandlerSignature(handlerClass));
	}
	
	@EventSubscriber
	public void acceptEvent(Event event) {
		Class<? extends Event> eventType = event.getClass();
		if (!testEvent(eventType, event))
			return;
		for (ICTListener listener : handlers) {
			Method listenerMethod;
			HandlerSignature handlerSig = handlerSigMap.get(listener.getClass());
			if ((listenerMethod = handlerSig.listenerMethods.get(eventType)) != null) {
				try {
					listenerMethod.invoke(listener, event);
				} catch (Exception e) {
					CTMain.logger.severe("Event handling error!");
					e.printStackTrace();
				}
			}
		}
	}
	
	private static boolean testEvent(Class<? extends Event> eventType, Event event) {
		Function<Event, Object> fnc = eventSigFuncs.get(eventType);
		if (fnc == null)
			return false;		
		Object res = fnc.apply(event);
		if (res instanceof Boolean)
			return ((Boolean)res).booleanValue();
		if (res instanceof IMessage)
			return CTMain.dcInstance.isChannel((IMessage)res);
		if (res instanceof IChannel)
			return CTMain.dcInstance.isChannel((IChannel)res);
		if (res instanceof IGuild)
			return CTMain.dcInstance.isServer((IGuild)res);
		throw new UnsupportedOperationException();
	}

	private static class HandlerSignature {
		
		public final Map<Class<? extends Event>, Method> listenerMethods = new HashMap<>();
		
		public HandlerSignature(Class<? extends ICTListener> listenerClass) {
			Method[] methods = listenerClass.getMethods();
			for (Method method : methods) {
				if (method.isAnnotationPresent(ListenTo.class)
					&& method.getParameterCount() == 1
					&& Event.class.isAssignableFrom(method.getParameterTypes()[0])) {
					Class<? extends Event> eventType = (Class<? extends Event>)method.getParameterTypes()[0];
					if (this.listenerMethods.containsKey(eventType))
						throw new IllegalStateException("Duplicate listener methods for event " + eventType.getName() + "!");
					this.listenerMethods.put(eventType, method);
				}
			}
		}
		
	}
	
}