package io.github.phantamanta44.celestia.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public class ChanceList<T> {

	private final List<T> chanceSet = new ArrayList<>();
	
	public ChanceList(T... outcomes) {
		chanceSet.addAll(Arrays.asList(outcomes));
	}
	
	public void addOutcome(T outcome) {
		chanceSet.add(outcome);
	}
	
	public T getAtRandom(Random rand) {
		return chanceSet.get(rand.nextInt(chanceSet.size()));
	}
	
	public Stream<T> stream() {
		return this.chanceSet.stream();
	}
	
}