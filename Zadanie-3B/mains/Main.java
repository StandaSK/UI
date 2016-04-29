package mains;

import java.util.*;

public class Main {
	public final static int MEMORY_CELL_COUNT = 64;
	public final static int STARTING_MEMORY_CELL_COUNT = 16;
	public final static int MAX_STEP_COUNT = 500;
	public final static int INDIVIDUAL_COUNT = 20;
	public final static int GENERATION_COUNT = 200;
	public final static int TREASURE_COUNT = 7;
	public final static int MAX_VALUES = 255;
	public final static double ELITARISM_RATE = 0.1;
	public final static double NEW_INDIVIDUAL_RATE = 0.3;
	public final static double MUTATION_RATE = 0.6;
	
	public static void main() {
		CustomVector mapSize = new CustomVector(7,7);
		CustomVector startingPosition = new CustomVector(0,3);
		Map map = new Map(mapSize, new CustomVector(1,2),
			new CustomVector(2,4), new CustomVector(4,1),
			new CustomVector(4,5), new CustomVector(6,3));
		Evolution evolution = new Evolution();
		
		List<StepSequence> individuals = Genetics.initialize(INDIVIDUAL_COUNT);
		
		for (int j = 0; j < GENERATION_COUNT; j++) {
			for (StepSequence i : individuals) {
				if (i.isExists() == false) {
					evolution.create(i);
				}
				if (i.isTracked() == false) {
					map.findTreasures(i, new CustomVector(startingPosition.getX(), startingPosition.getY()));
				}
			}
			
			/* Vytvorenie novej generácie jednotlivcov */
			individuals = Genetics.createNewGeneration(individuals);
		}
	}
	
	/**
	 * Zmeni èíslo na binárne - do Stringu
	 * @param number menené èíslo
	 * @return binárne èíslo, reprezentované Stringom
	 */
	public static String getBytesOfInt(int number){
		return String.format("%8s", Integer.toBinaryString(number & 0xFF)).replace(' ', '0');
	}
	
	/**
	 * Zmení binárne èíslo reprezentované Stringom spä na integer
	 * @param val binárne èíslo, reprezentované Stringom
	 * @return zmenené èíslo
	 */
	public static int getIntValue(String val){
		int result = 0;
		int num = val.length();
		
		for (int i = 0; i < num; i++)
			if (val.charAt(i) == '1')
				result += Math.pow(2, num - i - 1);
		
		return result;
	}
}