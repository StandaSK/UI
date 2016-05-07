package mains;

import java.util.*;
import java.util.stream.Collectors;

import customType.*;

public class Genetics {
	/* Inicializácia prvých count jedincov */
	public static List<StepSequence> initialize(int count) {
		List<StepSequence> individuals = new ArrayList<StepSequence>(count);
		
		for (int i = 0; i < count; i++)
			individuals.add(Evolution.generate());
		
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
			.limit((int) (MainFile.ELITARISM_RATE * size))
			.collect(Collectors.toList());
		
		/* Vygenerovanie nových (Main.NEW_INDIVIDUAL_RATE * size) náhodných jedincov */
		for(int i = 0; i < MainFile.NEW_INDIVIDUAL_RATE * size; i++)
			newGeneration.add(Evolution.generate());
		
		/* Mutovanie a kríženie starej generácie do novej */
		for (StepSequence ss : individuals) {
			if (newGeneration.size() == MainFile.INDIVIDUAL_COUNT) { break; }
			
			/* Mutovanie starej generácie do novej */
			if (MainFile.MUTATION_RATE > Math.random()) {
				newGeneration.add(mutate(ss));
			}
			
			if (newGeneration.size() == MainFile.INDIVIDUAL_COUNT) { break; }
			
			/* Kríženie starej generácie do novej */
			if (MainFile.CROSSOVER_RATE > Math.random()) {
				newGeneration.add(crossOver(individuals.get((int) (Math.random() * individuals.size())), ss));
			}
			
			if (newGeneration.size() == MainFile.INDIVIDUAL_COUNT) { break; }
		}
		
		return newGeneration;
	}
	
	/* Mutácia náhodného kroku jednica ss */
	public static StepSequence mutate(StepSequence ss) {
		int[] steps = ss.getSteps();
		/* Vygenerovanie náhodného èísla pre poradie kroku */
		int rndPos = (int) Math.floor(Math.random() * steps.length);
		/* Vygenerovanie náhodného kroku */
		int rndNum = (int) Math.floor(Math.random() * MainFile.MAX_VALUES);
		
		
		steps[rndPos] = rndNum;
		return new StepSequence(steps);
	}
	
	/* Kríženie dvoch jedincov do jedného */
	public static StepSequence crossOver(StepSequence ss1, StepSequence ss2) {
		int size;
		int ss1Length = ss1.getSteps().length;
		int ss2Length = ss2.getSteps().length;
		boolean ss1Longer = false;
		
		if (ss1Length > ss2Length) {
			size = ss1Length;
			ss1Longer = true;
		}
		else size = ss2Length;
		
		int[] newSteps = new int[size];
		
		if (ss1Longer) {
			for (int i = 0; i < ss2Length; i++) {
				if (i%2 == 0) {
					newSteps[i] = ss2.getSteps()[i];
				}
				else newSteps[i] = ss1.getSteps()[i];
			}
			for (int i = ss2Length; i < ss1Length; i++) {
				newSteps[i] = ss1.getSteps()[i];
			}
		}
		else {
			for (int i = 0; i < ss1Length; i++) {
				if (i%2 == 0) {
					newSteps[i] = ss2.getSteps()[i];
				}
				else newSteps[i] = ss1.getSteps()[i];
			}
			for (int i = ss1Length; i < ss2Length; i++) {
				newSteps[i] = ss2.getSteps()[i];
			}
		}
		
		return new StepSequence(newSteps);
	}
}