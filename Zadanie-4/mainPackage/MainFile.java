package mainPackage;

import customType.Rule;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainFile {
	public final static String FACTS_FILE_NAME = "fakty.txt";
	public final static String RULES_FILE_NAME = "pravidla.txt";
	public final static boolean DEBUG_INPUT = false;
	public final static boolean DEBUG_CYCLE = false;
	public final static boolean DEBUG_FILTER = false;
	public final static boolean DEBUG_RECURSION = false;
	public final static boolean DEBUG_REPLACE = false;
	
	private static List<String> facts = new ArrayList<String>();
	private static List<Rule> rules = new ArrayList<Rule>();
	private static List<String[]> aplicableRules = new ArrayList<String[]>();
	
	public static void main(String[] args) {
		
		/* Nacitanie faktov */
		try (BufferedReader factsReader = new BufferedReader(new FileReader(FACTS_FILE_NAME))) {
			String line = null;
			
			while ((line = factsReader.readLine()) != null) {
				facts.add(line);
		    }
			
			if (DEBUG_INPUT) { System.out.println("Vstupne fakty:\n" + facts); }
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		/* Nacitanie pravidiel */
		try (BufferedReader rulesReader = new BufferedReader(new FileReader(RULES_FILE_NAME))) {
			String line = null;
			String[] tempArray = null;
			
			while ((line = rulesReader.readLine()) != null) {
				Rule rule = new Rule();
				
				/* Nacitanie nazvu pravidla */
				rule.setName(line);
				if (DEBUG_INPUT) { System.out.println("Pravidlo: " + line); }
				
				/* Nacitanie podmienok pravidla */
				line = rulesReader.readLine();
				line = line.replace("AK ", "");
				tempArray = line.replace("((" , "").replace("))" , "").split("\\)\\(");
				rule.setConditions(tempArray);
				if (DEBUG_INPUT) { System.out.println("Podmienky: " + line); }
				
				/* Nacitanie akcii pravidla */
				line = rulesReader.readLine();
				line = line.replace("POTOM ", "");
				tempArray = line.replace("((" , "").replace("))" , "").split("\\)\\(");
				rule.setActions(tempArray);
				if (DEBUG_INPUT) { System.out.println("Akcie: " + line); }
				
				/* Prejdenie prazdneho riadku medzi pravidlami */
				line = rulesReader.readLine();
				if (DEBUG_INPUT) { System.out.println(); }
				
				rules.add(rule);
			}
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		while (true) {
			/* Hladanie aplikovatelnych instancii pravidiel - akcii */ 
			for (Rule r : rules) {
				recursion(new HashMap<String, String>(), r, 0);
			}
			
			if (DEBUG_CYCLE) {
				System.out.println("Neodfiltrovane pravidla: ");
				for (String[] s : aplicableRules) {
					System.out.println(Arrays.deepToString(s));
				}
			}
			
			/* Filtrovanie aplikovatelnych instancii pravidiel */ 
			filterAplicableRules();
			
			if (DEBUG_CYCLE) {
				System.out.println("Odfiltrovane pravidla: ");
				for (String[] s : aplicableRules) {
					System.out.println(Arrays.deepToString(s));
				}
			}
			
			/* Vyber prvej aplikovatelnej instancie a jej vykonanie*/
			if (!aplicableRules.isEmpty()) {
				createFact(aplicableRules.get(0));
				aplicableRules.remove(0);
			}
			
			/* Ak uz ziadne aplikovatelne instancie neexistuju */
			else break;
		}
		
		/* Prepisanie stareho suboru s faktami */
		try (PrintWriter out = new PrintWriter(new FileOutputStream(FACTS_FILE_NAME, false))) {
			for (String str: facts) {
				out.write(str + System.lineSeparator());
			}
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
	}
	
	/* Filtrovanie aplikovatelnych instancii pravidiel */
	private static void filterAplicableRules() {
		int size = aplicableRules.size();
		
		for (int i = 0; i < size; i++) {
			while ((!aplicableRules.isEmpty()) && (i < aplicableRules.size()) && (!doesChangeAnything(aplicableRules.get(i)))) {
				if (DEBUG_FILTER) {
					System.out.println("i: " + i + " SIZE: " + aplicableRules.size());
				}
				
				aplicableRules.remove(i);
			}
			
			size = aplicableRules.size();
		}
	}
	
	/* Zisti ci akcie actions nieco menia vo faktoch */
	private static boolean doesChangeAnything(String[] actions) {
		boolean temp = false;
		
		for (String f : actions) {
			if (f.contains("pridaj")) { temp = !facts.contains(f.replace("pridaj ", "")); }
			
			if (f.contains("vymaz")) { temp = facts.contains(f.replace("vymaz ", "")); }
			
			if (temp) { return true; }
		}
		
		return false;
	}
	
	private static void recursion(Map<String, String> localMap, Rule rule, int num) {
		String currentCondition = rule.getConditions()[num];
		
		/* Ak uz je na poslednej urovni rekurzie */
		if (num == rule.getConditions().length - 1) {
			if (DEBUG_RECURSION) {
				System.out.println("Konecna uroven rekurzie");
			}
			
			if (currentCondition.contains("<>")) {
				String[] tempArray = currentCondition.split(" ");
				
				if (isDifferent(tempArray[1], tempArray[2], localMap)) {
					aplicableRules.add(replaceVariables(rule.getActions(), localMap));
				}
				
				return;
			}
			
			for (String f : facts) {
				if (isConditionCorrect(replaceVariables(currentCondition, localMap), f)) {
					List<String> var = findHashValue(currentCondition, f);
					List<String> keep = new ArrayList<String>();
					
					if (DEBUG_RECURSION) {
						System.out.println("Pred pridanimK: " + localMap);
					}
					
					/* Pridanie premennych do hash mapy */
					for (String s : var) {
						String[] temp = s.split(" = ");
						String tempKeep = localMap.put(temp[0], temp[1]);
						
						if (tempKeep != null) {
							keep.add(temp[0]);
						}
					}
					
					if (DEBUG_RECURSION) {
						System.out.println("PridaneK: " + localMap);
					}
					
					/* Pridanie aplikovatelnych instancii */
					aplicableRules.add(replaceVariables(rule.getActions(), localMap));
					
					/* Odstranenie premennych z hash mapy */
					for (String s : var) {
						String[] temp = s.split(" = ");
						
						if (!keep.contains(temp[0])) {
							localMap.remove(temp[0]);
						}
					}
					
					if (DEBUG_RECURSION) {
						System.out.println("OdobraneK: " + localMap);
					}
				}
			}
		}
		/* Ak este nie je na poslednej urovni rekurzie */
		else {
			if (currentCondition.contains("<>")) {
				String[] tempArray = currentCondition.split(" ");
				
				if (isDifferent(tempArray[1], tempArray[2], localMap)) {
					recursion(localMap, rule, num + 1);
				}
			}
			else {
				for (String f : facts) {
					if (isConditionCorrect(replaceVariables(currentCondition, localMap), f)) {
						List<String> var = findHashValue(currentCondition, f);
						List<String> keep = new ArrayList<String>();
						
						if (DEBUG_RECURSION) {
							System.out.println("Pred pridanim: " + localMap);
						}
						
						/* Pridanie premennych do hash mapy */
						for (String s : var) {
							String[] temp = s.split(" = ");
							String tempKeep = localMap.put(temp[0], temp[1]);
							
							if (tempKeep != null) {
								keep.add(temp[0]);
							}
						}
						
						if (DEBUG_RECURSION) {
							System.out.println("Pridane: " + localMap);
						}
						
						recursion(localMap, rule, num + 1);
						
						/* Odstranenie premennych z hash mapy */
						for (String s : var) {
							String[] temp = s.split(" = ");
							
							if (!keep.contains(temp[0])) {
								localMap.remove(temp[0]);
							}
						}
						
						if (DEBUG_RECURSION) {
							System.out.println("Odobrane: " + localMap);
						}
					}
				}
			}
		}
	}
	
	/* Porovna podmienku condition s faktom fact a vrati arraylist vo formate ?PREMENNA = HODNOTA */
	private static List<String> findHashValue(String condition, String fact) {
		String[] arrayCondition = condition.replaceAll("[()]", "").split(" ");
		String[] arrayFact = fact.replaceAll("[()]", "").split(" ");
		List<String> result = new ArrayList<String>();
		
		if (arrayCondition.length != arrayFact.length) { return null; }
		
		for (int i = 0; i < arrayCondition.length; i++) {
			if (!arrayCondition[i].equals(arrayFact[i])) {
				if (arrayCondition[i].contains("?")) {
					result.add(arrayCondition[i] + " = " + arrayFact[i]);
				}
				else { return null; }
			}
		}
		
		return result;
	}
	
	/* V akcii vymeni premenne za ich prislusne hodnoty */
	private static String replaceVariables(String action, Map<String, String> localMap) {
		String[] tempArray = action.replaceAll("[()]", "").split(" ");
		String temp = action;
		
		for (String s : tempArray) {
			if (s.contains("?") && (localMap.containsKey(s))) {
				temp = temp.replace(s, localMap.get(s));
			}
		}
		
		return temp;
	}
	
	/* V akciach vymeni premenne za ich prislusne hodnoty */
	private static String[] replaceVariables(String[] actions, Map<String, String> localMap) {
		String[] temp = actions.clone();
		
		 if (DEBUG_REPLACE) {
			 System.out.println("REPLACE: " + Arrays.deepToString(temp) +
					 " X: " + localMap.get("?X") +
					 " Y: " + localMap.get("?Y") +
					 " Z: " + localMap.get("?Z"));
		 }
		 
		
		for (int i = 0; i < temp.length; i++) {
			String a = temp[i];
			String[] tempArray = a.replaceAll("[()]", "").split(" ");
			
			for (String s : tempArray)
				if (s.contains("?") && (localMap.containsKey(s))) {
					if (DEBUG_REPLACE) { System.out.println("Pred replace: " + temp[i]); }
					
					temp[i] = temp[i].replace(s, localMap.get(s));
					
					if (DEBUG_REPLACE) { System.out.println("Po replace: " + temp[i]); }
				}
		}
		
		return temp;
	}
	
	/* Z akcie vytvori fakt */
	private static void createFact(String action) {
		if (action.contains("pridaj")) { addFact(action.replace("pridaj ", "")); }
		
		if (action.contains("vymaz")) { deleteFact(action.replace("vymaz ", "")); }
		
		if (action.contains("sprava")) { message(action.replace("sprava ", "")); }
	}
	
	/* Z pola akcii vytvori fakty */
	private static void createFact(String[] actions) {
		for (String s : actions) {
			createFact(s);
		}
	}
	
	/* Zisti ci je podmienka platna a ak ano tak vlozi do hashmapy hodnoty k premennym */
	private static boolean isConditionCorrect(String condition, String fact) {
		/* Ak uz su premenne doplnene a podmienka sa zhoduje s faktom */
		if (condition.equals(fact)) { return true; }
		
		List<String> var = findHashValue(condition, fact);
		
		if (var != null) { return true; }
		
		return false;
	}
	
	/* Porovna ci su mena A a B odlisne - funkcia <> */
	private static boolean isDifferent(String A, String B, Map<String, String> localMap) {
		if (localMap.get(A).equals(localMap.get(B))) { return false; }
		
		return true;
	}
	
	/* Pridanie faktu */
	private static boolean addFact(String fact) {
		if (!facts.contains(fact)) {
			facts.add(fact);
			return true;
		}
		
		return false;
	}
	
	/* Odstranenie faktu */
	private static boolean deleteFact(String fact) {
		if (facts.contains(fact)) {
			for (int i = 0; i < facts.size(); i++) {
				if (facts.get(i).equals(fact)) {
					facts.remove(i);
					return true;
				}
			}
		}
		
		return false;
	}
	
	/* Sprava */
	private static void message(String fact) {
		System.out.println("Message: " + fact);
	}
}
