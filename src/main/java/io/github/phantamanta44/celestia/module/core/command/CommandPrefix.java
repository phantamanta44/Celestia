package io.github.phantamanta44.celestia.module.core.command;

import java.util.Collections;
import java.util.List;

import io.github.phantamanta44.celestia.CTMain;
import io.github.phantamanta44.celestia.core.ICommand;
import io.github.phantamanta44.celestia.util.MessageUtils;
import sx.blah.discord.handle.obj.IUser;

public class CommandPrefix implements ICommand {
	
	@Override
	public String getName() {
		return "chpref";
	}

	@Override
	public List<String> getAliases() {
		return Collections.emptyList();
	}

	@Override
	public String getDesc() {
		return "Change the command prefix.";
	}

	@Override
	public String getUsage() {
		return "chpref <prefix>";
	}

	@Override
	public void execute(IUser sender, String[] args) {
		if (args.length < 1) {
			CTMain.dcInstance.sendMessage("You need to provide a prefix!");
			return;
		}
		String pref = MessageUtils.concat(args);
		if ((pref.startsWith("'") && pref.endsWith("'"))
				|| (pref.startsWith("\"") && pref.endsWith("\"")))
			pref = pref.substring(1, pref.length() - 1);
		CTMain.config.set("prefix", pref);
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
