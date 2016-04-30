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
		
		while (allSteps.contains(path)) {
			path = "";
			for (int i = 0; i < MainFile.STARTING_MEMORY_CELL_COUNT; i++) {
				steps[i] = (int) Math.floor(Math.random() * MainFile.MAX_VALUES);
				path += steps[i];
			}
		}
		sources.add(path);
		return new StepSequence(steps);
	}
	
	public void create(StepSequence ss, CustomVector mapSize) {
		int mapPositionCount = ((int) mapSize.getX()) * ((int) mapSize.getY());
		operationCounter = 0;
		newStepIndex = 0;
		this.newSteps = ss.getSteps();
		
		while (sb.length() < mapPositionCount && newStepIndex >= 0 && newStepIndex < MainFile.MEMORY_CELL_COUNT && operationCounter < MainFile.MAX_STEP_COUNT) {
			operate();
		}
		
		allSteps.add(sb.toString());
		ss.setPath(sb.toString());
	}
	
	private void operate() {
		operationCounter++;
		String operator = MainFile.getBytesOfInt(newSteps[newStepIndex]).substring(0, 2);
		int temp;
		
		switch (operator) {
			case "00":
				temp = MainFile.getIntValue(MainFile.getBytesOfInt(newSteps[newStepIndex++]).substring(2, 8));
				newSteps[temp] += 1;
				break;
			case "01":
				temp = MainFile.getIntValue(MainFile.getBytesOfInt(newSteps[newStepIndex++]).substring(2, 8));
				newSteps[temp] -= 1;
				break;
			case "10":
				temp = MainFile.getIntValue(MainFile.getBytesOfInt(newSteps[newStepIndex++]).substring(2, 8));
				newStepIndex = temp;
				break;
			case "11":
				temp = MainFile.getIntValue(MainFile.getBytesOfInt(newSteps[newStepIndex++]).substring(2, 8));
				sb.append(getStepAsChar(newSteps[temp]));
				break;
		}
	}
	
	/* Zo vstupu zistí kroky */
	private char getStepAsChar(int tmp){
		byte[] num = MainFile.getBytesOfInt(tmp).getBytes();
		int count = 0;
		for (int i = 0; i < num.length; i++)
			if (num[i] == 49)
				count++;
		
		if (count <= 2)
			return 'h';
		if (count <= 4)
			return 'd';
		if (count <= 6)
			return 'p';
		
		return 'l';
	}
	
	public static void output(List<StepSequence> list, int num) {
		int maxTreasures = 0;
		int minStep = -1;
		float maxFitness = 0;
		float fitnessSum = 0;
		float treasureSum = 0;
		float stepSum = 0;
		StepSequence best = null;
		for (StepSequence ss : list) {
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
		System.out.println("num: " + num + ", operacii: " + operationCounter + ", "
				+ best + ", paths/sources: " + allSteps.size() + "/" + sources.size()
				+ ", count: " + size + ", avgFit: " + fitnessSum / size + ", avgTrea: "
				+ treasureSum / size + ", avgSteps: " + stepSum / size);
	}
}