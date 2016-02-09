package io.github.phantamanta44.celestia.core.command;

import java.util.List;

import sx.blah.discord.handle.obj.IUser;

public interface ICommand {

	public String getName();
	
	public List<String> getAliases();
	
	public String getDesc();
	
	public String getUsage();
	
	public void execute(IUser sender, String[] args);
	
	public boolean canUseCommand(IUser sender);
	
	public String getPermissionMessage(IUser sender);
	
}