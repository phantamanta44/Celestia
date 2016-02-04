package io.github.phantamanta44.celestia.event;

import java.util.Random;

import io.github.phantamanta44.celestia.CTMain;
import sx.blah.discord.handle.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

public class UtilCommands {

	@EventSubscriber
	public void onMessageReceived(MessageReceivedEvent event) {
		String msg = event.getMessage().getContent();
		if (msg.startsWith("!roll"))
			simulateDiceRoll(msg);
	}
	
	private void simulateDiceRoll(String msg) {
		int sides = 6;
		String[] parts = msg.split("\\s");
		try {
			sides = Integer.parseInt(parts[1]);
		} catch (NumberFormatException ex) { }
		Random rand = new Random();
		CTMain.dcInstance.sendMessage()
	}
	
}
