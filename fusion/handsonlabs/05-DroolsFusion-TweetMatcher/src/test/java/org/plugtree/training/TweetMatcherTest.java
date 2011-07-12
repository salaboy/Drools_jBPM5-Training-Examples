/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.plugtree.training;

import java.util.List;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.conf.EventProcessingOption;
import org.drools.io.impl.ClassPathResource;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.WorkingMemoryEntryPoint;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 *
 * @author salaboy
 */
public class TweetMatcherTest {
    
    public TweetMatcherTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    //plugtreelabs
    //apli8526

    //http://twitter4j.org/en/code-examples.html
    public void hello() throws TwitterException {

        StatefulKnowledgeSession ksession = this.createKSession();
        WorkingMemoryEntryPoint workingMemoryEntryPoint = ksession.getWorkingMemoryEntryPoint("tweets");
        // The factory instance is re-useable and thread safe.
        String twitterID = "droolsfusion";
        String twitterPassword = "testdroolsfusion";
        Twitter twitter = new TwitterFactory().getInstance(twitterID, twitterPassword);
//        Status status = twitter.updateStatus("Testing Twitter4j Api with fusion!");
//        System.out.println("Successfully updated the status to [" + status.getText() + "].");
        
        List<Status> statuses = twitter.getFriendsTimeline();
        System.out.println("Showing friends timeline.");
        for (Status statusnow : statuses) {
            
            workingMemoryEntryPoint.insert(new Tweet(statusnow.getCreatedAt().getTime(),statusnow.getText()));
            ksession.fireAllRules();
        }
    }
    private StatefulKnowledgeSession createKSession(){

        //Rules compilation
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("rules/tweetRules.drl"), ResourceType.DRL);

        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error : errors) {
                System.err.println(error);
            }
            throw new IllegalArgumentException("Could not parse knowledge.");
        }

        //KBase creation
        KnowledgeBaseConfiguration kbaseConfig = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
        kbaseConfig.setOption(EventProcessingOption.STREAM);

        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(kbaseConfig);
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());


        //KSession creation
      

        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);

     
        return ksession;
    }
}
