package io.github.phantamanta44.celestia.module.core.command;

import java.lang.management.ManagementFactory;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import io.github.phantamanta44.celestia.CTMain;
import io.github.phantamanta44.celestia.core.ICommand;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.Lambdas;

public class CommandInfo implements ICommand {

	private static final List<String> ALIASES = Arrays.asList(new String[] {"uptime", "about", "stats"});
	
	@Override
	public String getName() {
		return "info";
	}

	@Override
	public List<String> getAliases() {
		return ALIASES;
	}

	@Override
	public String getDesc() {
		return "Gets information about the bot.";
	}

	@Override
	public String getUsage() {
		return "info";
	}

	@Override
	public void execute(IUser sender, String[] args) {
		List<Entry<String, Object>> info = new ArrayList<>();
		info.add(new SimpleEntry<>("Uptime", getUptime()));
		info.add(new SimpleEntry<>("Servers", CTMain.dcInstance.getClient().getGuilds().size()));
		info.add(new SimpleEntry<>("Channels", CTMain.dcInstance.getClient().getChannels(true).stream()
				.distinct()
				.count()));
		info.add(new SimpleEntry<>("Users", CTMain.dcInstance.getClient().getGuilds().stream()
				.map(c -> c.getUsers())
				.reduce(Lambdas.listReduction()).get().stream()
				.distinct()
				.count()));
		Runtime rt = Runtime.getRuntime();
		info.add(new SimpleEntry<>("Used Mem", String.format("%.2f/%.2fMB", (rt.totalMemory() - rt.freeMemory()) / 1000000F, rt.totalMemory() / 1000000F)));
		String infoStr = info.stream()
				.map(e -> e.getKey().concat(": ").concat(String.valueOf(e.getValue())))
				.reduce((a, b) -> a.concat("\n").concat(b)).get();
		CTMain.dcInstance.sendMessage("**Bot Information:**\n```%s```\nSource code available at https://github.com/phantamanta44/Celestia", infoStr);
	}

	@Override
	public boolean canUseCommand(IUser sender) {
		return true;
	}

	@Override
	public String getPermissionMessage(IUser sender) {
		throw new IllegalStateException();
	}
	
	private static String getUptime() {
		long millis = ManagementFactory.getRuntimeMXBean().getUptime();
		
		long days = TimeUnit.MILLISECONDS.toDays(millis);
		millis -= TimeUnit.DAYS.toMillis(days);
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		millis -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

		return String.format("%s Days, %s Hours, %s Minutes, %s Seconds", days, hours, minutes, seconds);
	}
	
	@Override
	public String getEnglishInvokation() {
		return "";
	}

}
