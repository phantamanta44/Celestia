package io.github.phantamanta44.celestia.module.scripting.command;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;

import io.github.phantamanta44.celestia.CTMain;
import io.github.phantamanta44.celestia.core.ICommand;
import io.github.phantamanta44.celestia.module.scripting.host.HostObjectChannel;
import io.github.phantamanta44.celestia.module.scripting.host.HostObjectDiscordAPI;
import io.github.phantamanta44.celestia.module.scripting.host.HostObjectGuild;
import io.github.phantamanta44.celestia.module.scripting.host.HostObjectMessage;
import io.github.phantamanta44.celestia.module.scripting.host.HostObjectRole;
import io.github.phantamanta44.celestia.module.scripting.host.HostObjectUser;
import io.github.phantamanta44.celestia.util.MessageUtils;
import sx.blah.discord.handle.obj.IUser;

public class CommandEval implements ICommand {
	
	private static final Pattern lambdaRegex = Pattern.compile("\\|(\\w+):\\(?(\\w+(?:,\\s*\\w+)*)\\)?\\s*->\\s*\\{?(.*?)\\}?\\|");
	private static final Map<String, String> lambdaAbbrev = new HashMap<>();
	
	static {
		lambdaAbbrev.put("c", "Consumer");
		lambdaAbbrev.put("bc", "BiConsumer");
		lambdaAbbrev.put("f", "Function");
		lambdaAbbrev.put("bf", "BiFunction");
		lambdaAbbrev.put("p", "Predicate");
		lambdaAbbrev.put("bp", "BiPredicate");
		lambdaAbbrev.put("s", "Supplier");
		lambdaAbbrev.put("uo", "UnaryOperator");
		lambdaAbbrev.put("bo", "BinaryOperator");
	}

	@Override
	public String getName() {
		return "eval";
	}

	@Override
	public List<String> getAliases() {
		return Collections.emptyList();
	}

	@Override
	public String getDesc() {
		return "Evaluate some JS.";
	}

	@Override
	public String getUsage() {
		return "eval <script>";
	}

	@Override
	public void execute(IUser sender, String[] args) {
		if (args.length < 1) {
			CTMain.dcInstance.sendMessage("You must provide a script!");
			return;
		}
		String script = MessageUtils.concat(args);
		if (script.startsWith("```") && script.endsWith("```"))
			script = script.substring(3, script.length() - 3);
		else if (script.startsWith("`") && script.endsWith("`"))
			script = script.substring(1, script.length() - 1);
		Matcher mat = lambdaRegex.matcher(script);
		StringBuffer replBuffer = new StringBuffer();
		while (mat.find())
			mat.appendReplacement(replBuffer, parseLambda(mat.group()));
		mat.appendTail(replBuffer);
		script = replBuffer.toString();
		CTMain.logger.info(replBuffer.toString());
		
		Context con = Context.enter();
		try {
			Scriptable scope = con.initSafeStandardObjects();
			defineObjects(scope, sender);
			Object rtObj = con.evaluateString(scope, script, "<eval>", 1, null);
			Object apiHost = scope.get("api", scope);
			if (apiHost != Scriptable.NOT_FOUND && apiHost instanceof HostObjectDiscordAPI)
				((HostObjectDiscordAPI)apiHost).flushBuffer();
			String rtVal = Context.toString(rtObj);
			if (!(rtObj instanceof Undefined) && !rtVal.isEmpty())
				CTMain.dcInstance.sendMessage(rtVal);
		} catch (RhinoException ex) {
			CTMain.dcInstance.sendMessage("```%s\n%s```", ex.getMessage(), Arrays.stream(ex.getScriptStack(8, null))
					.reduce("", (a, b) -> a.toString().concat("\n").concat(b.toString()), (a, b) -> a.concat(b)));
		} catch (Exception ex) {
			StackTraceElement ste = ex.getStackTrace()[0];
			CTMain.dcInstance.sendMessage("`%s` thrown while executing script (at `%s:%s`)", ex.getClass().getName(), ste.getClassName(), ste.getLineNumber());
			ex.printStackTrace();
		} finally {
			Context.exit();
		}
	}

	private static void defineObjects(Scriptable scope, IUser me) throws Exception {
		ScriptableObject.defineClass(scope, HostObjectChannel.class);
		ScriptableObject.defineClass(scope, HostObjectDiscordAPI.class);
		ScriptableObject.defineClass(scope, HostObjectGuild.class);
		ScriptableObject.defineClass(scope, HostObjectMessage.class);
		ScriptableObject.defineClass(scope, HostObjectRole.class);
		ScriptableObject.defineClass(scope, HostObjectUser.class);
		ScriptableObject.putProperty(scope, "me", HostObjectUser.impl(me, scope));
		ScriptableObject.putProperty(scope, "bot", HostObjectUser.impl(CTMain.dcInstance.getBotUser(), scope));
		ScriptableObject.putProperty(scope, "channel", HostObjectChannel.impl(CTMain.dcInstance.getChannel(), scope));
		ScriptableObject.putProperty(scope, "guild", HostObjectGuild.impl(CTMain.dcInstance.getServer(), scope));
		ScriptableObject.putProperty(scope, "api", Context.getCurrentContext().newObject(scope, "DiscordAPI"));
	}
	
	private static String parseLambda(String exp) {
		try {
			Matcher mat = lambdaRegex.matcher(exp);
			if (!mat.matches())
				return exp;
			String cName = expandLambda(mat.group(1)), args = mat.group(2), body = mat.group(3);
			Class<?> funcInt = Class.forName("java.util.function." + cName);
			if (!funcInt.isAnnotationPresent(FunctionalInterface.class) || !funcInt.isInterface())
				return exp;
			Method funcMtd = Arrays.stream(funcInt.getMethods())
					.filter(m -> !m.isDefault() && !Modifier.isStatic(m.getModifiers()))
					.findFirst().get();
			if (funcMtd.getReturnType() != Void.TYPE && !body.contains("return"))
				body = "return " + body;
			return String.format("new Object({%s: function(%s) {%s}})", funcMtd.getName(), args, body);
		} catch (Exception ex) {
			return exp;
		}
	}

	private static String expandLambda(String cName) {
		if (!lambdaAbbrev.containsKey(cName))
			return cName;
		return lambdaAbbrev.get(cName);
	}

	@Override
	public boolean canUseCommand(IUser sender) {
		return CTMain.isAdmin(sender);
	}

	@Override
	public String getPermissionMessage(IUser sender) {
		return "No permission!";
	}

	@Override
	public String getEnglishInvokation() {
		return "";
	}

}
