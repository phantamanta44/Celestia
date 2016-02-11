package io.github.phantamanta44.celestia.core.command;

import java.util.Arrays;
import java.util.List;

import io.github.phantamanta44.celestia.CTMain;
import sx.blah.discord.handle.obj.IUser;

public class CommandHelp implements ICommand {
	
	private static final List<String> ALIASES = Arrays.asList(new String[] {"?", "helpme"});

	@Override
	public String getName() {
		return "help";
	}
	
	@Override
	public List<String> getAliases() {
		return ALIASES;
	}
	
	@Override
	public String getDesc() {
		return "Lists usable commands.";
	}

	@Override
	public String getUsage() {
		return "help";
	}

	@Override
	public void execute(IUser sender, String[] args) {
		String helpText = CommandDispatcher.cmdMapping.values().stream()
			.map(c -> String.format("%s%s - %s", CTMain.config.get("prefix"), c.getUsage(), c.getDesc()))
			.sorted()
			.reduce((a, b) -> a.concat("\n").concat(b)).get();
		CTMain.dcInstance.sendMessage("**Command list:**\n```%s```", helpText);
	}

	@Override
	public boolean canUseCommand(IUser sender) {
		return true;
	}
	
	@Override
	public String getPermissionMessage(IUser sender) {
		throw new IllegalStateException();
	}
	
	@Override
	public String getEnglishInvokation() {
		return "";
	}

}