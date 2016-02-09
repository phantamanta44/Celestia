package io.github.phantamanta44.celestia.event;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import sx.blah.discord.handle.obj.IUser;

public class ControlPanel {
	
	private static final Set<String> controllers = new HashSet<>();

	public ControlPanel() throws IOException {
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
