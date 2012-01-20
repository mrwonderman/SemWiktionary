package edu.unice.polytech.kis.semwiktionary.database;


import org.neo4j.graphdb.RelationshipType;

import edu.unice.polytech.kis.semwiktionary.model.*; // yes, they are (almost) all needed


public enum Relation implements RelationshipType {
	DEFINITION(Definition.class),
	SYNONYM(Word.class),
	ANTONYM(Word.class);
	
	public Class getDestinationType() {
		return this.maps;
	}

	private Class<? extends NodeMappedObject> maps;

	private Relation(Class<? extends NodeMappedObject> mapped) {
		this.maps = mapped;
	}
}
