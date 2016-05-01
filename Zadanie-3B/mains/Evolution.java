package mains;

import java.util.*;

import customType.*;

public class Evolution {
	private static Set<String> allSteps = new HashSet<String>();
	private static Set<String> sources = new HashSet<String>();
	private StringBuilder sb = new StringBuilder();
	private static int operationCounter = 0;
	private int newStepIndex;
	private int[] newSteps;
	
	/**
	 * Úvodná inicializácia prvých STARTING_MEMORY_CELL_COUNT krokov jedinca
	 * @return Novo vygenerovaný jedinec
	 */
	public static StepSequence generate() {
		int[] steps = new int[MainFile.MEMORY_CELL_COUNT];
		
		String path = "";
		
		/* Vygenerovanie nových krokov, while zabezpeèuje aby sa neopakovali */
		while (sources.contains(path)) {
			path = "";
			for (int i = 0; i < MainFile.STARTING_MEMORY_CELL_COUNT; i++) {
				steps[i] = (int) Math.floor(Math.random() * MainFile.MAX_VALUES);
				path += steps[i];
			}
		}
		sources.add(path);
		return new StepSequence(steps);
	}
	
	public void execute(StepSequence ss, CustomVector mapSize) {
		operationCounter = 0;
		newStepIndex = 0;
		this.newSteps = ss.getSteps();
		
		while (newStepIndex >= 0 && newStepIndex < MainFile.MEMORY_CELL_COUNT && operationCounter < MainFile.MAX_STEP_COUNT) {
			operate();
		}
		
		allSteps.add(sb.toString());
		ss.setPath(sb.toString());
	}
	
	private void operate() {
		operationCounter++;
		String operator = MainFile.getBytesOfInt(newSteps[newStepIndex]).substring(0, 2);
		int temp = MainFile.getIntValue(MainFile.getBytesOfInt(newSteps[newStepIndex++]).substring(2, 8));
		
		//System.out.println("Operate: " + operator);
		
		switch (operator) {
			case "00":
				/* Inkrementácia */
				newSteps[temp] += 1;
				break;
			case "01":
				/* Dekrementácia */
				newSteps[temp] -= 1;
				break;
			case "10":
				/* Skok */
				newStepIndex = temp;
				break;
			case "11":
				/* Výpis do StringBuilderu */
				sb.append(getStepAsChar(newSteps[temp]));
				break;
		}
	}
	
	/* Zo vstupu zistí kroky */
	private char getStepAsChar(int tmp) {
		byte[] num = MainFile.getBytesOfInt(tmp).getBytes();
		int count = 0;
		
		//System.out.println("GSAC: " + tmp);
		
		for (int i = 0; i < num.length; i++)
			if (num[i] == 49) {
				count++;
				//System.out.println(count);
			}
		
		if (count <= 2)
			return 'h';
		if (count <= 4)
			return 'd';
		if (count <= 6)
			return 'p';
		
		return 'l';
	}
	
	public static void output(List<StepSequence> list, int generation) {
		int maxTreasures = 0;
		int minStep = -1;
		float maxFitness = 0;
		float fitnessSum = 0;
		float treasureSum = 0;
		float stepSum = 0;
		StepSequence best = null;
		
		for (StepSequence ss : list) {
			//System.out.println("Fitness: " + ss.getFitness());
			
			if (ss.getTreasureCount() > maxTreasures)
				maxTreasures = ss.getTreasureCount();
			
			if (minStep < 0 || minStep > ss.getStepCount()) {
				//System.out.println(minStep + "/" + ss.getStepCount());
				minStep = ss.getStepCount();
				//System.out.println(minStep + "/" + ss.getStepCount());
			}
			
			if (maxFitness < ss.getFitness()) {
				maxFitness = ss.getFitness();
				best = ss;
			}
			
			fitnessSum += ss.getFitness();
			treasureSum += ss.getTreasureCount();
			stepSum += ss.getStepCount();
		}
		float size = list.size();
		System.out.println(generation + ".  OpCounter: " + operationCounter + "   "
				+ best + ", paths/sources: " + allSteps.size() + "/" + sources.size()
				+ ", count: " + size + ", avgFit: " + fitnessSum / size + ", avgTrea: "
				+ treasureSum / size + ", avgSteps: " + stepSum / size);
	}
}