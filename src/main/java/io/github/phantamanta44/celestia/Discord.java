package io.github.phantamanta44.celestia;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import io.github.phantamanta44.celestia.core.EventDispatcher;
import io.github.phantamanta44.celestia.core.command.CommandDispatcher;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class Discord {
	
	private IDiscordClient dcClient;
	private IGuild server;
	private IChannel channel;
	
	public void initApi(String email, String pass) throws DiscordException {
		CTMain.logger.info("Attempting to build Discord client...");
		dcClient = new ClientBuilder().withLogin(email, pass).build();
	}
	
	public void login() throws DiscordException {
		CTMain.logger.info("Attempting to log into Discord...");
		dcClient.login();
	}
	
	public IDiscordClient getClient() {
		return dcClient;
	}
	
	public void setServer(String servName, String chanName) throws IllegalArgumentException {
		server = dcClient.getGuilds().stream()
			.filter(s -> s.getName().equalsIgnoreCase(servName))
			.findFirst().orElseThrow(() -> new IllegalArgumentException("Server does not exist!"));
		channel = server.getChannels().stream()
			.filter(s -> s.getName().equalsIgnoreCase(chanName))
			.findFirst().orElseThrow(() -> new IllegalArgumentException("Channel does not exist!"));
	}
	
	public void registerListeners() throws IOException {
		registerListener(this);
		registerListener(new EventDispatcher());
		EventDispatcher.registerHandler(new CommandDispatcher());
	}
	
	public void registerListener(Object listener) {
		dcClient.getDispatcher().registerListener(listener);
	}
	
	@EventSubscriber
	public void onReady(ReadyEvent event) {
		setServer(CTMain.config.get("server"), CTMain.config.get("channel"));
		CTMain.logger.info("Login successful! Username: %s, Token: %s", dcClient.getOurUser().getName(), dcClient.getToken());
		setGame(CTMain.config.get("game"));
	}
	
	public void sendMessage(String msg) {
		try {
			channel.sendMessage(msg);
		} catch (Exception ex) {
			CTMain.logger.warn("Error sending message \"%s\"!", msg);
			ex.printStackTrace();
		}
	}

	public void sendMessage(String format, Object... args) {
		sendMessage(String.format(format, args));
	}

	public Collection<IUser> getUsers() {
		return server.getUsers();
	}
	
	public IUser getUser(String id) {
		return server.getUserByID(id);
	}
	
	public IUser getBotUser() {
		return dcClient.getOurUser();
	}
	
	public IGuild getServer() {
		return server;
	}

	public IChannel getChannel() {
		return channel;
	}
	
	public boolean isServer(IGuild guild) {
		if (server == null)
			return false;
		return guild.getID().equalsIgnoreCase(server.getID());
	}
	
	public boolean isChannel(IChannel chan) {
		if (channel == null)
			return false;
		return chan.getID().equalsIgnoreCase(channel.getID());
	}
	
	public boolean isChannel(IMessage msg) {
		return isChannel(msg.getChannel());
	}

	public void setGame(String gameName) {
		if (gameName == null || gameName.isEmpty())
			dcClient.updatePresence(false, Optional.empty());
		else
			dcClient.updatePresence(false, Optional.of(gameName));
	}

}
