package io.github.phantamanta44.celestia.core.command;

import java.util.Arrays;
import java.util.List;

import io.github.phantamanta44.celestia.event.ControlPanel;
import io.github.phantamanta44.celestia.event.DeletionManager;
import sx.blah.discord.handle.obj.IUser;

public class CommandUnsay implements ICommand {

	private static final List<String> ALIASES = Arrays.asList(new String[] {"delete", "revoke"});
	
	@Override
	public String getName() {
		return "unsay";
	}

	@Override
	public List<String> getAliases() {
		return ALIASES;
	}

	@Override
	public String getDesc() {
		return "Unsays a previous message by the bot.";
	}

	@Override
	public String getUsage() {
		return "unsay [count]";
	}

	@Override
	public void execute(IUser sender, String[] args) {
		DeletionManager.procCmd(sender, args);
	}

	@Override
	public boolean canUseCommand(IUser sender) {
		return ControlPanel.isAdmin(sender);
	}

	@Override
	public String getPermissionMessage(IUser sender) {
		return "No permission!";
	}

}
