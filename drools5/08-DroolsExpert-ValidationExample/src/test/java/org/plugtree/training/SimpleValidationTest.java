/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.plugtree.training;

import org.plugtree.training.simplevalidation.model.control.CorrectionRequest;
import java.util.UUID;
import org.plugtree.training.simplevalidation.model.control.Validation;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ClassPathResource;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.QueryResultsRow;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.plugtree.training.simplevalidation.model.Address;
import org.plugtree.training.simplevalidation.model.Person;
import org.plugtree.training.simplevalidation.model.Phone;
import org.plugtree.training.simplevalidation.model.control.ErrorEntry;
import static org.junit.Assert.*;

/**
 *
 * @author salaboy
 */
public class SimpleValidationTest {

    public SimpleValidationTest() {
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
    public void noErrorsValidation() {
        Person person = new Person("salaboy");
        person.addAddress(new Address(person.getId(), "7th", 123, "92013"));
        person.addPhone(new Phone(person.getId(), "555-1235"));

        StatefulKnowledgeSession ksession = createKSession();
        KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);

        for (Address address : person.getAddresses()) {
            ksession.insert(address);
        }

        for (Phone phone : person.getPhones()) {
            ksession.insert(phone);
        }

        ksession.insert(person);


        int fired = ksession.fireAllRules();
        assertEquals(0, fired);

        org.drools.runtime.rule.QueryResults results =
                ksession.getQueryResults("getAllErrors", new Object[]{});
        assertEquals(0, results.size());



    }

    @Test
    public void addressError() {
        Person person = new Person("salaboy");
        person.addAddress(new Address(person.getId(), "7th", null, "92013"));
        person.addPhone(new Phone(person.getId(), "555-1235"));

        StatefulKnowledgeSession ksession = createKSession();
        KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);

        if (person.getAddresses() != null) {
            for (Address address : person.getAddresses()) {
                ksession.insert(address);
            }
        }

        if (person.getPhones() != null) {
            for (Phone phone : person.getPhones()) {
                ksession.insert(phone);
            }
        }

        ksession.insert(person);


        ksession.fireAllRules();

        org.drools.runtime.rule.QueryResults results =
                ksession.getQueryResults("getAllErrors", new Object[]{});
        assertEquals(1, results.size());


