package io.github.phantamanta44.celestia.core.command;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import io.github.phantamanta44.celestia.CTMain;
import io.github.phantamanta44.celestia.util.ChanceList;
import io.github.phantamanta44.celestia.util.MessageUtils;
import sx.blah.discord.handle.obj.IUser;

public class CommandSlap implements ICommand {

	private static final List<String> ALIASES = Arrays.asList(new String[] {"whack"});
	private static final ChanceList<String> slapStrings = new ChanceList<>(
			"\\*Slaps %s\\*", "\\*Smacks %s with a fish\\*", "\\*Whacks %s\\*", "\\*Wallops %s\\*");
	
	@Override
	public String getName() {
		return "slap";
	}

	@Override
	public List<String> getAliases() {
		return ALIASES;
	}

	@Override
	public String getDesc() {
		return "Smacks another person.";
	}

	@Override
	public String getUsage() {
		return "slap <@person>";
	}

	@Override
	public void execute(IUser sender, String[] args) {
		IUser user = MessageUtils.parseName(args, 0);
		if (user == null) {
			CTMain.dcInstance.sendMessage("There's nobody to slap!");
			return;
		}
		Random rand = new Random();
		CTMain.dcInstance.sendMessage(slapStrings.getAtRandom(rand), user.mention());
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
