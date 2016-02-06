package io.github.phantamanta44.celestia.event;

import java.util.HashSet;
import java.util.Set;

import sx.blah.discord.handle.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.PrivateChannel;

public class ControlPanel {
	
	private static final Set<String> controllers = new HashSet<>();

	@EventSubscriber
	public void onMessageReceived(MessageReceivedEvent event) {
		if (!event.getMessage().getChannel().isPrivate())
			return;
		PrivateChannel chan = (PrivateChannel)event.getMessage().getChannel();
		if (!controllers.contains(chan.getRecipient().getName()))
			return;
		// Control panel logic
	}
	
}
