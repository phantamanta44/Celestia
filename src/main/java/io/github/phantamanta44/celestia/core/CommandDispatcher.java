package io.github.phantamanta44.celestia.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import io.github.phantamanta44.celestia.CTMain;
import io.github.phantamanta44.celestia.util.MessageUtils;
import sx.blah.discord.handle.impl.events.MentionEvent;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;

public class CommandDispatcher implements ICTListener {
	
	protected static final Map<String, ICommand> cmdMapping = new ConcurrentHashMap<>();
	protected static final Map<String, ICommand> aliasMapping = new ConcurrentHashMap<>();
	protected static final Map<String, ICommand> regexMapping = new ConcurrentHashMap<>();
	
	public static void registerCommand(ICommand cmd) {
		cmdMapping.put(cmd.getName().toLowerCase(), cmd);
		aliasMapping.put(cmd.getName().toLowerCase(), cmd);
		regexMapping.put(cmd.getEnglishInvokation(), cmd);
		cmd.getAliases().forEach(a -> aliasMapping.put(a.toLowerCase(), cmd));
	}
	
	public static void unregisterCommand(ICommand cmd) {
		cmdMapping.remove(cmd.getName().toLowerCase(), cmd);
		aliasMapping.remove(cmd.getName().toLowerCase(), cmd);
		regexMapping.remove(cmd.getEnglishInvokation(), cmd);
		cmd.getAliases().forEach(a -> aliasMapping.remove(a.toLowerCase(), cmd));
	}
	
	public CommandDispatcher() {
		cmdMapping.clear();
		aliasMapping.clear();
	}
	
	public static Stream<ICommand> streamCommands() {
		return cmdMapping.values().stream();
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
	
	@ListenTo
	public void onMention(MentionEvent event) {
		String msg = event.getMessage().getContent(), men = CTMain.dcInstance.getBotUser().mention();
		IUser sender = event.getMessage().getAuthor();
		if (!msg.startsWith(men) && !msg.endsWith(men))
			return;
		for (Entry<String, ICommand> entry : regexMapping.entrySet()) {
			Matcher m = Pattern.compile(entry.getKey(), Pattern.CASE_INSENSITIVE).matcher(msg);
			if (!m.matches())
				continue;
			ICommand cmd = entry.getValue();
			List<String> args = new ArrayList<>();
			for (int i = 0; true; i++) {
				try {
					String g = m.group("a" + i);
					if (g == null)
						break;
					args.add(g);
				} catch (IllegalArgumentException ex) {
					break;
				}
			}
			CTMain.logger.info("Received einvoc: \"%s\" for %s %s", msg, cmd.getName(),
					args.stream().reduce((a, b) -> a.concat(" ").concat(b)).orElse(""));
			if (cmd.canUseCommand(sender))
				cmd.execute(sender, args.toArray(new String[0]));
			else
				CTMain.dcInstance.sendMessage("%s: %s", sender.mention(), cmd.getPermissionMessage(sender));
		}
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