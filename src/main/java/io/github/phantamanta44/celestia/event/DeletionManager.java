package io.github.phantamanta44.celestia.event;

import java.util.ArrayDeque;
import java.util.Deque;

import io.github.phantamanta44.celestia.CTMain;
import io.github.phantamanta44.celestia.core.ICTListener;
import io.github.phantamanta44.celestia.util.MathUtils;
import sx.blah.discord.handle.impl.events.MessageSendEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class DeletionManager implements ICTListener {

	private static Deque<IMessage> msgStack = new ArrayDeque<>();
	
	@ListenTo
	public void onMessageSend(MessageSendEvent event) {
		if (!event.getMessage().getChannel().equals(CTMain.dcInstance.getChannel()))
			return;
		if (event.getMessage().getAuthor().equals(CTMain.dcInstance.getBotUser()))
			msgStack.add(event.getMessage());
	}
	
	public static void procCmd(IUser sender, String[] args) {
		int toDelete = 1;
		try {
			toDelete = MathUtils.clamp(Integer.parseInt(args[1]), 1, 10);
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
