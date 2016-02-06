package io.github.phantamanta44.celestia.event;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import io.github.phantamanta44.celestia.CTMain;
import sx.blah.discord.handle.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.PrivateChannel;
import sx.blah.discord.handle.obj.IUser;

public class ControlPanel {
	
	private static final Set<String> controllers = new HashSet<>();

	public ControlPanel() throws IOException {
		BufferedReader strIn = new BufferedReader(new FileReader(new File("admins.txt")));
		String line;
		while ((line = strIn.readLine()) != null)
			controllers.add(line);
		strIn.close();
	}
	
	@EventSubscriber
	public void onMessageReceived(MessageReceivedEvent event) throws Exception {
		if (!event.getMessage().getChannel().isPrivate())
			return;
		PrivateChannel chan = (PrivateChannel)event.getMessage().getChannel();
		if (!isAdmin(chan.getRecipient()))
			return;
		String msg = event.getMessage().getContent();
		if (msg.startsWith("!gameset")) {
			setGame(msg);
			chan.sendMessage("Game changed.");
		}
		else if (msg.startsWith("!halt")) {
			chan.sendMessage("Halting!");
			attemptHalt(msg);
		}
	}
	
	private static void setGame(String msg) {
		CTMain.logger.info("Received control: %s", msg);
		String[] parts = msg.split("\\s", 2);
		if (parts.length < 2)
			CTMain.dcInstance.setGame(null);
		else
			CTMain.dcInstance.setGame(parts[1]);
	}
	
	private static void attemptHalt(String msg) {
		CTMain.logger.info("Received control: %s", msg);
		Runtime.getRuntime().exit(0);
	}
	
	public static boolean isAdmin(IUser user) {
		return controllers.contains(user.getName());
	}
	
}
