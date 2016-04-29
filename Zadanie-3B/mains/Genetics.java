package mains;

import java.util.*;
import java.util.stream.Collectors;

public class Genetics {
	/* Inicializácia prvých count jedincov */
	public static List<StepSequence> initialize(int count) {
		List<StepSequence> individuals = new ArrayList<StepSequence>(count);
		
		while (count > 0) {
			individuals.add(Evolution.generate());
			count--;
		}
		
		return individuals;
	}
	
	/**
	 * Zo starej generácie individuals vytvorí novú
	 * @param individuals stará generácia
	 * @return nová generácia
	 */
	public static List<StepSequence> createNewGeneration(List<StepSequence> individuals) {
		int size = individuals.size();
		List<StepSequence> newGeneration = new ArrayList<StepSequence>(size);
		
		/* Výber (Main.ELITARISM_LEVEL * size) najlepších jedincov */
		newGeneration = individuals.stream().sorted((a, b) -> {
			if (a.getFitness() > b.getFitness()) return -1;
			else if (a.getFitness() < b.getFitness()) return 1;
			return 0; })
			.limit((int) (Main.ELITARISM_RATE * size))
			.collect(Collectors.toList());
		
		/* Vygenerovanie nových (Main.NEW_INDIVIDUAL_RATE * size) náhodných jedincov */
		for(int i = 0; i < Main.NEW_INDIVIDUAL_RATE * size; i++)
			newGeneration.add(Evolution.generate());
		
		/* Mutovanie starej generácie do novej */
		for (StepSequence ss : individuals) {
			if (newGeneration.size() == Main.INDIVIDUAL_COUNT) { break; }
			
			if (Main.MUTATION_RATE > Math.random()) {
				newGeneration.add(mutate(ss));
			}
		}
		
		return newGeneration;
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