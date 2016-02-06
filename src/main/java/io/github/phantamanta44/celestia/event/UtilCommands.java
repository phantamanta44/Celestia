package io.github.phantamanta44.celestia.event;

import java.util.Random;

import io.github.phantamanta44.celestia.CTMain;
import io.github.phantamanta44.celestia.util.ChanceList;
import io.github.phantamanta44.celestia.util.MessageUtils;
import sx.blah.discord.handle.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;

public class UtilCommands {
	
	private static final ChanceList<String> slapStrings = new ChanceList<>(
		"\\*Slaps %s\\*", "\\*Smacks %s with a fish\\*", "\\*Whacks %s\\*", "\\*Wallops %s\\*");

	@EventSubscriber
	public void onMessageReceived(MessageReceivedEvent event) {
		if (!event.getMessage().getChannel().equals(CTMain.dcInstance.getChannel()))
			return;
		String msg = event.getMessage().getContent();
		if (msg.startsWith("!roll"))
			simulateDiceRoll(msg);
		else if (msg.startsWith("!slap")) {
			if (!slapPerson(msg))
				CTMain.dcInstance.sendMessage("There's nobody to slap!");
		}
	}
	
	private static void simulateDiceRoll(String msg) {
		CTMain.logger.info("Received command: %s", msg);
		int sides = 6;
		String[] parts = msg.split("\\s");
		try {
			sides = Math.max(Integer.parseInt(parts[1]), 2);
		} catch (Exception ex) { }
		Random rand = new Random();
		CTMain.dcInstance.sendMessage("Rolled a D%s: %s", sides, rand.nextInt(sides) + 1);
	}
	
	private static boolean slapPerson(String msg) {
		CTMain.logger.info("Received command: %s", msg);
		String[] parts = msg.split("\\s");
		IUser user = MessageUtils.parseName(parts);
		if (user == null)
			return false;
		Random rand = new Random();
		CTMain.dcInstance.sendMessage(slapStrings.getAtRandom(rand), user.mention());
		return true;
	}
	
}
