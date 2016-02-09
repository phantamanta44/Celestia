package io.github.phantamanta44.celestia.core.command;

import java.util.Arrays;
import java.util.List;

import io.github.phantamanta44.celestia.event.DuelManager;
import sx.blah.discord.handle.obj.IUser;

public class CommandDuel implements ICommand {

	private static final List<String> ALIASES = Arrays.asList(new String[] {"duel", "fight", "fiteme"});
	
	@Override
	public String getName() {
		return "1v1";
	}

	@Override
	public List<String> getAliases() {
		return ALIASES;
	}

	@Override
	public String getDesc() {
		return "Fights another person in a typing battle.";
	}

	@Override
	public String getUsage() {
		return "1v1 <@person>";
	}

	@Override
	public void execute(IUser sender, String[] args) {
		DuelManager.procCmd(sender, args);
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
