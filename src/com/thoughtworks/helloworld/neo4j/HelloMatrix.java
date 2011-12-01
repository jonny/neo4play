package com.thoughtworks.helloworld.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.index.Index;
import org.neo4j.kernel.EmbeddedGraphDatabase;

public class HelloMatrix {
	
	private static enum RelTypes implements RelationshipType {
		NEO_NODE,
        KNOWS,
        CODED_BY
	}

	private static final String DB_PATH = "target/matrix-db/";
	private static final String USERNAME_KEY = "username";
	
	private static GraphDatabaseService graphDb;
	private static Index<Node> nodeIndex;

	public static void main(String[] args) {	
		graphDb = new EmbeddedGraphDatabase(DB_PATH);
		nodeIndex = graphDb.index().forNodes( "nodes" );

		NeoWrapper db = new NeoWrapper(graphDb);
		
		populateNodeSpace(db);
		
		db.execute(new DbCommand() {
			
			@Override
			void execute(GraphDatabaseService graphDb) {
			}
		});
		
		
		db.cleanUp();

	}

	private static void populateNodeSpace(NeoWrapper db) {
		db.execute(new DbCommand() {
			
			@Override
			void execute(GraphDatabaseService graphDb) {
				Node thomas = graphDb.createNode();
		        thomas.setProperty( "name", "Thomas Anderson" );
		        thomas.setProperty( "age", 29 );
		
		        // connect Neo/Thomas to the reference node
		        Node referenceNode = graphDb.getReferenceNode();
		        referenceNode.createRelationshipTo( thomas, RelTypes.NEO_NODE );
		
		        Node trinity = graphDb.createNode();
		        trinity.setProperty( "name", "Trinity" );
		        Relationship rel = thomas.createRelationshipTo( trinity,
		                RelTypes.KNOWS );
		        rel.setProperty( "age", "3 days" );
		        Node morpheus = graphDb.createNode();
		        morpheus.setProperty( "name", "Morpheus" );
		        morpheus.setProperty( "rank", "Captain" );
		        morpheus.setProperty( "occupation", "Total badass" );
		        thomas.createRelationshipTo( morpheus, RelTypes.KNOWS );
		        rel = morpheus.createRelationshipTo( trinity, RelTypes.KNOWS );
		        rel.setProperty( "age", "12 years" );
		        Node cypher = graphDb.createNode();
		        cypher.setProperty( "name", "Cypher" );
		        cypher.setProperty( "last name", "Reagan" );
		        trinity.createRelationshipTo( cypher, RelTypes.KNOWS );
		        rel = morpheus.createRelationshipTo( cypher, RelTypes.KNOWS );
		        rel.setProperty( "disclosure", "public" );
		        Node smith = graphDb.createNode();
		        smith.setProperty( "name", "Agent Smith" );
		        smith.setProperty( "version", "1.0b" );
		        smith.setProperty( "language", "C++" );
		        rel = cypher.createRelationshipTo( smith, RelTypes.KNOWS );
		        rel.setProperty( "disclosure", "secret" );
		        rel.setProperty( "age", "6 months" );
		        Node architect = graphDb.createNode();
		        architect.setProperty( "name", "The Architect" );
		        smith.createRelationshipTo( architect, RelTypes.CODED_BY );
			}
		});

		
	}
	
	

}
