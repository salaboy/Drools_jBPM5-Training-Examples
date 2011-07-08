package org.plugtree.training;

import java.util.ArrayList;
import java.util.List;



import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.plugtree.training.enums.CustomerType;
import org.plugtree.training.enums.ShippingType;
import org.plugtree.training.model.Customer;
import org.plugtree.training.model.Order;

public class DSLExampleTest {

	private StatefulKnowledgeSession ksession;
	private List<Order> rejectedNational = new ArrayList<Order>();
	private List<Order> rejectedInternational = new ArrayList<Order>();
	private List<Order> priorityCustomer = new ArrayList<Order>();
        @Before
	public void setUp() throws Exception {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(new ClassPathResource("/rules/CustomLanguage.dsl", getClass()), ResourceType.DSL);
		kbuilder.add(new ClassPathResource("/rules/Rules.dslr", getClass()), ResourceType.DSLR);
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if (errors.size() > 0) {
			for (KnowledgeBuilderError error : errors) {
				System.err.println(error);
			}
			throw new IllegalArgumentException("Could not parse knowledge.");
		}
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

		ksession = kbase.newStatefulKnowledgeSession();
                // KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);
	}
        @Test
	public void testExecution() {

		Customer johnInternational = new Customer("John Z", CustomerType.INTERNATIONAL);
		Customer annaInternational = new Customer("Anna M", CustomerType.INTERNATIONAL);
		Customer maryInternational = new Customer("Mary C", CustomerType.NATIONAL);
		Customer jesseNational = new Customer("Jessy D", CustomerType.NATIONAL);

		Order internationalExpressOrder = new Order();
		internationalExpressOrder.setCustomer(johnInternational);
		internationalExpressOrder.setShipping(ShippingType.EXPRESS);
		internationalExpressOrder.setAmount(100);

		Order internationalUSPSOrderRejected = new Order();
		internationalUSPSOrderRejected.setCustomer(annaInternational);
		internationalUSPSOrderRejected.setShipping(ShippingType.USPS);
		internationalUSPSOrderRejected.setAmount(10);

		Order internationalUSPSOrderAccepted = new Order();
		internationalUSPSOrderAccepted.setCustomer(johnInternational);
		internationalUSPSOrderAccepted.setShipping(ShippingType.USPS);
		internationalUSPSOrderAccepted.setAmount(110);

		Order nationalStandardOrder = new Order();
		nationalStandardOrder.setCustomer(maryInternational);
		nationalStandardOrder.setShipping(ShippingType.STANDARD);
		nationalStandardOrder.setAmount(90.7f);

		Order nationalExpressOrder = new Order();
		nationalExpressOrder.setCustomer(jesseNational);
		nationalExpressOrder.setShipping(ShippingType.EXPRESS);
		nationalExpressOrder.setAmount(930);

		ksession.setGlobal("rejectedNational", rejectedNational);
		ksession.setGlobal("rejectedInternational", rejectedInternational);
		ksession.setGlobal("priorityCustomer", priorityCustomer);

		ksession.insert(internationalExpressOrder);
		ksession.insert(internationalUSPSOrderRejected);
		ksession.insert(internationalUSPSOrderAccepted);
		ksession.insert(nationalStandardOrder);
		ksession.insert(nationalExpressOrder);
		ksession.fireAllRules();

		Assert.assertEquals(1, rejectedNational.size());
		Assert.assertEquals(1, rejectedInternational.size());
		Assert.assertEquals(2, priorityCustomer.size());

	}

}
