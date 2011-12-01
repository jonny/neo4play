package com.thoughtworks.helloworld.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;

public abstract class DbCommand {

	abstract void execute(GraphDatabaseService graphDb);

}
