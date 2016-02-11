package io.github.phantamanta44.celestia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

public class CTConfig {

	private final File configFile;
	private final Map<String, String> configKeys = new HashMap<>();
	
	public CTConfig(String filename) {
		this(new File(filename));
	}
	
	public CTConfig(File file) {
		configFile = file;
	}
	
	public void read() throws Exception {
		configKeys.clear();
		BufferedReader reader = new BufferedReader(new FileReader(configFile));
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("#"))
				continue;
			String[] parts = line.split("=", 2);
			if (parts.length < 2) {
				CTMain.logger.warn("Invalid config line:\n\t%s", line);
				continue;
			}
			configKeys.put(parts[0], parts[1]);
		}
		reader.close();
	}
	
	public String get(String key) {
		return configKeys.get(key);
	}
	
	public void set(String key, String value) {
		configKeys.put(key, value);
	}
	
	public Stream<Entry<String, String>> stream() {
		return configKeys.entrySet().stream();
	}
	
}
