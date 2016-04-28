package main;

import java.util.*;

public class Genetics {
	/* Inicializ�cia prv�ch jedincov */
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
	
	/* Mut�cia n�hodn�ho kroku jednica ss */
	public static StepSequence mutate(StepSequence ss) {
		int[] steps = ss.getSteps();
		/* Vygenerovanie n�hodn�ho ��sla pre poradie kroku */
		int rndPos = (int) Math.floor(Math.random() * steps.length);
		/* Vygenerovanie n�hodn�ho kroku */
		int rndNum = (int) Math.floor(Math.random() * Main.MAX_VALUES);
		
		
		steps[rndPos] = rndNum;
		return new StepSequence(steps);
	}
}