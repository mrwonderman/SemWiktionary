package edu.unice.polytech.kis.semwiktionary.model;

// For the moment, this class is a mockup.
// A definition is represented by a simple string.
public class Definition {

	private String definition;
	
	public Definition(String definition) {
		this.definition = definition;
	}

	public String getDefinition() {
		return definition;
	}
	
	public String toString() {
		return definition;
	}
	
	public boolean equals(Object o) {
		if (this.getClass().isInstance(o))
			return this.definition.equals(((Definition) o).getDefinition());
		
		if (String.class.isInstance(o))
			return this.definition.equals(o);
		
		return false;
	}
	
}
