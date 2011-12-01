package com.thoughtworks.helloworld.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;

public class NeoWrapper {
	
	private final GraphDatabaseService graphDb;

	public NeoWrapper(String path) {
		this.graphDb = new EmbeddedGraphDatabase(path);
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

	
	private void registerShutdownHook(final GraphDatabaseService graphDb) {
	    Runtime.getRuntime().addShutdownHook(new Thread() {
	        @Override
	        public void run() {
	            graphDb.shutdown();
	        }
	    });
	}

}
