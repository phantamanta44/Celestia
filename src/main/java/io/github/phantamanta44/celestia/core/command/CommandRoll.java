package io.github.phantamanta44.celestia.core.command;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import io.github.phantamanta44.celestia.CTMain;
import sx.blah.discord.handle.obj.IUser;

public class CommandRoll implements ICommand {

	private static final List<String> ALIASES = Arrays.asList(new String[] {"dice"});
	
	@Override
	public String getName() {
		return "roll";
	}

	@Override
	public List<String> getAliases() {
		return ALIASES;
	}

	@Override
	public String getDesc() {
		return "Rolls an n-sided fair die.";
	}

	@Override
	public String getUsage() {
		return "roll [#faces]";
	}

	@Override
	public void execute(IUser sender, String[] args) {
		int sides = 6;
		try {
			sides = Math.max(Integer.parseInt(args[0]), 2);
		} catch (Exception ex) { }
		Random rand = new Random();
		CTMain.dcInstance.sendMessage("Rolled a D%s: %s", sides, rand.nextInt(sides) + 1);
	}

	@Override
	public boolean canUseCommand(IUser sender) {
		return true;
	}

	@Override
	public String getPermissionMessage(IUser sender) {
		throw new IllegalStateException();
	}

}
