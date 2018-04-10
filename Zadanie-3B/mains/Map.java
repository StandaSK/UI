package mains;

import customType.*;
import java.util.*;

public class Map {
	private CustomVector size;
	private Set<CustomVector> treasureSet = new HashSet<CustomVector>();
	
	/* Konstruktor */
	public Map(CustomVector mapSize, CustomVector ... treasures) {
		this.setSize(mapSize);
		
		for (CustomVector t : treasures) {
			this.treasureSet.add(t);
		}
		
	}
	
	/* Najdenie pokladov */
	public void findTreasures(StepSequence ss, CustomVector position) {
		int foundTreasureCount = 0;
		int stepCount = 0;
		String path = ss.getPath();
		Set<CustomVector> treasures = new HashSet<CustomVector>(treasureSet);
		
		//System.out.println(path.isEmpty());
				
		while (!path.isEmpty()) {
			if (path.startsWith("l")) {
				/* Krok dolava */
				position.setX(position.getX() - 1);
			}
			else if (path.startsWith("p")) {
				/* Krok doprava */
				position.setX(position.getX() + 1);
			}
			else if (path.startsWith("h")) {
				/* Krok hore */
				position.setY(position.getY() + 1);
			}
			else if (path.startsWith("d")) {
				/* Krok dole */
				position.setY(position.getY() - 1);
			}
						
			/* Ak sa sucasny bod nenachadza v obdlzniku velkosti X+1, Y+1 */
			if (!position.isInRectangle(new CustomVector(0,0),
					new CustomVector(size.getX(), size.getY()))) {
				/* Oznaci sa tento jedinec ako prejdeny a hladanie skonci */
				//System.out.println("FTC: " + foundTreasureCount + "\tSC: " + stepCount);
				ss.track(foundTreasureCount, stepCount);
				return;
			}
			
			//System.out.println("Act Position: " + position + "\tStep Count: " + stepCount);
			
			/* Ak je sucasny bod jednym z pokladov */
			if (treasures.contains(position)) {
				foundTreasureCount++;
				treasures.remove(position);
				//System.out.println("Nasiel sa poklad!!!");
			}

			//System.out.println("FT: " + path);

			stepCount++;
			path = path.substring(1);
		}
		/* Tento jedinec sa oznaci ako prejdeny */
		//System.out.println("FTC: " + foundTreasureCount + "\tSC: " + stepCount + "   KASUHFJSHAKGFJASF");
		ss.track(foundTreasureCount, stepCount);
	}
	
	/* Gettery */
	public CustomVector getSize() { return size; }
	public Set<CustomVector> getTreasureSet() { return treasureSet; }
	/* Settery */
	public void setSize(CustomVector size) { this.size = size; }
	public void setTreasureSet(Set<CustomVector> treasureSet) { this.treasureSet = treasureSet; }
}
