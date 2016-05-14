package customType;

public class Rule {
	private String name;
	private String[] conditions;
	private String[] actions;
	
	public Rule() {
		this.setName(null);
		this.setConditions(null);
		this.setActions(null);
	}
	
	public Rule(String name, String[] conditions, String[] actions) {
		this.setName(name);
		this.setConditions(conditions);
		this.setActions(actions);
	}
	
	/* Gettery */
	public String getName() { return name; }
	public String[] getConditions() { return conditions; }
	public String[] getActions() { return actions; }
	/* Settery */
	public void setName(String name) { this.name = name; }
	public void setConditions(String[] conditions) { this.conditions = conditions; }
	public void setActions(String[] actions) { this.actions = actions; }
}
