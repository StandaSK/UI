package main;

import java.util.*;

public class Main {
	public final static int MEMORY_CELL_COUNT = 64;
	public final static int MAX_STEP_COUNT = 500;
	public final static int INDIVIDUAL_COUNT = 20;
	public final static int GENERATION_COUNT = 200;
	public final static int TREASURE_COUNT = 7;
	
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
		}
	}
}