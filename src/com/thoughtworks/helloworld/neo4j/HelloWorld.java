package com.thoughtworks.helloworld.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;

public class HelloWorld {
	
	static enum RelTypes implements RelationshipType {
		KNOWS
	}

	private static final String DB_PATH = "../db/";

	public static void main(String[] args) {
		GraphDatabaseService graphDb = new EmbeddedGraphDatabase(DB_PATH);
		registerShutdownHook(graphDb);
		
		Transaction tx = graphDb.beginTx();
		try {
			Node firstNode = graphDb.createNode();
			Node secondNode = graphDb.createNode();
			 
			Relationship relationship = firstNode.createRelationshipTo(secondNode, RelTypes.KNOWS);
			 
			firstNode.setProperty("message", "Hello, ");
			secondNode.setProperty("message", "world!");
			relationship.setProperty("message", "brave Neo4j ");			
			
			System.out.print(firstNode.getProperty("message"));
			System.out.print(relationship.getProperty("message"));
			System.out.print(secondNode.getProperty("message"));
			
			for (Node node : graphDb.getAllNodes()) {
			    for (Relationship rel : node.getRelationships()) {
			        rel.delete();
			    }
			    node.delete();
			}

			tx.success();
			
		} finally {
		    tx.finish();
		}
		
		

	}
		
	
	private static void registerShutdownHook(final GraphDatabaseService graphDb) {
	    // Registers a shutdown hook for the Neo4j instance so that it
	    // shuts down nicely when the VM exits (even if you "Ctrl-C" the
	    // running example before it's completed)
	    Runtime.getRuntime().addShutdownHook(new Thread() {
	        @Override
	        public void run() {
	            graphDb.shutdown();
	        }
	    });
	}


}
