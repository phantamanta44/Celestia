package io.github.phantamanta44.celestia;

public class CTMain {
	
	public static final Discord dcInstance = new Discord();
	public static final LogWrapper logger = new LogWrapper("CT");
	public static final CTConfig config = new CTConfig("celestia.conf");
	
	public static void main(String[] args) {
		try {
			config.read();
			dcInstance.initApi(config.get("email"), config.get("pass"));
			dcInstance.registerListeners();
			dcInstance.login();
		} catch (Exception e) {
			logger.severe("Something went wrong!");
			e.printStackTrace();
		}
	}

}