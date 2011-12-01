package com.thoughtworks.helloworld.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

public class NeoWrapper {
	
	private final GraphDatabaseService graphDb;

	public NeoWrapper(GraphDatabaseService graphDb) {
		this.graphDb = graphDb;
		registerShutdownHook(graphDb);
	}
	
	public void execute(DbCommand command) {
		Transaction tx = graphDb.beginTx();
		try {
			command.execute(graphDb);
			tx.success();
			
		} finally {
		    tx.finish();
		}

	}
	
	public void cleanUp() {
		execute(new DbCommand() {
			
			@Override
			void execute(GraphDatabaseService graphDb) {
				for (Node node : graphDb.getAllNodes()) {
				    for (Relationship rel : node.getRelationships()) {
				        rel.delete();
				    }
				    node.delete();
				}
			}
		});		
	}

	
	private void registerShutdownHook(final GraphDatabaseService graphDb) {
	    Runtime.getRuntime().addShutdownHook(new Thread() {
	        @Override
	        public void run() {
	            graphDb.shutdown();
	        }
	    });
	}

}
