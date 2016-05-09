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
	private static List<String> actions = new ArrayList<String>();
	
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
			//String[] tempArray;
			
			while ((line = rulesReader.readLine()) != null) {
				if (line.startsWith("AK")) {
					temp = line.substring(3);
					temp = temp.substring(1, temp.length() - 1);
					
					/*tempArray = temp.split("[)]");
					
					for (int i = 0; i < tempArray.length; i++) {
						tempArray[i] = tempArray[i].substring(1);
						//System.out.println(tempArray[i]);
					}*/
					
					if (DEBUG_INPUT) System.out.println("Podmienka: " + temp);
					rules.add(temp);
				}
				else if (line.startsWith("POTOM")) {
					temp = line.substring(6);
					temp = temp.substring(1, temp.length() - 1);
					
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
		
		//LOGIKA PROGRAMU
		resolveRulesFacts();
		
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
	
	private static String resolveRulesFacts() {
		/*String[] ruleCopy = rule.replaceAll("[()]", "").split(" ");
		String[] factCopy = fact.replaceAll("[()]", "").split(" ");*/
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
	}
	
	private static String findCondition(String condition) {
		String[] arrayCondition = condition.split(" ");
		String[] arrayFact = null;
		int conditionLength = arrayCondition[0].length();
		int factLength = 0;
		
		for (int i = 0; i < facts.size(); i++) {
			arrayFact = facts.get(i).split(" ");
			factLength = arrayFact.length;
			
			if (conditionLength != factLength) {
				continue;
			}
			
			
		}
		
		return null;
	}
	
	private static void addFact(String fact) {
		if (!facts.contains(fact))
			facts.add(fact);
	}
	
	private static boolean deleteFact(String fact) {
		if (facts.contains(fact)) {
			for (int i = 0; i < facts.size(); i++)
				if (facts.get(i) == fact) facts.remove(i);
			return true;
		}
		return false;
	}
	
	private static void message(String fact) {
		System.out.println(fact);
	}
}
