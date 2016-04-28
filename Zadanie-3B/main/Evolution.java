package main;

import java.util.*;

public class Evolution {
	private static Set<String> sources = new HashSet<String>();
	
	/**
	 * Úvodná inicializácia prvých STARTING_MEMORY_CELL_COUNT krokov jedinca
	 * @return Novo vygenerovaný jedinec
	 */
	public static StepSequence genereate() {
		int[] steps = new int[Main.MEMORY_CELL_COUNT];
		
		String path = "";
		
		while (sources.contains(path)) {
			path = "";
			for (int i = 0; i < Main.STARTING_MEMORY_CELL_COUNT; i++) {
				steps[i] = (int) Math.floor(Math.random() * Main.MAX_VALUES);
				path += steps[i];
			}
		}
		sources.add(path);
		return new StepSequence(steps);
	}
	
	public void create(StepSequence ss) {
		ss.setPath(null);
	}
	
	public static void analyze(List<StepSequence> ss, int num) {
		
	}
}