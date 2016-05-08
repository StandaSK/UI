package mainPackage;

import java.io.*;

/* Try with resources
 * http://stackoverflow.com/questions/4716503/reading-a-plain-text-file-in-java
 */

public class MainFile {
	public final static String FACTS_FILE_NAME = "fakty.txt";
	public final static String RULES_FILE_NAME = "pravidla.txt";
	
	private static String facts = "";
	private static String rules = "";
	
	public static void main(String[] args) {
		
		/* Naèítanie faktov */
		try (BufferedReader factsReader = new BufferedReader(new FileReader(FACTS_FILE_NAME))) {
			StringBuilder factsBuilder = new StringBuilder();
			String line = null;
			
			while ((line = factsReader.readLine()) != null) {
		        factsBuilder.append(line);
		        factsBuilder.append(System.lineSeparator());
		        line = factsReader.readLine();
		    }
			
		    facts = factsBuilder.toString();
			System.out.println("Fakty:\n" + facts);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
				
		/* Naèítanie pravidiel */
		try (BufferedReader rulesReader = new BufferedReader(new FileReader(FACTS_FILE_NAME))) {
			StringBuilder rulesBuilder = new StringBuilder();
			String line = null;
			
			while ((line = rulesReader.readLine()) != null) {
				rulesBuilder.append(line);
				rulesBuilder.append(System.lineSeparator());
		        line = rulesReader.readLine();
		    }
			
			rules = rulesBuilder.toString();
			System.out.println("Pravidlá:\n" + rules);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		
	}
	
	private static void appendFact(String fact) {
		if (!facts.contains(fact)) {
			StringBuilder sb = new StringBuilder();
			sb.append(facts + System.lineSeparator() + fact);
			facts = sb.toString();
		}
	}
	
	private static boolean deleteFact(String fact) {
		if (facts.contains(fact)) {
			facts.replace(fact, "");
			return true;
		}
		else return false;
	}
	
	private static void message(String fact) {
		System.out.println(fact);
	}
}