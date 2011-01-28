import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: salaboy
 * Date: 1/28/11
 * Time: 3:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleEmergencyServiceProcessTest {



    @Test
    public void simpleTest(){

        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("processes/emergency-service-simple.bpmn"), ResourceType.BPMN2);
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

        ksession.startProcess("com.plugtree.training.simple.emergencyservice");



    }
}
