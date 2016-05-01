package mains;

import java.util.*;

import customType.*;

public class Map {
	private CustomVector size;
	private Set<CustomVector> treasureSet = new HashSet<CustomVector>();
	
	/* Kon�truktor */
	public Map(CustomVector mapSize, CustomVector ... treasures) {
		this.setSize(mapSize);
		
		for (CustomVector t : treasures) {
			this.treasureSet.add(t);
		}
		
	}
	
	/* N�jdenie pokladov */
	public void findTreasures(StepSequence ss, CustomVector position) {
		int foundTreasureCount = 0;
		int stepCount = 0;
		String path = ss.getPath();
		Set<CustomVector> treasures = new HashSet<CustomVector>(treasureSet);
		
		//System.out.println(path.isEmpty());
				
		while (!path.isEmpty()) {
			if (path.startsWith("l")) {
				/* Krok do�ava */
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
						
			/* Ak sa s��asn� bod nenach�dza v ob�niku ve�kosti X+1, Y+1 */
			if (!position.isInRectangle(new CustomVector(0,0),
					new CustomVector(size.getX(), size.getY()))) {
				/* Ozna�� sa tento jedinec ako prejden� a h�adanie skon�� */
				//System.out.println("FTC: " + foundTreasureCount + "\tSC: " + stepCount);
				ss.track(foundTreasureCount, stepCount);
				return;
			}
			
			//System.out.println("Act Position: " + position + "\tStep Count: " + stepCount);
			
			/* Ak je s��asn� bod jedn�m z pokladov */
			if (treasures.contains(position)) {
				foundTreasureCount++;
				treasures.remove(position);
				System.out.println("Nasiel sa poklad!!!");
			}

			//System.out.println("FT: " + path);

			stepCount++;
			path = path.substring(1);
		}
		/* Tento jedinec sa ozna�� ako prejden� */
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
