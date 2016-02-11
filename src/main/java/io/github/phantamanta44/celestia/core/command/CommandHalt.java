package io.github.phantamanta44.celestia.core.command;

import java.util.Collections;
import java.util.List;

import io.github.phantamanta44.celestia.CTMain;
import io.github.phantamanta44.celestia.event.ControlPanel;
import sx.blah.discord.handle.obj.IUser;

public class CommandHalt implements ICommand {

	@Override
	public String getName() {
		return "halt";
	}

	@Override
	public List<String> getAliases() {
		return Collections.emptyList();
	}

	@Override
	public String getDesc() {
		return "Stops the bot.";
	}

	@Override
	public String getUsage() {
		return "halt";
	}

	@Override
	public void execute(IUser sender, String[] args) {
		CTMain.dcInstance.sendMessage("Halting!");
		Runtime.getRuntime().exit(0);
	}

	@Override
	public boolean canUseCommand(IUser sender) {
		return ControlPanel.isAdmin(sender);
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
