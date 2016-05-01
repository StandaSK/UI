package mains;

import java.util.*;
import java.util.stream.Collectors;

import customType.*;

public class Genetics {
	/* Inicializ�cia prv�ch count jedincov */
	public static List<StepSequence> initialize(int count) {
		List<StepSequence> individuals = new ArrayList<StepSequence>(count);
		
		for (int i = 0; i < count; i++)
			individuals.add(Evolution.generate());
		
		return individuals;
	}
	
	/**
	 * Zo starej gener�cie individuals vytvor� nov�
	 * @param individuals star� gener�cia
	 * @return nov� gener�cia
	 */
	public static List<StepSequence> createNewGeneration(List<StepSequence> individuals) {
		int size = individuals.size();
		List<StepSequence> newGeneration = new ArrayList<StepSequence>(size);
		
		/* V�ber (Main.ELITARISM_LEVEL * size) najlep��ch jedincov */
		newGeneration = individuals.stream().sorted((a, b) -> {
			if (a.getFitness() > b.getFitness()) return -1;
			else if (a.getFitness() < b.getFitness()) return 1;
			return 0; })
			.limit((int) (MainFile.ELITARISM_RATE * size))
			.collect(Collectors.toList());
		
		/* Vygenerovanie nov�ch (Main.NEW_INDIVIDUAL_RATE * size) n�hodn�ch jedincov */
		for(int i = 0; i < MainFile.NEW_INDIVIDUAL_RATE * size; i++)
			newGeneration.add(Evolution.generate());
		
		/* Mutovanie starej gener�cie do novej */
		for (StepSequence ss : individuals) {
			if (newGeneration.size() == MainFile.INDIVIDUAL_COUNT) { break; }
			
			if (MainFile.MUTATION_RATE > Math.random()) {
				newGeneration.add(mutate(ss));
			}
		}
		
		return newGeneration;
	}
	
	/* Mut�cia n�hodn�ho kroku jednica ss */
	public static StepSequence mutate(StepSequence ss) {
		int[] steps = ss.getSteps();
		/* Vygenerovanie n�hodn�ho ��sla pre poradie kroku */
		int rndPos = (int) Math.floor(Math.random() * steps.length);
		/* Vygenerovanie n�hodn�ho kroku */
		int rndNum = (int) Math.floor(Math.random() * MainFile.MAX_VALUES);
		
		
		steps[rndPos] = rndNum;
		return new StepSequence(steps);
	}
}