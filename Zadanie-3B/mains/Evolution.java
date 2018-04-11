package mains;

import customType.*;
import java.util.*;

public class Evolution {
	private static Set<String> allSteps = new HashSet<String>();
	private static Set<String> sources = new HashSet<String>();
	private StringBuilder sb = new StringBuilder();
	private static int operationCounter = 0;
	private int newStepIndex;
	private int[] newSteps;
	
	/**
	 * Uvodna inicializacia prvych STARTING_MEMORY_CELL_COUNT genov jedinca
	 * @return Novo vygenerovany jedinec
	 */
	public static StepSequence generate() {
		int[] steps = new int[MainFile.MEMORY_CELL_COUNT];
		
		String path = "";
		
		/* Vygenerovanie novych krokov, while zabezpecuje aby sa neopakovali */
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
		//int fieldCount = (int) (mapSize.getX() * mapSize.getY());
		operationCounter = 0;
		newStepIndex = 0;
		this.newSteps = ss.getSteps();
		sb = new StringBuilder();
		
		while (/*sb.length() < fieldCount && */newStepIndex >= 0 && newStepIndex < MainFile.MEMORY_CELL_COUNT && operationCounter < MainFile.MAX_STEP_COUNT) {
			operate();
		}
		
		allSteps.add(sb.toString());
		ss.setPath(sb.toString());
	}
	
	private void operate() {
		operationCounter++;
		String operator = MainFile.getBytesOfInt(newSteps[newStepIndex]).substring(0, 2);
		int temp = MainFile.getIntValue(MainFile.getBytesOfInt(newSteps[newStepIndex++]).substring(2, 8));
		
		//System.out.println("Operate: " + operator + " " + temp);
		
		switch (operator) {
			case "00":
				/* Inkrementacia */
				newSteps[temp] += 1;
				break;
			case "01":
				/* Dekrementacia */
				newSteps[temp] -= 1;
				break;
			case "10":
				/* Skok */
				newStepIndex = temp;
				break;
			case "11":
				/* Vypis do StringBuilderu */
				sb.append(getStepAsChar(newSteps[temp]));
				break;
		}
	}
	
	/* Zo vstupu zisti kroky */
	public static char getStepAsChar(int tmp) {
		int mapSize = 49;
		byte[] num = MainFile.getBytesOfInt(tmp).getBytes();
		int count = 0;
		
		//System.out.println("GSAC: " + tmp);
		
		for (int i = 0; i < num.length; i++)
			if (num[i] == mapSize) {
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
		int minStep = -1;
		double maxFitness = 0;
		float fitnessSum = 0;
		float treasureSum = 0;
		float stepSum = 0;
		StepSequence best = list.get(0);
		
		for (StepSequence ss : list) {
			//System.out.println("Fitness: " + ss.getFitness());
			
			if (minStep < 0 || minStep > ss.getStepCount()) {
				//System.out.println(minStep + "/" + ss.getStepCount());
				minStep = ss.getStepCount();
				//System.out.println(minStep + "/" + ss.getStepCount());
			}
			
			if (maxFitness <= ss.getFitness()) {
				maxFitness = ss.getFitness();
				best = ss;
			}
			
			fitnessSum += ss.getFitness();
			treasureSum += ss.getTreasureCount();
			stepSum += ss.getStepCount();
		}
		
		int listSize = list.size();
		//System.out.println(generation);
		System.out.println(generation + ". Generation: Operation Counter: " + operationCounter + "\nBest: "
				+ best.toString() + "All Step Count: " + allSteps.size() + ", Sources Size: " + sources.size()
				+ ", List Size: " + listSize + ", Fitness Avg: " + fitnessSum/listSize
				+ ", Treasure Avg: " + treasureSum/listSize + ", Step Sum: " + stepSum);
	}
}