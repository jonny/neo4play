package com.thoughtworks.helloworld.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.index.Index;
import org.neo4j.kernel.EmbeddedGraphDatabase;

public class HelloPeople {
	
	private static enum RelTypes implements RelationshipType {
	    USERS_REFERENCE,
	    USER
	}

	private static final String DB_PATH = "target/people-db/";
	private static final String USERNAME_KEY = "username";
	
	private static GraphDatabaseService graphDb;
	private static Index<Node> nodeIndex;

	public static void main(String[] args) {	
		graphDb = new EmbeddedGraphDatabase(DB_PATH);
		nodeIndex = graphDb.index().forNodes( "nodes" );

		NeoWrapper db = new NeoWrapper(graphDb);
		
		db.execute(new DbCommand() {
			
			@Override
			void execute(GraphDatabaseService graphDb) {
				Node usersReferenceNode = graphDb.createNode();
			    graphDb.getReferenceNode().createRelationshipTo(
			        usersReferenceNode, RelTypes.USERS_REFERENCE );

			    // Create some users and index their names with the IndexService
			    for ( int id = 0; id < 100; id++ ) {
			        Node userNode = createAndIndexUser( idToUserName( id ) );
			        usersReferenceNode.createRelationshipTo( userNode, RelTypes.USER );
			    }				
			}
		});
		
		
		int idToFind = 45;
		Node foundUser = nodeIndex.get( USERNAME_KEY, idToUserName( idToFind ) ).getSingle();
		System.out.println( "The username of user " + idToFind + " is "+ foundUser.getProperty( USERNAME_KEY ) );
		
		db.cleanUp();

	}
	
	
	
	private static String idToUserName( final int id ) {
	    return "user" + id + "@neo4j.org";
	}
	 
	private static Node createAndIndexUser( final String username ) {
	    Node node = graphDb.createNode();
	    node.setProperty( USERNAME_KEY, username );
	    nodeIndex.add( node, USERNAME_KEY, username );
	    return node;
	}

}
