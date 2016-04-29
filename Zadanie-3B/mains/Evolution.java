package mains;

import java.util.*;

import customType.*;

public class Evolution {
	private static Set<String> allSteps = new HashSet<String>();
	StringBuilder sb = new StringBuilder();
	private int operationCounter;
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
		allSteps.add(path);
		return new StepSequence(steps);
	}
	
	public void create(StepSequence ss) {
		operationCounter = 0;
		newStepIndex = 0;
		
		while (sb.length() < 49 && newStepIndex < MainFile.MEMORY_CELL_COUNT && operationCounter < MainFile.MAX_STEP_COUNT) {
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
				sb.append(getLetter(newSteps[temp]));
				break;
		}
	}
	
	/* Zo vstupu zisti kroky */
	private char getLetter(int tmp){
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
}