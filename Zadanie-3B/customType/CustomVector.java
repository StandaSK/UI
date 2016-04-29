package customType;

public class CustomVector {
	private float x,y;
	
	/* Konštruktor */
	public CustomVector(float x, float y) {
		this.setX(x);
		this.setY(y);
	}
	
	/**
	 * Funkcia zistí èi sa bod nachádza v obdåžniku
	 * @param pos poèiatoèná pozícia obdåžnika
	 * @param size ve¾kos obdåžnika
	 * @return TRUE ak sa v òom nachádza, FALSE ak nie
	 */
	public boolean isInRectangle(CustomVector pos, CustomVector size) {
		return (x > pos.x) && (x < pos.x + size.x) && (y > pos.y) && (y < pos.y + size.y);
	}
	
	/* Gettery */
	public float getX() { return x; }
	public float getY() { return y; }
	/* Settery */
	public void setX(float x) { this.x = x; }
	public void setY(float y) { this.y = y; }
}