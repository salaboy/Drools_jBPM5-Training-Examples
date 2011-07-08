package org.plugtre.training;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentConfiguration;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.io.ResourceChangeScannerConfiguration;
import org.drools.io.ResourceFactory;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.plugtree.training.enums.InsuranceType;
import org.plugtree.training.enums.VehicleType;
import org.plugtree.training.model.Person;
import org.plugtree.training.model.Vehicle;

public class KnowledgeAgentExampleTest extends TestCase {

	private StatefulKnowledgeSession ksession;

	protected void setUp() throws Exception {
		KnowledgeAgentConfiguration configuration = KnowledgeAgentFactory.newKnowledgeAgentConfiguration();
		configuration.setProperty("drools.agent.scanDirectories", "true");
		ResourceChangeScannerConfiguration sconf = ResourceFactory.getResourceChangeScannerService().newResourceChangeScannerConfiguration();
		sconf.setProperty( "drools.resource.scanner.interval", "1" ); 

		ResourceFactory.getResourceChangeScannerService().configure( sconf );
		ResourceFactory.getResourceChangeNotifierService().start();
		ResourceFactory.getResourceChangeScannerService().start();

		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		KnowledgeAgent kagent = (KnowledgeAgent) KnowledgeAgentFactory.newKnowledgeAgent("incremental builder", kbase, configuration);

		kagent.applyChangeSet(new ClassPathResource("/config/change-set.xml", getClass()));
		kbase = kagent.getKnowledgeBase();

		ksession = kagent.getKnowledgeBase().newStatefulKnowledgeSession();
	}

	public void testExecution() {
		Vehicle car1 = new Vehicle(new Person(20), VehicleType.CAR, 2006, 35000f);
		Vehicle car2 = new Vehicle(new Person(60), VehicleType.CAR, 1960, 50000f);
		Vehicle car3 = new Vehicle(new Person(34), VehicleType.CAR, 1994, 4000f);
		Vehicle moto1 = new Vehicle(new Person(16), VehicleType.MOTORCYCLE, 1993, 3230F);
		Vehicle moto2 = new Vehicle(new Person(19), VehicleType.MOTORCYCLE, 2001, 8400F);
		Vehicle moto3 = new Vehicle(new Person(29), VehicleType.MOTORCYCLE, 2010, 10000F);

		ksession.insert(car1);
		ksession.insert(car2);
		ksession.insert(car3);
		ksession.insert(moto1);
		ksession.insert(moto2);
		ksession.insert(moto3);

		ksession.fireAllRules();

		Assert.assertEquals(InsuranceType.NORMAL, car1.getInsurance().getType());
		Assert.assertEquals(InsuranceType.NOT_AVAILABLE, car2.getInsurance().getType());
		Assert.assertEquals(InsuranceType.FULL, car3.getInsurance().getType());
		Assert.assertEquals(InsuranceType.NOT_AVAILABLE, moto1.getInsurance().getType());
		Assert.assertEquals(InsuranceType.FULL, moto2.getInsurance().getType());
		Assert.assertEquals(InsuranceType.NORMAL, moto3.getInsurance().getType());
	}

}
