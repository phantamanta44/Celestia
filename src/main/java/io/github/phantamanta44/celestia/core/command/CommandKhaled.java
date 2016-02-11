package io.github.phantamanta44.celestia.core.command;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import io.github.phantamanta44.celestia.CTMain;
import io.github.phantamanta44.celestia.util.ChanceList;
import sx.blah.discord.handle.obj.IUser;

public class CommandKhaled implements ICommand {

	private static final List<String> ALIASES = Arrays.asList(new String[] {"djkhaled", "anotherone", "inspireme"});
	private static final ChanceList<String> khaledisms = new ChanceList<>(
			"Another one.", "Bless up.", "We the best.", "I changed. A lot.", "Win, win, win, no matter what.",
			"You smart.", "You loyal.", "You grateful.", "I appreciate you.", "You a genius.",
			"You can put the hinges on the hands, too.", "I changed. A lot. You can too.",
			"Never give up.", "Never surrender.", "Buy yo' mama a house.", "Buy yo' whole family houses.",
			"Never play yourself.", "They don't want you to succeed.", "You very smart.");
	
	@Override
	public String getName() {
		return "khaled";
	}

	@Override
	public List<String> getAliases() {
		return ALIASES;
	}

	@Override
	public String getDesc() {
		return "We the best.";
	}

	@Override
	public String getUsage() {
		return "khaled [#iter]";
	}

	@Override
	public void execute(IUser sender, String[] args) {
		int iter = 3;
		try {
			iter = Integer.parseInt(args[0]);
		} catch (Exception ex) { }
		StringBuilder toSend = new StringBuilder();
		Random rand = new Random();
		for (int i = 0; i < iter; i++)
			toSend.append(khaledisms.getAtRandom(rand)).append("\n");
		CTMain.dcInstance.sendMessage(toSend.toString());
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
