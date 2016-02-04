package io.github.phantamanta44.celestia;

import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LogWrapper {

	private final Logger logger;
	
	public LogWrapper(String name) {
		logger = LogManager.getLogManager().getLogger(name);
	}
	
	public void info(String msg) {
		logger.info(msg);
	}
	
	public void info(String format, Object... args) {
		logger.info(String.format(format, args));
	}
	
	public void warn(String msg) {
		logger.warning(msg);
	}
	
	public void warn(String format, Object... args) {
		logger.warning(String.format(format, args));
	}
	
	public void severe(String msg) {
		logger.severe(msg);
	}
	
	public void severe(String format, Object... args) {
		logger.severe(String.format(format, args));
	}
	
}
