package io.github.phantamanta44.celestia;

import io.github.phantamanta44.celestia.event.UtilCommands;
import io.github.phantamanta44.celestia.event.trivia.TriviaManager;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

public class Discord {
	
	private IDiscordClient dcClient;
	private IGuild server;
	private IChannel channel;
	
	public void initApi(String email, String pass) throws DiscordException {
		CTMain.logger.info("Attempting to build Discord client...");
		dcClient = new ClientBuilder().withLogin(email, pass).build();
		CTMain.logger.info("Attempting to log into Discord...");
		dcClient.login();
		CTMain.logger.info("Login successful! Username: %s, Token: %s", dcClient.getOurUser().getName(), dcClient.getToken());
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
	
	public void registerListeners() {
		registerListener(new UtilCommands());
		registerListener(new TriviaManager());
	}
	
	public void registerListener(Object listener) {
		dcClient.getDispatcher().registerListener(listener);
	}

}
