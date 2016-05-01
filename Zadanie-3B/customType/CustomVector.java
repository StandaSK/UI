package customType;

public class CustomVector {
	private float x,y;
	
	/* Kon�truktor */
	public CustomVector(float x, float y) {
		this.setX(x);
		this.setY(y);
	}
	
	public String toString(){
		return x + "," + y;
	}
	
	/**
	 * Funkcia zist� �i sa bod nach�dza v obd�niku
	 * @param pos po�iato�n� poz�cia obd�nika
	 * @param size ve�kos� obd�nika
	 * @return TRUE ak sa v �om nach�dza, FALSE ak nie
	 */
	public boolean isInRectangle(CustomVector pos, CustomVector size) {
		//System.out.println(pos.x + "\t" + (pos.x + size.x) + "\t" + pos.y + "\t" +(pos.y + size.y));
		return (x >= pos.x) && (x < pos.x + size.x) && (y >= pos.y) && (y < pos.y + size.y);
	}
	
	/* Gettery */
	public float getX() { return x; }
	public float getY() { return y; }
	/* Settery */
	public void setX(float x) { this.x = x; }
	public void setY(float y) { this.y = y; }
}