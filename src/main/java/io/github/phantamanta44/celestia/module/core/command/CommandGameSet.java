package io.github.phantamanta44.celestia.module.core.command;

import java.util.Collections;
import java.util.List;

import io.github.phantamanta44.celestia.CTMain;
import io.github.phantamanta44.celestia.core.ICommand;
import io.github.phantamanta44.celestia.util.MessageUtils;
import sx.blah.discord.handle.obj.IUser;

public class CommandGameSet implements ICommand {

	@Override
	public String getName() {
		return "gameset";
	}

	@Override
	public List<String> getAliases() {
		return Collections.emptyList();
	}

	@Override
	public String getDesc() {
		return "Sets the bot's game caption.";
	}

	@Override
	public String getUsage() {
		return "gameset [name]";
	}

	@Override
	public void execute(IUser sender, String[] args) {
		if (args.length < 2)
			CTMain.dcInstance.setGame(null);
		else
			CTMain.dcInstance.setGame(MessageUtils.concat(args));
	}

	@Override
	public boolean canUseCommand(IUser sender) {
		return CTMain.isAdmin(sender);
	}

	@Override
	public String getPermissionMessage(IUser sender) {
		return "No permission!";
	}
	
	@Override
	public String getEnglishInvokation() {
		return "";
	}

}
