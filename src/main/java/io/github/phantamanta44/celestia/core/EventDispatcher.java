package io.github.phantamanta44.celestia.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import io.github.phantamanta44.celestia.CTMain;
import io.github.phantamanta44.celestia.core.ICTListener.ListenTo;
import sx.blah.discord.handle.Event;
import sx.blah.discord.handle.IListener;
import sx.blah.discord.handle.impl.events.MentionEvent;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.MessageSendEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;

@SuppressWarnings("unchecked")
public class EventDispatcher implements IListener<Event> {

	private static final Map<Class<? extends ICTListener>, HandlerSignature> handlerSigMap = new HashMap<>();
	private static final List<ICTListener> handlers = new ArrayList<>();
	private static final Map<Class<? extends Event>, Predicate<Event>> eventSigFuncs = new HashMap<>();
	
	static {
		eventSigFuncs.put(MentionEvent.class, e -> CTMain.dcInstance.isChannel(((MentionEvent)e).getMessage()));
		eventSigFuncs.put(MessageReceivedEvent.class, e -> CTMain.dcInstance.isChannel(((MessageReceivedEvent)e).getMessage()));
		eventSigFuncs.put(MessageSendEvent.class, e -> CTMain.dcInstance.isChannel(((MessageSendEvent)e).getMessage()));
		eventSigFuncs.put(ReadyEvent.class, e -> true);
	}
	
	public static void registerHandler(ICTListener handler) {
		handlers.add(handler);
		Class<? extends ICTListener> handlerClass = handler.getClass();
		if (!handlerSigMap.containsKey(handlerClass))
			handlerSigMap.put(handlerClass, new HandlerSignature(handlerClass));
	}
	
	@Override
	public void handle(Event event) {
		Class<? extends Event> eventType = event.getClass();
		if (!eventSigFuncs.get(eventType).test(event))
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