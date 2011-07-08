/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.salaboy.tests;

import com.wordpress.salaboy.model.Address;
import com.wordpress.salaboy.model.Person;
import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ClassPathResource;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 * @author salaboy
 */
public class ReteNetworkTest {

    public ReteNetworkTest() {
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
    public void firstRuleDrl() {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("firstRule.drl"), ResourceType.DRL);
        
        if(kbuilder.hasErrors()){
            throw new IllegalStateException("Knowledge Resources cannot be parsed! "+ kbuilder.getErrors().iterator().next().getMessage());
        }
        
        KnowledgeBase kbase = kbuilder.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);
        ksession.insert(new Person("Salaboy", 28));
        
        int fired = ksession.fireAllRules();
        
        assertEquals(1, fired);
    }
    
    @Test
    public void secondRuleDrl() {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("secondRule.drl"), ResourceType.DRL);
        
        if(kbuilder.hasErrors()){
            throw new IllegalStateException("Knowledge Resources cannot be parsed! "+ kbuilder.getErrors().iterator().next().getMessage());
        }
        
        KnowledgeBase kbase = kbuilder.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);
        Person person = new Person("Salaboy", 28);
        ksession.insert(person);
        
        ksession.insert(new Address("nowhere","",1425,"BA"));
        // We have an activation
        
        int fired = ksession.fireAllRules();
        
        assertEquals(1, fired);
    }
    
    @Test
    public void thirdRuleDrl() {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("thirdRule.drl"), ResourceType.DRL);
        
        if(kbuilder.hasErrors()){
            throw new IllegalStateException("Knowledge Resources cannot be parsed! "+ kbuilder.getErrors().iterator().next().getMessage());
        }
        
        KnowledgeBase kbase = kbuilder.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        
        KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);
        
        
        Person salaboy = new Person("Salaboy", 28);
        Address address = new Address("nowhere","",1425,"BA");
        // We need to set up the reference to the address object inside the person object for the rule to evaluate to true
        salaboy.setAddress(address);
        ksession.insert(salaboy);
        ksession.insert(address);
        int fired = ksession.fireAllRules();
        
        
        
        assertEquals(1, fired);
    }
    
    @Test
    public void threeRulesTogetherDrl() {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("threeRulesTogether.drl"), ResourceType.DRL);
        
        if(kbuilder.hasErrors()){
            throw new IllegalStateException("Knowledge Resources cannot be parsed! "+ kbuilder.getErrors().iterator().next().getMessage());
        }
        
        KnowledgeBase kbase = kbuilder.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);
        Person salaboy = new Person("Salaboy", 28);
        Address address = new Address("nowhere","",1425,"BA");
        salaboy.setAddress(address);
        ksession.insert(salaboy);
        ksession.insert(address);
        int fired = ksession.fireAllRules();
        
        assertEquals(3, fired);
    }
}