        for (QueryResultsRow row : results) {
            System.out.println(">>> Error ( " + ((ErrorEntry) row.get("$error")).getError().toString() + ")");
            assertEquals("Invalid Address Number", ((ErrorEntry) row.get("$error")).getMessage());
        }


    }

    @Test
    public void noPhoneOrNoAddressError() {
        Person person = new Person("salaboy");
        person.addPhone(new Phone(person.getId(), "555-1235"));

        StatefulKnowledgeSession ksession = createKSession();
        KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);
        if (person.getAddresses() != null) {
            for (Address address : person.getAddresses()) {
                ksession.insert(address);
            }
        }
        if (person.getPhones() != null) {
            for (Phone phone : person.getPhones()) {
                ksession.insert(phone);
            }
        }

        ksession.insert(person);


        ksession.fireAllRules();

        org.drools.runtime.rule.QueryResults results =
                ksession.getQueryResults("getAllErrors", new Object[]{});
        assertEquals(1, results.size());


        for (QueryResultsRow row : results) {
            System.out.println(">>> Error ( " + ((ErrorEntry) row.get("$error")).getError().toString() + ")");
            assertEquals("No Address or No Phone", ((ErrorEntry) row.get("$error")).getMessage());
        }


    }

    @Test
    public void getErrorsByValidationId() {
        Person person = new Person("salaboy");
        person.addPhone(new Phone(person.getId(), "555-1235"));

        StatefulKnowledgeSession ksession = createKSessionWithValidationById();
        KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);
        Validation validation = new Validation("My Validation");
        ksession.insert(validation);

        if (person.getAddresses() != null) {
            for (Address address : person.getAddresses()) {
                ksession.insert(address);
            }
        }
        if (person.getPhones() != null) {
            for (Phone phone : person.getPhones()) {
                ksession.insert(phone);
            }
        }

        ksession.insert(person);


        ksession.fireAllRules();

        //Result for Random ID = 0
        org.drools.runtime.rule.QueryResults results =
                ksession.getQueryResults("getAllErrorsByValidationId", new Object[]{UUID.randomUUID().toString()});
        assertEquals(0, results.size());
        
        //Result for All Errors ID = 1
        results =
                ksession.getQueryResults("getAllErrors", new Object[]{});
        assertEquals(1, results.size());
        
        //Results for this validation = 1
        results =
                ksession.getQueryResults("getAllErrorsByValidationId", new Object[]{validation.getId()});
        assertEquals(1, results.size());


        for (QueryResultsRow row : results) {
            System.out.println(">>> Error ( " + ((ErrorEntry) row.get("$error")).getError().toString() + ")");
            assertEquals("No Address or No Phone", ((ErrorEntry) row.get("$error")).getMessage());
        }


    }
    
    @Test
    public void breakOnErrors() {
        Person person = new Person("salaboy");
        person.addPhone(new Phone(person.getId(), "555-1235"));

        StatefulKnowledgeSession ksession = createKSessionWithBreak();
        KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);
        Validation validation = new Validation("My Validation");
        ksession.insert(validation);

        if (person.getAddresses() != null) {
            for (Address address : person.getAddresses()) {
                ksession.insert(address);
            }
        }
        if (person.getPhones() != null) {
            for (Phone phone : person.getPhones()) {
                ksession.insert(phone);
            }
        }

        ksession.insert(person);


        ksession.fireAllRules();

        //Result for Random ID = 0
        org.drools.runtime.rule.QueryResults results =
                ksession.getQueryResults("getAllErrorsByValidationId", new Object[]{UUID.randomUUID().toString()});
        assertEquals(0, results.size());
        
        //Result for All Errors ID = 1
        results =
                ksession.getQueryResults("getAllErrors", new Object[]{});
        assertEquals(1, results.size());
        
        //Results for this validation = 1
        results =
                ksession.getQueryResults("getAllErrorsByValidationId", new Object[]{validation.getId()});
        assertEquals(1, results.size());


        for (QueryResultsRow row : results) {
            System.out.println(">>> Error ( " + ((ErrorEntry) row.get("$error")).getError().toString() + ")");
            assertEquals("No Address or No Phone", ((ErrorEntry) row.get("$error")).getMessage());
        }


    }
    
     @Test
    public void moreLogicalTMSApproach() {
        Person person = new Person("salaboy");
        person.addPhone(new Phone(person.getId(), "555-1235"));

        StatefulKnowledgeSession ksession = createKSessionMoreLogical();
        KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);
        Validation validation = new Validation("My Validation");
        ksession.insert(validation);

        if (person.getAddresses() != null) {
            for (Address address : person.getAddresses()) {
                ksession.insert(address);
            }
        }
        if (person.getPhones() != null) {
            for (Phone phone : person.getPhones()) {
                ksession.insert(phone);
            }
        }

        ksession.insert(person);


        ksession.fireAllRules();

        //Result for All Errors = 1
        org.drools.runtime.rule.QueryResults results =
                ksession.getQueryResults("getAllErrors", new Object[]{});
        assertEquals(1, results.size());
        
        
        //Corrections should be 1
        results =
                ksession.getQueryResults("getAllCorrectionRequest", new Object[]{});
        assertEquals(1, results.size());
        
        for (QueryResultsRow row : results) {
            System.out.println(">>> Error ( " + ((CorrectionRequest) row.get("$correction")).getError().toString() + ")");
            assertEquals("No Address or No Phone", ((CorrectionRequest) row.get("$correction")).getError().getMessage());
        }
        
        ksession.insert(new Address(person.getId(), "7th", 123, "92013")); 
        // I've already fix the problem
        results =
                ksession.getQueryResults("getAllCorrectionRequest", new Object[]{});
        assertEquals(0, results.size());
        
        
        //Result for All Errors = 0
        results =
                ksession.getQueryResults("getAllErrors", new Object[]{});
        assertEquals(0, results.size());
        
       


    }
    
    
    
    

    /**
     * Compiles resources and creates a new ksession.
     * @return
     */
    private StatefulKnowledgeSession createKSession() {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("rules/SimpleValidationRules.drl"), ResourceType.DRL);

        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error : errors) {
                System.err.println(error);
            }
            throw new IllegalArgumentException("Could not parse knowledge.");
        }

        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        //KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);

        return ksession;
    }

    /**
     * Compiles resources and creates a new ksession.
     * @return
     */
    private StatefulKnowledgeSession createKSessionWithValidationById() {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("rules/SimpleValidationRulesWithValidationId.drl"), ResourceType.DRL);

        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error : errors) {
                System.err.println(error);
            }
            throw new IllegalArgumentException("Could not parse knowledge.");
        }

        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        //KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);

        return ksession;
    }
    
     /**
     * Compiles resources and creates a new ksession.
     * @return
     */
    private StatefulKnowledgeSession createKSessionWithBreak() {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("rules/SimpleValidationRulesWithBreak.drl"), ResourceType.DRL);

        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error : errors) {
                System.err.println(error);
            }
            throw new IllegalArgumentException("Could not parse knowledge.");
        }

        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        //KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);

        return ksession;
    }
    
       /**
     * Compiles resources and creates a new ksession.
     * @return
     */
    private StatefulKnowledgeSession createKSessionMoreLogical() {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("rules/SimpleValidationRulesMoreLogical.drl"), ResourceType.DRL);

        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error : errors) {
                System.err.println(error);
            }
            throw new IllegalArgumentException("Could not parse knowledge.");
        }

        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        //KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);

        return ksession;
    }
}
