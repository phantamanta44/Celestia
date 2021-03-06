package io.github.phantamanta44.celestia.module.scripting;

import java.util.Arrays;
import java.util.List;

import io.github.phantamanta44.celestia.core.CommandDispatcher;
import io.github.phantamanta44.celestia.core.EventDispatcher;
import io.github.phantamanta44.celestia.core.ICTListener;
import io.github.phantamanta44.celestia.core.ICommand;
import io.github.phantamanta44.celestia.module.ICTModule;
import io.github.phantamanta44.celestia.module.scripting.command.CommandEval;

public class ScriptingModule implements ICTModule {
	
	private static final List<ICommand> cmds = Arrays.asList(new ICommand[] {
			new CommandEval()
	});
	private static final List<ICTListener> listeners = Arrays.asList(new ICTListener[] {
			
	});

	@Override
	public String getName() {
		return "scripting";
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
