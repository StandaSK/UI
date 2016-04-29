package customType;

public class CustomVector {
	private float x,y;
	
	/* Kon�truktor */
	public CustomVector(float x, float y) {
		this.setX(x);
		this.setY(y);
	}
	
	/**
	 * Funkcia zist� �i sa bod nach�dza v obd�niku
	 * @param pos po�iato�n� poz�cia obd�nika
	 * @param size ve�kos� obd�nika
	 * @return TRUE ak sa v �om nach�dza, FALSE ak nie
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