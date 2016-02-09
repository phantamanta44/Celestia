package io.github.phantamanta44.celestia.core.command;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import io.github.phantamanta44.celestia.CTMain;
import sx.blah.discord.handle.obj.IUser;

public class CommandBash implements ICommand {

	private static final List<String> ALIASES = Arrays.asList(new String[] {"qdb", "quote", "bashquote", "ircquote"});
	private static final String BASH_URL = "http://bash.org/?random";
		
	@Override
	public String getName() {
		return "bash";
	}

	@Override
	public List<String> getAliases() {
		return ALIASES;
	}

	@Override
	public String getDesc() {
		return "Retrieves a random quote from http://bash.org.";
	}

	@Override
	public String getUsage() {
		return "bash";
	}

	@Override
	public void execute(IUser sender, String[] args) {
		try {
			Document doc = Jsoup.connect(BASH_URL).get();
			Element qList = doc.getElementsByAttributeValue("valign", "top").get(0);
			String id = qList.child(0).child(0).child(0).text(), quote = qList.child(1).html().replaceAll("<br>", "\n");
			CTMain.dcInstance.sendMessage("**Quote %s**\n```%s```", id, StringEscapeUtils.unescapeHtml4(quote));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public boolean canUseCommand(IUser sender) {
		return true;
	}

	@Override
	public String getPermissionMessage(IUser sender) {
		throw new IllegalStateException();
	}

}
