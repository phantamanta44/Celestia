package io.github.phantamanta44.celestia.module.random;

import java.util.Arrays;
import java.util.List;

import io.github.phantamanta44.celestia.core.CommandDispatcher;
import io.github.phantamanta44.celestia.core.EventDispatcher;
import io.github.phantamanta44.celestia.core.ICTListener;
import io.github.phantamanta44.celestia.core.ICommand;
import io.github.phantamanta44.celestia.module.ICTModule;
import io.github.phantamanta44.celestia.module.random.command.CommandBash;
import io.github.phantamanta44.celestia.module.random.command.CommandDuel;
import io.github.phantamanta44.celestia.module.random.command.CommandKhaled;
import io.github.phantamanta44.celestia.module.random.command.CommandRoll;
import io.github.phantamanta44.celestia.module.random.command.CommandSlap;
import io.github.phantamanta44.celestia.module.random.event.DuelManager;

public class RandomModule implements ICTModule {
	
	private static final List<ICommand> cmds = Arrays.asList(new ICommand[] {
			new CommandBash(), new CommandDuel(), new CommandKhaled(), new CommandRoll(),
			new CommandSlap()
	});
	private static final List<ICTListener> listeners = Arrays.asList(new ICTListener[] {
			new DuelManager()
	});

	@Override
	public String getName() {
		return "random";
	}

	@Override
	public void onEnable() {
		cmds.forEach(c -> CommandDispatcher.registerCommand(c));
		listeners.forEach(l -> EventDispatcher.registerHandler(l));
	}

	@Override
	public void onDisable() {
		cmds.forEach(c -> CommandDispatcher.unregisterCommand(c));
		listeners.forEach(l -> EventDispatcher.unregisterHandler(l));
	}

}
