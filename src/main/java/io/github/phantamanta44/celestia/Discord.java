package io.github.phantamanta44.celestia;

import java.io.IOException;
import java.util.Collection;

import io.github.phantamanta44.celestia.event.DuelManager;
import io.github.phantamanta44.celestia.event.UtilCommands;
import io.github.phantamanta44.celestia.event.trivia.TriviaManager;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
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
		registerListener(new UtilCommands());
		registerListener(new TriviaManager());
		registerListener(new DuelManager());
	}
	
	public void registerListener(Object listener) {
		dcClient.getDispatcher().registerListener(listener);
	}
	
	public void onReady(ReadyEvent event) {
		setServer(CTMain.config.get("server"), CTMain.config.get("channel"));
		CTMain.logger.info("Login successful! Username: %s, Token: %s", dcClient.getOurUser().getName(), dcClient.getToken());
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
	
	public IGuild getServer() {
		return server;
	}

	public IChannel getChannel() {
		return channel;
	}

}
