package io.github.phantamanta44.celestia.util;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.phantamanta44.celestia.CTMain;
import sx.blah.discord.handle.obj.IUser;

public class MessageUtils {
	
	private static final Pattern namePtn = Pattern.compile("<@(\\d*)>");

	public static IUser parseName(String[] parts) {
		if (parts.length < 1)
			return null;
		Matcher m = namePtn.matcher(parts[1]);
		if (!m.find())
			return null;
		return CTMain.dcInstance.getUser(m.group(1));
	}
	
	public static String concat(String[] parts) {
		return Arrays.stream(parts).reduce((a, b) -> a.concat(" ").concat(b)).get();
	}
	
}
