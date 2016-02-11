package io.github.phantamanta44.celestia.core.command;

import java.util.Arrays;
import java.util.List;

import io.github.phantamanta44.celestia.CTMain;
import io.github.phantamanta44.celestia.util.MessageUtils;
import sx.blah.discord.handle.obj.IUser;

public class CommandEcho implements ICommand {

	private static final List<String> ALIASES = Arrays.asList(new String[] {"say", "print"});
	
	@Override
	public String getName() {
		return "echo";
	}

	@Override
	public List<String> getAliases() {
		return ALIASES;
	}

	@Override
	public String getDesc() {
		return "Causes the bot to say something.";
	}

	@Override
	public String getUsage() {
		return "echo [text]";
	}

	@Override
	public void execute(IUser sender, String[] args) {
		CTMain.dcInstance.sendMessage(MessageUtils.concat(args));
	}

	@Override
	public boolean canUseCommand(IUser sender) {
		return true;
	}

	@Override
	public String getPermissionMessage(IUser sender) {
		throw new IllegalStateException();
	}

}
