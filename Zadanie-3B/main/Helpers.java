package main;

public class Helpers {
	
	/**
	 * Zmeni ��slo na bin�rne - do Stringu
	 * @param number menen� ��slo
	 * @return bin�rne ��slo, reprezentovan� Stringom
	 */
	public static String getBytesOfInt(int number){
		return String.format("%8s", Integer.toBinaryString(number & 0xFF)).replace(' ', '0');
	}
	
	/**
	 * Zmen� bin�rne ��slo reprezentovan� Stringom sp� na integer
	 * @param val bin�rne ��slo, reprezentovan� Stringom
	 * @return zmenen� ��slo
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