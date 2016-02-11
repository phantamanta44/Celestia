package io.github.phantamanta44.celestia.module.core;

import java.util.Arrays;
import java.util.List;

import io.github.phantamanta44.celestia.core.CommandDispatcher;
import io.github.phantamanta44.celestia.core.EventDispatcher;
import io.github.phantamanta44.celestia.core.ICTListener;
import io.github.phantamanta44.celestia.core.ICommand;
import io.github.phantamanta44.celestia.module.ICTModule;
import io.github.phantamanta44.celestia.module.core.command.CommandChannel;
import io.github.phantamanta44.celestia.module.core.command.CommandGameSet;
import io.github.phantamanta44.celestia.module.core.command.CommandHalt;
import io.github.phantamanta44.celestia.module.core.command.CommandHelp;
import io.github.phantamanta44.celestia.module.core.command.CommandInfo;
import io.github.phantamanta44.celestia.module.core.command.CommandModList;
import io.github.phantamanta44.celestia.module.core.command.CommandModToggle;
import io.github.phantamanta44.celestia.module.core.command.CommandPrefix;
import io.github.phantamanta44.celestia.module.core.command.CommandServer;
import io.github.phantamanta44.celestia.module.core.command.CommandUnsay;
import io.github.phantamanta44.celestia.module.core.event.DeletionManager;

public class CoreModule implements ICTModule {
	
	private static final List<ICommand> cmds = Arrays.asList(new ICommand[] {
			new CommandChannel(), new CommandGameSet(), new CommandHalt(), new CommandHelp(),
			new CommandInfo(), new CommandModList(), new CommandModToggle(), new CommandPrefix(),
			new CommandServer(), new CommandUnsay()
	});
	private static final List<ICTListener> listeners = Arrays.asList(new ICTListener[] {
			new DeletionManager()
	});

	@Override
	public String getName() {
		return "core";
	}

	@Override
	public void onEnable() {
		cmds.forEach(c -> CommandDispatcher.registerCommand(c));
		listeners.forEach(l -> EventDispatcher.registerHandler(l));
	}

	@Override
	public void onDisable() {
		throw new IllegalStateException();
	}

}
