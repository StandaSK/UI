package customType;

public class CustomVector {
	private float x,y;
	
	/* Konštruktor */
	public CustomVector(float x, float y) {
		this.setX(x);
		this.setY(y);
	}
	
	public String toString(){
		return x + "," + y;
	}
	
	@Override
	public int hashCode() {
	    final int prime = 31;
	    float result = 1;
	    result = prime * result + x;
	    result = prime * result + y;
	    return (int) result;
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj)
	        return true;
	    if (obj == null)
	        return false;
	    if (getClass() != obj.getClass())
	        return false;
	    CustomVector other = (CustomVector) obj;
	    if (x != other.x)
	        return false;
	    if (y != other.y)
	        return false;
	    return true;
	}
	
	/**
	 * Funkcia zistí èi sa bod nachádza v obdåžniku
	 * @param pos poèiatoèná pozícia obdåžnika
	 * @param size ve¾kos obdåžnika
	 * @return TRUE ak sa v òom nachádza, FALSE ak nie
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