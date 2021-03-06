package customType;

//import mains.MainFile;

public class StepSequence {
	private int treasureCount;
	private int stepCount;
	private int[] steps;
	
	private float fitness;
	
	private String path;
	
	private boolean tracked;
	private boolean executed;
	
	/* Konstruktor */
	public StepSequence(int[] steps) {
		this.setSteps(steps);
		//this.setStepCount(steps.length);
	}
	
	/* Sluzi na vypis atributov jedinca */
	public String toString() {
		return ("Steps: " + stepCount + '\n' +
				//MainFile.getBytesOfInt(steps[63]) + '\n' +
				"Treasures: " + treasureCount + '\n' +
				"Fitness: " + fitness + '\n');
	}
	
	/* Nastavi hodnoty a oznaci sa ako uz prejdeny */
	public void track(int treasureCount, int stepCount) {
		this.setTreasureCount(treasureCount);
		this.setStepCount(stepCount);
		this.setTracked(true);
		this.setFitness((float) treasureCount - ((float) stepCount / 1000));
		//System.out.println("Track:\t" + stepCount + "\t" + fitness);
	}
	
	/* Gettery */
	public int getTreasureCount() { return treasureCount; }
	public int getStepCount() { return stepCount; }
	public int[] getSteps() { return steps; }
	public float getFitness() { return fitness; }
	public String getPath() { return path; }
	public boolean isTracked() { return tracked; }
	public boolean isExecuted() { return executed; }
	/* Settery */
	public void setTreasureCount(int treasureCount) { this.treasureCount = treasureCount; }
	public void setStepCount(int stepCount) { this.stepCount = stepCount; }
	public void setSteps(int[] steps) { this.steps = steps; }
	public void setFitness(float fitness) { this.fitness = fitness; }
	public void setPath(String path) {
		this.path = path;
		this.setExecuted(true);
	}
	public void setTracked(boolean tracked) { this.tracked = tracked; }
	public void setExecuted(boolean executed) { this.executed = executed; }
}