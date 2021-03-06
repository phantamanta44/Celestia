package io.github.phantamanta44.celestia.module.random.event;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import io.github.phantamanta44.celestia.CTMain;
import io.github.phantamanta44.celestia.core.ICTListener;
import io.github.phantamanta44.celestia.util.ChanceList;
import io.github.phantamanta44.celestia.util.MessageUtils;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;

public class DuelManager implements ICTListener {

	private static final ChanceList<String> words = new ChanceList<>();
	
	private static Timer taskTimer = new Timer();
	private static DuelState state = DuelState.INACTIVE;
	private static IUser fighterA, fighterB;
	private static String target;
	private static TimerTask cancelTask;
	
	public DuelManager() {
		try {
			BufferedReader strIn = new BufferedReader(new FileReader(new File("duel.txt")));
			String line;
			while ((line = strIn.readLine()) != null)
				words.addOutcome(line);
			strIn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@ListenTo
	public void onMessageReceived(MessageReceivedEvent event) {
		if (!CTMain.dcInstance.isChannel(event.getMessage()))
			return;
		String msg = event.getMessage().getContent();
		IUser auth = event.getMessage().getAuthor();
		if (state == DuelState.ACTIVE && (auth.equals(fighterA) || auth.equals(fighterB))) {
			if (msg.equalsIgnoreCase(target)) {
				CTMain.dcInstance.sendMessage("**%s wins the 1v1!**", auth.mention());
				state = DuelState.INACTIVE;
				cancelTask.cancel();
			}
		}
	}
	
	public static void procCmd(IUser sender, String[] args) {
		if (state != DuelState.INACTIVE) {
			CTMain.dcInstance.sendMessage("There is already a 1v1 in progress!");
			return;
		}
		if (args.length < 1) {
			CTMain.dcInstance.sendMessage("You need to specify someone to 1v1!");
			return;
		}
		fighterB = MessageUtils.parseName(args, 0);
		if (fighterB == null) {
			CTMain.dcInstance.sendMessage("Nobody to 1v1!");
			return;
		}
		fighterA = sender;
		state = DuelState.PREPARING;
		CTMain.dcInstance.sendMessage("**%s and %s\u2014Get ready to battle!**", fighterA.mention(), fighterB.mention());
		Random rand = new Random();
		taskTimer.schedule(new FightTask(words.getAtRandom(rand)), 3000L + (long)(rand.nextFloat() * 5000F));
	}

	public static class FightTask extends TimerTask {

		private String word;
		
		public FightTask(String word) {
			this.word = word;
		}
		
		@Override
		public void run() {
			state = DuelState.ACTIVE;
			target = word;
			CTMain.dcInstance.sendMessage("**You have 10 seconds to type \"%s\"!**", target);
			cancelTask = new FightResetTask();
			taskTimer.schedule(cancelTask, 10000L);
		}
		
	}
	
	public static class FightResetTask extends TimerTask {

		@Override
		public void run() {
			state = DuelState.INACTIVE;
			CTMain.dcInstance.sendMessage("**1v1 cancelled.**");
		}
		
	}
	
	public static enum DuelState {
		
		INACTIVE, PREPARING, ACTIVE;
		
	}
	
}
