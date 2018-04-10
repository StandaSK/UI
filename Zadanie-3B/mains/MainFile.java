package mains;

import customType.*;
import java.util.*;

public class MainFile {
	public final static int MEMORY_CELL_COUNT = 64;
	public final static int STARTING_MEMORY_CELL_COUNT = 32;
	public final static int MAX_STEP_COUNT = 500;
	public final static int INDIVIDUAL_COUNT = 200;
	public final static int GENERATION_COUNT = 2000;
	public final static int TREASURE_COUNT = 5;
	public final static int MAX_VALUES = 255;
	public final static double ELITARISM_RATE = 0.15;
	public final static double NEW_INDIVIDUAL_RATE = 0.4;
	public final static double MUTATION_RATE = 0.5;
	public final static double CROSSOVER_RATE = 0.5;
	
	public static void main(String[] args) {
		CustomVector mapSize = new CustomVector(7,7);
		CustomVector startingPosition = new CustomVector(0,3);
		Map map = new Map(mapSize, new CustomVector(1,2),
			new CustomVector(2,4), new CustomVector(4,1),
			new CustomVector(4,5), new CustomVector(6,3));
		Evolution evolution = new Evolution();
		
		/* Vytvorenie uvodnej generacie */
		List<StepSequence> individuals = Genetics.initialize(INDIVIDUAL_COUNT);
		
		/* Tvorenie nasledujucich generacii */
		for (int j = 0; j < GENERATION_COUNT; j++) {
			for (StepSequence i : individuals) {
				/* Ak este nebol spracovany */
				if (i.isExecuted() == false) {
					evolution.execute(i, mapSize);
				}
				/* Ak este nehladal poklady */
				if (i.isTracked() == false) {
					map.findTreasures(i, new CustomVector(startingPosition.getX(), startingPosition.getY()));
				}
			}
			
			Evolution.output(individuals, j);
			
			/* Vytvorenie novej generacie jednotlivcov */
			individuals = Genetics.createNewGeneration(individuals);
		}
	}
	
	/**
	 * Zmeni cislo na binarne - do Stringu
	 * @param number menene cislo
	 * @return binarne cislo, reprezentovane Stringom
	 */
	public static String getBytesOfInt(int number) {
		return String.format("%8s", Integer.toBinaryString(number & 0xFF)).replace(' ', '0');
	}
	
	/**
	 * Zmeni binarne cislo reprezentovane Stringom spat na integer
	 * @param val binarne cislo, reprezentovane Stringom
	 * @return zmenene cislo
	 */
	public static int getIntValue(String val) {
		int result = 0;
		int num = val.length();
		
		for (int i = 0; i < num; i++)
			if (val.charAt(i) == '1')
				result += Math.pow(2, num - i - 1);
		
		return result;
	}
}