package org.plugtree.training.expert;


import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.DecisionTableConfiguration;
import org.drools.builder.DecisionTableInputType;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.decisiontable.InputType;
import org.drools.decisiontable.SpreadsheetCompiler;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.plugtree.training.enums.CreditStatus;
import org.plugtree.training.model.Job;
import org.plugtree.training.model.Person;

public class SpreadsheetExampleTest  {

    private StatefulKnowledgeSession ksession;

    @Before
    public void setUp() throws Exception {
        DecisionTableConfiguration dtableconfiguration = KnowledgeBuilderFactory.newDecisionTableConfiguration();
        dtableconfiguration.setInputType(DecisionTableInputType.XLS);

        
        SpreadsheetCompiler compiler = new SpreadsheetCompiler();
        String drl = compiler.compile("/rules/CreditRules.xls", InputType.XLS);
        System.out.println("DRL String:"+drl);
        
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("/rules/CreditRules.xls", getClass()), ResourceType.DTABLE, dtableconfiguration);
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error : errors) {
                System.err.println(error);
            }
            throw new IllegalArgumentException("Could not parse knowledge.");
        }

    
//        
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        ksession = kbase.newStatefulKnowledgeSession();
        //KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);
    }
    @Test
    public void testExecution() {
        Person p1 = new Person(20);
        p1.setJob(new Job(2, 3000f));
        Person p2 = new Person(18);
        p2.setJob(new Job(0, 5000f));
        Person p3 = new Person(47);
        p3.setJob(new Job(10, 4500f));
        ksession.insert(p1);
        ksession.insert(p2);
        ksession.insert(p3);
        ksession.fireAllRules();

        Assert.assertEquals(CreditStatus.ACCEPTED, p1.getCredit().getStatus());
        Assert.assertEquals(CreditStatus.REJECTED, p2.getCredit().getStatus());
        Assert.assertEquals(CreditStatus.REJECTED, p2.getCredit().getStatus());

    }
}
