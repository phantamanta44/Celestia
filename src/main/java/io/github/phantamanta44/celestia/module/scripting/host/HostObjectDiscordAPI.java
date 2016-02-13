package io.github.phantamanta44.celestia.module.scripting.host;

import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSFunction;

import io.github.phantamanta44.celestia.CTMain;

public class HostObjectDiscordAPI extends ScriptableObject {

	private static final long serialVersionUID = 1L;
	private List<String> msgBuffer = new ArrayList<>();
	
	@Override
	public String getClassName() {
		return "DiscordAPI";
	}
	
	@JSFunction
	public void print(Object obj) {
		msgBuffer.add(Context.toString(obj));
	}
	
	@JSFunction
	public boolean isActive(Object obj) {
		if (obj instanceof HostObjectChannel)
			return CTMain.dcInstance.isChannel(((HostObjectChannel)obj).getDataSrc());
		if (obj instanceof HostObjectGuild)
			return CTMain.dcInstance.isServer(((HostObjectGuild)obj).getDataSrc());
		if (obj instanceof HostObjectMessage)
			return CTMain.dcInstance.isChannel(((HostObjectMessage)obj).getDataSrc());
		return false;
	}
	
	public void flushBuffer() {
		if (msgBuffer.isEmpty())
			return;
		String toSend = msgBuffer.stream()
				.reduce((a, b) -> a.concat("\n").concat(b)).orElse("");
		if (toSend.isEmpty())
			return;
		CTMain.dcInstance.sendMessage(toSend);
	}
	
}
