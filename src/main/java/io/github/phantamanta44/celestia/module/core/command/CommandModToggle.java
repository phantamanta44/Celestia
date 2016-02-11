package io.github.phantamanta44.celestia.module.core.command;

import java.util.Collections;
import java.util.List;

import io.github.phantamanta44.celestia.CTMain;
import io.github.phantamanta44.celestia.core.ICommand;
import io.github.phantamanta44.celestia.module.ModuleManager;
import sx.blah.discord.handle.obj.IUser;

public class CommandModToggle implements ICommand {

	@Override
	public String getName() {
		return "modtoggle";
	}

	@Override
	public List<String> getAliases() {
		return Collections.emptyList();
	}

	@Override
	public String getDesc() {
		return "Toggle a module.";
	}

	@Override
	public String getUsage() {
		return "modtoggle <module>";
	}

	@Override
	public void execute(IUser sender, String[] args) {
		if (args.length < 1) {
			CTMain.dcInstance.sendMessage("You need to specify a module!");
			return;
		}
		if (!ModuleManager.isModule(args[0])) {
			CTMain.dcInstance.sendMessage("Not a valid module!");
			return;
		}
		String id = args[0].toLowerCase();
		boolean newState = !ModuleManager.isEnabled(id);
		ModuleManager.setState(id, newState);
		CTMain.dcInstance.sendMessage("%s the %s module.", newState ? "Enabled" : "Disabled", id.toLowerCase());
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
