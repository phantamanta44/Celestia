package io.github.phantamanta44.celestia.core.command;

import java.util.Collections;
import java.util.List;

import io.github.phantamanta44.celestia.CTMain;
import io.github.phantamanta44.celestia.event.ControlPanel;
import sx.blah.discord.handle.obj.IUser;
public class CommandChannel implements ICommand {
	
	@Override
	public String getName() {
		return "chchan";
	}

	@Override
	public List<String> getAliases() {
		return Collections.emptyList();
	}

	@Override
	public String getDesc() {
		return "Changes the bot's channel of residence.";
	}

	@Override
	public String getUsage() {
		return "chchan <channel>";
	}

	@Override
	public void execute(IUser sender, String[] args) {
		if (args.length < 1) {
			CTMain.dcInstance.sendMessage("You need to specify a channel!");
			return;
		}
		try {
			CTMain.dcInstance.setChannel(args[0]);
		} catch (IllegalArgumentException ex) {
			CTMain.dcInstance.sendMessage("No such channel!");
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
