package org.plugtree.training;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.hibernate.Session;
import org.plugtree.training.model.Address;
import org.plugtree.training.model.Person;
import org.plugtree.training.model.Pet;
import org.plugtree.training.model.PetShop;
import org.plugtree.training.model.Person.OcupationType;
import org.plugtree.training.model.Pet.PetType;
import org.plugtree.training.util.HibernateUtil;

public class ConditionalElementsRulesTest extends TestCase {

	public ConditionalElementsRulesTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(ConditionalElementsRulesTest.class);
	}

	public void testConditionalElementsRules() {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(new ClassPathResource("rules/conditionalRules.drl"), ResourceType.DRL);

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

		Session session = HibernateUtil.getSession();

		Person daleCooper = createPerson("Dale", "Cooper", 30, OcupationType.FBI_SPECIAL_AGENT);
		Person lelandPalmer = createPerson("Leland", "Palmer", 51, OcupationType.LAWYER);
		Person lauraPalmer = createPerson("Laura", "Palmer", 18, OcupationType.STUDENT);
		Person harryTruman = createPerson("Harry", "Truman", 38, OcupationType.OFFICER);

		PetShop happyAnimals = new PetShop("Happy Animals", new Address("Lala 653", 4, "NA", "1013"));
		PetShop maddog = new PetShop("Maddog", new Address("Mall Street 27", 0, "NA", "1013"));

		Pet cat = new Pet("Chatran", PetType.CAT, happyAnimals);
		Pet monkey1 = new Pet("Mumo", PetType.MONKEY, happyAnimals);
		Pet monkey2 = new Pet("Dufo", PetType.MONKEY, maddog);
		Pet monkey3 = new Pet("Dufo 2", PetType.MONKEY, maddog);
		Pet monkey4 = new Pet("Dufo 3", PetType.MONKEY, maddog);
		Pet fish1 = new Pet("Dum", PetType.FISH, maddog);
		Pet fish2 = new Pet("Nemo", PetType.FISH, happyAnimals);

		session.beginTransaction().begin();
		session.save(cat);
		session.save(monkey1);
		session.save(monkey2);
		session.save(monkey3);
		session.save(monkey4);
		session.save(fish2);
		session.getTransaction().commit();

		ksession.setGlobal("session", session);

		ksession.insert(daleCooper);
		ksession.insert(lelandPalmer);
		ksession.insert(lauraPalmer);
		ksession.insert(harryTruman);

		ksession.insert(happyAnimals);
		ksession.insert(maddog);

		ksession.insert(cat);
		ksession.insert(monkey1);
		ksession.insert(monkey2);
		ksession.insert(fish1);

		ksession.fireAllRules();

	}

	private Person createPerson(String name, String lastName, int age, OcupationType ocupation) {
		Person person = new Person(name, lastName, age, ocupation);
		person.setAddress(new Address("Evergreen 100", 1, "C", "1064"));
		person.addPet(new Pet("Tweetie", Pet.PetType.BIRD));
		person.addPet(new Pet("Chuwaka", Pet.PetType.DOG));
		person.addPet(new Pet("Pipo", Pet.PetType.CAT));
		return person;
	}

}
