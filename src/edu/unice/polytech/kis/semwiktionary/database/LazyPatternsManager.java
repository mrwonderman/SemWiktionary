package edu.unice.polytech.kis.semwiktionary.database;


import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.Transaction;

import edu.unice.polytech.kis.semwiktionary.model.NodeMappedObject;


/** This class helps managing so-called “lazy patterns”.
* The Wiktionary uses many patterns, or "Modèles", to reuse common pieces of information, such as lexical categories.
* These are described in some entries, but no guarantee is made that the description entries will be _after_ others that might use them.
* Therefore, to avoid a two-pass parsing, or we use a _lazy_ management of such relations.
* The idea is as follows:
* - When a pattern is encountered in an entry, the `LazyPatternsManager` records it as an entry in an index of name `UNKNOWN`, with the key of the name of the pattern.
* - When the pattern description entry is encountered, a named node is created to store its information.
* - All nodes previously registered as having this pattern in the `UNKNOWN` index get a link added to the new pattern node.
* - All references indexed with the new pattern in the `UNKNOWN` index are removed.
*/
public class LazyPatternsManager {
	
	public static final String INDEX_KEY = "Unknown";
	
	private static Index<Node> index = Database.getIndexForName(INDEX_KEY);
	
	/** Registers the given NodeMappedObject as having the given pattern, without knowing which it is.
	*/
	public static void register(String pattern, NodeMappedObject element) {
		
		element.indexAsOn(pattern, INDEX_KEY); // we can't index on a key only, so the value we associate to the key is always "true"
	}

	/** Adds a relation from all nodes previously registered with the given pattern to the given destination, with the given relation type.
	*
	*@param	pattern	The pattern previously unknown.
	*@param	destination	The new NodeMappedObject to which previously-registered elements should be linked to.
	*@param	relType	How the new relations should be typed.
	*/
	public static void transferAll(String pattern, NodeMappedObject destination, Relation relType) {
		IndexHits<Node> hits = index.get(pattern, true);
		Node destinationNode = destination.getNode();
		
		Transaction tx = Database.getDbService().beginTx();

		try {
			for (Node currentNode : hits) {
				Database.link(currentNode, destinationNode, relType);
				index.remove(currentNode);
			}
		} finally {
		    tx.finish();

			hits.close();
		}
	}
}
