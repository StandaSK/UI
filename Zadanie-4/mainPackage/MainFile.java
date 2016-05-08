package mainPackage;

import java.io.*;
import java.util.*;

public class MainFile {
	public final static String FACTS_FILE_NAME = "fakty.txt";
	public final static String RULES_FILE_NAME = "pravidla.txt";
	public final static boolean DEBUG_INPUT = true;
	public final static boolean DEBUG_OUTPUT = true;
	
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
			
			while ((line = rulesReader.readLine()) != null) {
				if (line.startsWith("AK")) {
					rules.add(line.substring(3));
				}
				else if (line.startsWith("POTOM")) {
					rules.add(line.substring(6));
				}
		    }
			
			if (DEBUG_INPUT) System.out.println("\nVstupné pravidlá:\n" + rules);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		//LOGIKA PROGRAMU
		compareRuleFact(facts.get(0), rules.get(0));
		
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
	
	private static String compareRuleFact(String rule, String fact) {
		String[] ruleCopy = rule.replaceAll("[()]", "").split(" ");
		String[] factCopy = fact.replaceAll("[()]", "").split(" ");
		
		for (String str : factCopy) {
			
		}
		
		return null;
	}
	
	private static void appendFact(String fact) {
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
