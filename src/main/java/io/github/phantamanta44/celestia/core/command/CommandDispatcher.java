package io.github.phantamanta44.celestia.core.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.github.phantamanta44.celestia.CTMain;
import io.github.phantamanta44.celestia.core.ICTListener;
import io.github.phantamanta44.celestia.util.MessageUtils;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;

public class CommandDispatcher implements ICTListener {
	
	protected static final Map<String, ICommand> cmdMapping = new HashMap<>();
	protected static final Map<String, ICommand> aliasMapping = new HashMap<>();
	
	public static void registerCommand(ICommand cmd) {
		cmdMapping.put(cmd.getName().toLowerCase(), cmd);
		aliasMapping.put(cmd.getName().toLowerCase(), cmd);
		cmd.getAliases().forEach(a -> aliasMapping.put(a.toLowerCase(), cmd));
	}
	
	public CommandDispatcher() {
		cmdMapping.clear();
		aliasMapping.clear();
	}

	@ListenTo
	public void onMessageReceived(MessageReceivedEvent event) {
		processEvent(event.getMessage().getAuthor(), event.getMessage().getContent());
	}
	
	private void processEvent(IUser sender, String msg) {
		String pref = CTMain.config.get("prefix");
		if (!msg.toLowerCase().startsWith(pref.toLowerCase()))
			return;
		String[] msgSplit = msg.substring(pref.length()).split("\\s");
		String cmd = msgSplit[0];
		String[] args;
		if (msgSplit.length > 1)
			args = Arrays.copyOfRange(msgSplit, 1, msgSplit.length);
		else
			args = new String[0];
		processCommand(sender, cmd, args);
	}
	
	private void processCommand(IUser sender, String cmdName, String[] args) {
		ICommand cmd;
		if ((cmd = aliasMapping.get(cmdName)) != null) {
			CTMain.logger.info("Received command: %s %s", cmdName, MessageUtils.concat(args));
			if (cmd.canUseCommand(sender))
				cmd.execute(sender, args);
			else
				CTMain.dcInstance.sendMessage("%s: %s", sender.mention(), cmd.getPermissionMessage(sender));
		}
	}
	
}