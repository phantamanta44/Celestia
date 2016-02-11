package io.github.phantamanta44.celestia.module.core.command;

import java.util.Collections;
import java.util.List;

import io.github.phantamanta44.celestia.CTMain;
import io.github.phantamanta44.celestia.core.ICommand;
import io.github.phantamanta44.celestia.module.ModuleManager;
import sx.blah.discord.handle.obj.IUser;

public class CommandModList implements ICommand {

	@Override
	public String getName() {
		return "modlist";
	}

	@Override
	public List<String> getAliases() {
		return Collections.emptyList();
	}

	@Override
	public String getDesc() {
		return "Get a list of modules.";
	}

	@Override
	public String getUsage() {
		return "modlist";
	}

	@Override
	public void execute(IUser sender, String[] args) {
		CTMain.dcInstance.sendMessage("**Module List:**\n%s", ModuleManager.streamStatus()
				.map(entry -> entry.getValue().getValue() ? String.format("**%s**", entry.getKey()) : entry.getKey())
				.reduce((a, b) -> a.concat(", ").concat(b)).get());
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
