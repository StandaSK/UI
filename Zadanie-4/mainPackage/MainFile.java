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
			boolean correctRule = true;
			
			while ((line = rulesReader.readLine()) != null) {
				if (line.startsWith("AK")) {
					temp = line.substring(3);
					temp = temp.substring(1, temp.length() - 1);
					
					tempArray = temp.split("[)]");
					
					for (int i = 0; i < tempArray.length; i++) {
						tempArray[i] = tempArray[i].substring(1);
						/* Kontrola splnenia podmienky */
						correctRule &= isRuleCorrect(tempArray[i]);
					}
					
					if (DEBUG_INPUT) System.out.println("Podmienka: " + temp);
					rules.add(temp);
				}
				else if (line.startsWith("POTOM")) {
					temp = line.substring(6);
					temp = temp.substring(1, temp.length() - 1);
					
					/* Vytvorenie noveho faktu ak je splnena podmienka */
					if (correctRule) {
						//System.out.println("CORRECT RULE: " + temp + getVariables(rules.get(rules.size() - 1)));
						createFact(temp, getVariables(rules.get(rules.size() - 1)));
					}
					
					if (DEBUG_INPUT) System.out.println("Akcia: " + temp);
					rules.add(temp);
				}
				correctRule = true;
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
		
		
		if (arrayCondition.length != arrayFact.length) {
			//System.out.println("Nesedi dlzka!");
			return null;
		}
		
		for (int i = 0; i < arrayCondition.length; i++) {
			if (arrayCondition[i].equals(arrayFact[i])) {
				if (arrayCondition[i].contains("?")) {
					result.add(arrayFact[i] + " = " + arrayCondition[i]);
				}
				else {
					//System.out.println("Nesedia premenne!");
					return null;
				}
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
		//System.out.println("action: " + action);
		
		for (int i = 0; i < actionCopy.length; i++) {
			if (actionCopy[i].contains("pridaj"))
				//System.out.println("PRIDAJ: " + replaceVariables(action.replace("pridaj ", ""), var));
				addFact(replaceVariables(action.replace("pridaj ", ""), var));
			
			if (actionCopy[i].contains("vymaz"))
				//System.out.println("VYMAZ: " + replaceVariables(action.replace("pridaj ", ""), var));
				deleteFact(replaceVariables(action.replace("vymaz ", ""), var));
			
			if (actionCopy[i].contains("sprava"))
				//System.out.println("SPRAVA: " + replaceVariables(action.replace("pridaj ", ""), var));
				message(replaceVariables(action.replace("sprava ", ""), var));
		}
	}
	
	/* V akcii vymeni premenne za prislusne hodnoty */
	private static String replaceVariables(String action, List<String> var) {
		String result = action;
		
		for (int i = 0; i < var.size(); i++) {
			String[] help = var.toString().replaceAll("]", "").replace("[", "").replace(",", "").split(" ");
			
			for (int j = 0; j < help.length; j++) {
				
			}
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
		System.out.println("Message: " + fact);
	}
}
