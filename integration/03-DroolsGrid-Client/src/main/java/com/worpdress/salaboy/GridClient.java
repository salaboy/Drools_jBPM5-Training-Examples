package com.worpdress.salaboy;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactoryService;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactoryService;
import org.drools.builder.ResourceType;
import org.drools.grid.ConnectionFactoryService;
import org.drools.grid.GridConnection;
import org.drools.grid.GridNode;
import org.drools.grid.GridServiceDescription;
import org.drools.grid.conf.GridPeerServiceConfiguration;
import org.drools.grid.conf.impl.GridPeerConfiguration;
import org.drools.grid.impl.GridImpl;
import org.drools.grid.service.directory.Address;
import org.drools.grid.service.directory.WhitePages;
import org.drools.grid.service.directory.impl.CoreServicesLookupConfiguration;
import org.drools.grid.service.directory.impl.GridServiceDescriptionImpl;
import org.drools.grid.service.directory.impl.WhitePagesRemoteConfiguration;
import org.drools.io.impl.ByteArrayResource;
import org.drools.runtime.StatefulKnowledgeSession;

/**
 * This class creates a connection with a remote node (grid configuration inside drools-camel-server)
 * and creates a new knowledge session with a new rule and an entity declaration. 
 * We can use the soap/rest endpoints to interact with this newly created session. 
 * 
 */
public class GridClient 
{
    public static void main( String[] args )
    {
        //ClassPathXmlApplicationContext springContext = new ClassPathXmlApplicationContext( "classpath:beans-test-grid.xml" );
        Map<String, GridServiceDescription> coreServicesMap = new HashMap<String, GridServiceDescription>();
        GridServiceDescriptionImpl gsd = new GridServiceDescriptionImpl(WhitePages.class.getName());
        Address addr = gsd.addAddress("socket");
        addr.setObject(new InetSocketAddress[]{new InetSocketAddress("localhost", 8000)});
        coreServicesMap.put(WhitePages.class.getCanonicalName(), gsd);    
        
        GridImpl grid2 = new GridImpl(new ConcurrentHashMap<String, Object>());
        
        GridPeerConfiguration conf = new GridPeerConfiguration();
        GridPeerServiceConfiguration coreSeviceConf = new CoreServicesLookupConfiguration( coreServicesMap );
        conf.addConfiguration( coreSeviceConf );
        
        //coreServicesMap = Hazelcast.newHazelcastInstance( null ).getMap( CoreServicesLookup.class.getName() );
        
       

        GridPeerServiceConfiguration wprConf = new WhitePagesRemoteConfiguration( );
        conf.addConfiguration( wprConf );

        conf.configure( grid2 );
        
        GridServiceDescription<GridNode> n1Gsd = grid2.get( WhitePages.class ).lookup( "node1" );
        GridConnection<GridNode> conn = grid2.get( ConnectionFactoryService.class ).createConnection( n1Gsd );
        GridNode remoteN1 = conn.connect();

        KnowledgeBuilder kbuilder = remoteN1.get( KnowledgeBuilderFactoryService.class ).newKnowledgeBuilder();

        
        
        String rule = "package  org.grid.test\n"
                + "declare Message2\n"
                +    "text : String\n"
                + "end\n"
                + "rule \"echo2\" \n"
                + "dialect \"mvel\"\n"
                +   "when\n"
                + "     $m : Message2()\n"
                +   "then\n"
                +       "$m.text = \"echo2:\" + $m.text;\n"
                + "end\n";
                //System.out.println("Rule = "+rule);
        kbuilder.add( new ByteArrayResource( rule.getBytes() ),
                      ResourceType.DRL );

        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if ( errors != null && errors.size() > 0 ) {
            for ( KnowledgeBuilderError error : errors ) {
                System.out.println( "Error: " + error.getMessage() );

            }
            return;
        }

        KnowledgeBase kbase = remoteN1.get( KnowledgeBaseFactoryService.class ).newKnowledgeBase();

        

        kbase.addKnowledgePackages( kbuilder.getKnowledgePackages() );

        StatefulKnowledgeSession session = kbase.newStatefulKnowledgeSession();

        
        
        
        remoteN1.set("ksession3", session);
        
        
        
        remoteN1.dispose();
        
       

    }
}
