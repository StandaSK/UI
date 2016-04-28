package main;

import java.util.*;

public class Genetics {
	/* Inicializácia prvých jedincov */
	public static List<StepSequence> initialize(int count) {
		List<StepSequence> individuals = new ArrayList<StepSequence>(count);
		
		while (count > 0) {
			individuals.add(Evolution.genereate());
			count--;
		}
		
		return individuals;
	}
	
	public static List<StepSequence> createNewGeneration(List<StepSequence> individuals) {
		
		for (StepSequence ss : individuals) {
			System.out.printf(ss.toString());
		}
		
		return null;
	}
	
	/* Mutácia náhodného kroku jednica ss */
	public static StepSequence mutate(StepSequence ss) {
		int[] steps = ss.getSteps();
		/* Vygenerovanie náhodného èísla pre poradie kroku */
		int rndPos = (int) Math.floor(Math.random() * steps.length);
		/* Vygenerovanie náhodného kroku */
		int rndNum = (int) Math.floor(Math.random() * Main.MAX_VALUES);
		
		
		steps[rndPos] = rndNum;
		return new StepSequence(steps);
	}
}