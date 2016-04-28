package main;

public class Helpers {
	
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