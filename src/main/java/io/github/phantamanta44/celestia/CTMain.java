package io.github.phantamanta44.celestia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import io.github.phantamanta44.celestia.module.ModuleManager;
import io.github.phantamanta44.celestia.module.chat.ChatModule;
import io.github.phantamanta44.celestia.module.core.CoreModule;
import io.github.phantamanta44.celestia.module.random.RandomModule;
import io.github.phantamanta44.celestia.module.scripting.ScriptingModule;
import sx.blah.discord.handle.obj.IUser;

public class CTMain {
	
	public static final Discord dcInstance = new Discord();
	public static final LogWrapper logger = new LogWrapper("CT");
	public static final CTConfig config = new CTConfig("celestia.conf");
	
	private static final Set<String> controllers = new HashSet<>();
	
	public static void main(String[] args) {
		try {
			config.read();
			getAdmins();
			dcInstance.initApi(config.get("email"), config.get("pass"));
			dcInstance.registerListeners();
			registerModules();
			dcInstance.login();
		} catch (Exception e) {
			logger.severe("Something went wrong!");
			e.printStackTrace();
		}
	}
	
	private static void registerModules() {
		new CoreModule().onEnable();
		ModuleManager.registerModule(new RandomModule(), confIsTrue("mod.random"));
		ModuleManager.registerModule(new ChatModule(), confIsTrue("mod.chat"));
		ModuleManager.registerModule(new ScriptingModule(), confIsTrue("mod.scripting"));
	}
	
	private static boolean confIsTrue(String key) {
		String val = config.get(key);
		return val == null ? false : val.equalsIgnoreCase("true");
	}
	
	private static void getAdmins() throws IOException {
		BufferedReader strIn = new BufferedReader(new FileReader(new File("admins.txt")));
		String line;
		while ((line = strIn.readLine()) != null)
			controllers.add(line);
		strIn.close();
	}
	
	public static boolean isAdmin(IUser user) {
		return controllers.contains(user.getName());
	}

}