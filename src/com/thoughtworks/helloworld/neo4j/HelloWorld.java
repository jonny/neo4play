package com.thoughtworks.helloworld.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.kernel.EmbeddedGraphDatabase;

public class HelloWorld {
	
	static enum RelTypes implements RelationshipType {
		KNOWS
	}

	private static final String DB_PATH = "target/db/";

	public static void main(String[] args) {
		GraphDatabaseService graphDb = new EmbeddedGraphDatabase(DB_PATH);
		NeoWrapper db = new NeoWrapper(graphDb);
		
		db.execute(new DbCommand() {
			
			@Override
			void execute(GraphDatabaseService graphDb) {
				Node firstNode = graphDb.createNode();
				Node secondNode = graphDb.createNode();
				 
				Relationship relationship = firstNode.createRelationshipTo(secondNode, RelTypes.KNOWS);
				 
				firstNode.setProperty("message", "Hello, ");
				secondNode.setProperty("message", "world!");
				relationship.setProperty("message", "brave Neo4j ");			
				
				System.out.print(firstNode.getProperty("message"));
				System.out.print(relationship.getProperty("message"));
				System.out.print(secondNode.getProperty("message"));
				
			}
		});
		
		db.cleanUp();

	}


}
