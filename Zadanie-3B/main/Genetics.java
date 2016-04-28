package main;

import java.util.*;

public class Genetics {
	/* Inicializácia prvých jedincov */
	public static List<StepSequence> initialize(int count) {
		List<StepSequence> individuals = new ArrayList<StepSequence>(count);
		
		while (count > 0) {
			individuals.add(null);
			count--;
		}
		
		return individuals;
	}
	
	/* Mutácia náhodného kroku jednica ss */
	public static StepSequence mutate(StepSequence ss) {
		int[] steps = ss.getSteps();
		/* Vygenerovanie nahodneho cisla pre poradie kroku */
		int rndPos = (int) Math.floor(Math.random() * steps.length);
		/* Vygenerovanie nahodneho kroku */
		int rndNum = (int) Math.floor(Math.random() * 255);
		
		
		steps[rndPos] = rndNum;
		return new StepSequence(steps);
	}
}