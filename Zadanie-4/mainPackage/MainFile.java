package mainPackage;

import java.io.*;
import java.util.*;

public class MainFile {
	public final static String FACTS_FILE_NAME = "fakty.txt";
	public final static String RULES_FILE_NAME = "pravidla.txt";
	public final static boolean DEBUG_INPUT = false;
	public final static boolean DEBUG_OUTPUT = false;
	
	private static List<String> facts = new ArrayList<String>();
	private static List<String> rules = new ArrayList<String>();
	//private static List<String> actions = new ArrayList<String>();
	
	public static void main(String[] args) {
		
		/* Načítanie faktov */
		try (BufferedReader factsReader = new BufferedReader(new FileReader(FACTS_FILE_NAME))) {
			String line = null;
			
			while ((line = factsReader.readLine()) != null) {
				facts.add(line);
		    }
			
			if (DEBUG_INPUT) System.out.println("Vstupné fakty:\n" + facts);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
				
		/* Načítanie pravidiel */
		try (BufferedReader rulesReader = new BufferedReader(new FileReader(RULES_FILE_NAME))) {
			String line = null;
			String temp = null;
			String[] tempArray;
			boolean correctRule = false;
			
			while ((line = rulesReader.readLine()) != null) {
				if (line.startsWith("AK")) {
					temp = line.substring(3);
					temp = temp.substring(1, temp.length() - 1);
					
					tempArray = temp.split("[)]");
					
					for (int i = 0; i < tempArray.length; i++) {
						tempArray[i] = tempArray[i].substring(1);
						/* Kontrola splnenia podmienky */
						correctRule = isRuleCorrect(tempArray[i]);
					}
					
					if (DEBUG_INPUT) System.out.println("Podmienka: " + temp);
					rules.add(temp);
				}
				else if (line.startsWith("POTOM")) {
					temp = line.substring(6);
					temp = temp.substring(1, temp.length() - 1);
					
					/* Vytvorenie noveho faktu ak je splnena podmienka */
					if (correctRule) createFact(temp, getVariables(rules.get(rules.size())));
					
					if (DEBUG_INPUT) System.out.println("Akcia: " + temp);
					rules.add(temp);
				}
		    }
			
			if (DEBUG_INPUT) System.out.println("\nVstupné pravidlá:\n" + rules);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		/* Prepísanie starého súboru s faktami */
		try (PrintWriter out = new PrintWriter(new FileOutputStream(FACTS_FILE_NAME, false))) {
			for (String str: facts) {
				out.write(str + System.lineSeparator());
			}
		} catch (FileNotFoundException fnf) {
			fnf.printStackTrace();
		}
		
		if (DEBUG_OUTPUT) {
			System.out.println("\nVýstupné fakty:");
			facts.forEach(System.out::println);
			System.out.println("\nVýstupné pravidlá:");
			rules.forEach(System.out::println);
		}
	}
	
	/*private static String resolveRulesFacts() {
		/*String[] ruleCopy = rule.replaceAll("[()]", "").split(" ");
		String[] factCopy = fact.replaceAll("[()]", "").split(" ");
		String[] tempArray = null;
		
		for (int i = 0; i < rules.size(); i++) {
			tempArray = rules.get(i).split("[)]");
			for (int j = 0; j < tempArray.length; j++) {
				tempArray[j] += ")";
				System.out.println(tempArray[j]);
			}
			System.out.println();
		}
		
		return null;
	}*/
	
	/* Vrati arraylist premennych v pravidle */
	private static List<String> getVariables(String rule) {
		String[] arrayRule = rule.replaceAll("[()]", " ").split(" ");
		List<String> result = new ArrayList<String>();
		
		for (int i = 0; i < arrayRule.length; i++) {
			if (arrayRule[i].contains("?")) {
				result.add(arrayRule[i]);
			}
		}
		return result;
	}
	
	/* Porovna podmienku condition s faktom fact a vrati arraylist vo formate ?PREMENNA = HODNOTA */
	private static List<String> findCondition(String condition, String fact) {
		String[] arrayCondition = condition.replaceAll("[()]", "").split(" ");
		String[] arrayFact = fact.replaceAll("[()]", "").split(" ");
		List<String> result = new ArrayList<String>();
		
		
		if (arrayCondition.length != arrayFact.length)
			return null;
			
		for (int i = 0; i < arrayCondition.length; i++) {
			if (arrayCondition[i] != arrayFact[i]) {
				if (arrayCondition[i].contains("?")) {
					result.add(arrayFact[i] + " = " + arrayCondition[i]);
				}
				else return null;
			}
		}
		return result;
	}
	
	/* Zisti ci je pravidlo spravne */
	private static boolean isRuleCorrect(String rule) {
		List<String> temp = null;
		
		for (int i = 0; i < facts.size(); i++) {
			temp = findCondition(rule, facts.get(i));
			if (temp != null) {
				return true;
			}
		}
		return false;
	}
	
	/* Z akcie vytvori fakt */
	private static void createFact(String action, List<String> var) {
		String[] actionCopy = action.replaceAll("[()]", "").split(" ");
		
		for (int i = 0; i < actionCopy.length; i++) {
			if (actionCopy[i].contains("pridaj"))
				addFact("(" + replaceVariables(action.replace("pridaj ", ""), var) + ")");
			
			if (actionCopy[i].contains("vymaz"))
				deleteFact("(" + replaceVariables(action.replace("vymaz ", ""), var) + ")");
			
			if (actionCopy[i].contains("sprava"))
				message("(" + replaceVariables(action.replace("sprava ", ""), var) + ")");
		}
	}
	
	/* V akcii vymeni premenne za prislusne hodnoty */
	private static String replaceVariables(String action, List<String> var) {
		String result = action;
		
		for (int i = 0; i < var.size(); i++) {
			String[] help = var.get(i).split(" ");
			String temp = help[0];
			String temp2 = help[2];
			
			for (int j = 3; j < help.length; j++) {
				temp2 += " " + help[j];
			}
			
			if (result.contains(temp))
				result = result.replace(temp, temp2);
		}
		return result;
	}
	
	/* Pridanie faktu */
	private static void addFact(String fact) {
		if (!facts.contains(fact))
			facts.add(fact);
	}
	
	/* Odstranenie faktu */
	private static boolean deleteFact(String fact) {
		if (facts.contains(fact))
			for (int i = 0; i < facts.size(); i++)
				if (facts.get(i) == fact) {
					facts.remove(i);
					return true;
				}
		return false;
	}
	
	/* Sprava */
	private static void message(String fact) {
		System.out.println(fact);
	}
}
