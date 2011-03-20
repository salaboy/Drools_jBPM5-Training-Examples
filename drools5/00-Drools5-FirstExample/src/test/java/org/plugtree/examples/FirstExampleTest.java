package org.plugtree.examples;


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
import org.junit.Test;
import org.plugtree.examples.model.Person;
import org.plugtree.examples.model.Pet;


public class FirstExample {


    /**
     * Cat on a Tree Test
     */
    @Test
    public void catOnATreeTest() {
        // Create the Knowledge Builder
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        // Add our rules
        kbuilder.add(new ClassPathResource("rules.drl"), ResourceType.DRL);
        //Check for errors during the compilation of the rules
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error : errors) {
                System.err.println(error);
            }
            throw new IllegalArgumentException("Could not parse knowledge.");
        }

        // Create the Knowledge Base
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        // Add the binary packages (compiled rules) to the Knowledge Base
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        // Create the StatefulSession using the Knowledge Base that contains
        // the compiled rules
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

        // We can add a runtime logger to understand what is going on inside the
        // Engine
        KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);


        // Create a Person
        Person person = new Person("Salaboy!");
        // Create a Pet
        Pet pet = new Pet("mittens", "on a limb", Pet.PetType.CAT);
        // Set the Pet to the Person
        person.setPet(pet);

        // Now we will insert the Pet and the Person into the KnowledgeSession
        ksession.insert(pet);
        ksession.insert(person);

        // We will fire all the rules that were activated
        ksession.fireAllRules();


    }
}
