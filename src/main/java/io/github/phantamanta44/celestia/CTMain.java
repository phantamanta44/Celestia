package io.github.phantamanta44.celestia;

import io.github.phantamanta44.celestia.core.EventDispatcher;
import io.github.phantamanta44.celestia.core.command.CommandBash;
import io.github.phantamanta44.celestia.core.command.CommandDispatcher;
import io.github.phantamanta44.celestia.core.command.CommandDuel;
import io.github.phantamanta44.celestia.core.command.CommandGameSet;
import io.github.phantamanta44.celestia.core.command.CommandHalt;
import io.github.phantamanta44.celestia.core.command.CommandHelp;
import io.github.phantamanta44.celestia.core.command.CommandRoll;
import io.github.phantamanta44.celestia.core.command.CommandSlap;
import io.github.phantamanta44.celestia.core.command.CommandUnsay;
import io.github.phantamanta44.celestia.event.ControlPanel;
import io.github.phantamanta44.celestia.event.DeletionManager;
import io.github.phantamanta44.celestia.event.DuelManager;

public class CTMain {
	
	public static final Discord dcInstance = new Discord();
	public static final LogWrapper logger = new LogWrapper("CT");
	public static final CTConfig config = new CTConfig("celestia.conf");
	
	public static void main(String[] args) {
		try {
			config.read();
			new ControlPanel(); // TODO move this elsewhere
			dcInstance.initApi(config.get("email"), config.get("pass"));
			dcInstance.registerListeners();
			registerListeners();
			dcInstance.login();
		} catch (Exception e) {
			logger.severe("Something went wrong!");
			e.printStackTrace();
		}
	}
	
	private static void registerListeners() throws Exception {
		CommandDispatcher.registerCommand(new CommandHalt());
		CommandDispatcher.registerCommand(new CommandGameSet());
		EventDispatcher.registerHandler(new DuelManager());
		CommandDispatcher.registerCommand(new CommandDuel());
		EventDispatcher.registerHandler(new DeletionManager());
		CommandDispatcher.registerCommand(new CommandUnsay());
		CommandDispatcher.registerCommand(new CommandSlap());
		CommandDispatcher.registerCommand(new CommandRoll());
		CommandDispatcher.registerCommand(new CommandBash());
		CommandDispatcher.registerCommand(new CommandHelp());
	}

}