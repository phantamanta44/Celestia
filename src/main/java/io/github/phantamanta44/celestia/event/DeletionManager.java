package io.github.phantamanta44.celestia.event;

import java.util.ArrayDeque;
import java.util.Deque;

import io.github.phantamanta44.celestia.CTMain;
import io.github.phantamanta44.celestia.util.MathUtils;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.MessageSendEvent;
import sx.blah.discord.handle.obj.IMessage;

public class DeletionManager {

	private static Deque<IMessage> msgStack = new ArrayDeque<>();
	
	public void onMessageSend(MessageSendEvent event) {
		if (!event.getMessage().getChannel().equals(CTMain.dcInstance.getChannel()))
			return;
		if (event.getMessage().getAuthor().equals(CTMain.dcInstance.getBotUser()))
			msgStack.add(event.getMessage());
	}
	
	public void onMessageReceived(MessageReceivedEvent event) throws Exception {
		if (!event.getMessage().getChannel().equals(CTMain.dcInstance.getChannel()))
			return;
		String msg = event.getMessage().getContent();
		String[] parts = msg.split("\\s");
		if (event.getMessage().getContent().startsWith("!unsay") && ControlPanel.isAdmin(event.getMessage().getAuthor())) {
			CTMain.logger.info("Received command: %s", msg);
			int toDelete = 1;
			try {
				toDelete = MathUtils.clamp(Integer.parseInt(parts[1]), 1, 10);
			} catch (Exception ex) { }
			for (int i = 0; i < toDelete; i++) {
				IMessage td = msgStack.pollLast();
				if (td == null)
					break;
				try {
					td.delete();
				} catch (Exception ex) {
					CTMain.logger.warn(ex.getMessage());
					toDelete++;
				}
			}
		}
	}
	
}
