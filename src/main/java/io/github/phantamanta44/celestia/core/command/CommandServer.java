package io.github.phantamanta44.celestia.core.command;

import java.util.Collections;
import java.util.List;

import io.github.phantamanta44.celestia.CTMain;
import io.github.phantamanta44.celestia.event.ControlPanel;
import sx.blah.discord.handle.obj.IUser;
public class CommandServer implements ICommand {
	
	@Override
	public String getName() {
		return "chserv";
	}

	@Override
	public List<String> getAliases() {
		return Collections.emptyList();
	}

	@Override
	public String getDesc() {
		return "Changes the bot's server of residence.";
	}

	@Override
	public String getUsage() {
		return "chserv <server> <channel>";
	}

	@Override
	public void execute(IUser sender, String[] args) {
		if (args.length < 2) {
			CTMain.dcInstance.sendMessage("You need to specify a server and channel!");
			return;
		}
		try {
			CTMain.dcInstance.setServer(args[0], args[1]);
		} catch (IllegalArgumentException ex) {
			CTMain.dcInstance.sendMessage("No such server or channel!");
		}
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
