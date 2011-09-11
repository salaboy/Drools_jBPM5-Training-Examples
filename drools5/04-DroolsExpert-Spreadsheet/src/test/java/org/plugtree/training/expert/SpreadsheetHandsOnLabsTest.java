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
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.plugtree.training.handsonlabs.enums.ItemType;
import org.plugtree.training.handsonlabs.model.SpecialOffer;
import org.plugtree.training.handsonlabs.model.StockItem;

public class SpreadsheetHandsOnLabsTest  {

    private StatefulKnowledgeSession ksession;

    @Before
    public void setUp() throws Exception {
        
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("/rules/HandsOnLabsRules.drl", getClass()), ResourceType.DRL);
    	
    	
//    	SpreadsheetCompiler compiler = new SpreadsheetCompiler();
//	      String drl = compiler.compile("/04-DecisionTableSolution.xls", InputType.XLS);
//	      System.out.println("DRL String:"+drl);
//    	DecisionTableConfiguration dtableconfiguration = KnowledgeBuilderFactory.newDecisionTableConfiguration();
//        dtableconfiguration.setInputType(DecisionTableInputType.XLS);
//        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
//        kbuilder.add(new ClassPathResource("/04-DecisionTableSolution.xls", getClass()), ResourceType.DTABLE, dtableconfiguration);
    	
    	
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
        KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);
    }
    
    //@Test
    public void nothing() {
    	
    }
    
    @Test
    public void testExecution() {
    	
    	//Regular prices
    	Double samsung_led_price = 800d;
    	Double samsung_lcd_price = 400d;
    	Double samsung_tube_price = 200d;
    	
    	Double sony_led_price = 900d;
    	Double sony_lcd_price = 500d;
    	
    	
    	//TVs
    	StockItem samsungLEDTV1 = new StockItem(ItemType.LED_TV,"Samsung");
    	StockItem samsungLEDTV2 = new StockItem(ItemType.LED_TV,"Samsung");
    	
    	StockItem samsungLCDTV1 = new StockItem(ItemType.LCD_TV,"Samsung");
    	
    	StockItem samsungTUBETV1 = new StockItem(ItemType.TUBE_TV,"Samsung");
    	
    	StockItem sonyLEDTV1 = new StockItem(ItemType.LED_TV,"Sony");
    	
    	StockItem sonyLCDTV1 = new StockItem(ItemType.LCD_TV,"Sony");
    	StockItem sonyLCDTV2 = new StockItem(ItemType.LCD_TV,"Sony");
    	
    	//PROMOTIONAL PRICES
    	SpecialOffer samsungLCDSpecialOffer = new SpecialOffer(ItemType.LCD_TV, "Samsung", 300d); 
    	
    	SpecialOffer sonyLEDSpecialOffer = new SpecialOffer(ItemType.LED_TV, "Sony", 850d);
    	
    	//Insert promotional prices as globals
    	ksession.setGlobal("samsung_led_price", samsung_led_price);
    	ksession.setGlobal("samsung_lcd_price", samsung_lcd_price);
    	ksession.setGlobal("samsung_tube_price", samsung_tube_price);
    	ksession.setGlobal("sony_led_price", sony_led_price);
    	ksession.setGlobal("sony_lcd_price", sony_lcd_price);
        
    	//Insert Special prices as facts
    	ksession.insert(samsungLCDSpecialOffer);
    	ksession.insert(sonyLEDSpecialOffer);
    	
    	//Insert items as facts
        ksession.insert(samsungLEDTV1);
        ksession.insert(samsungLEDTV2);
        ksession.insert(samsungLCDTV1);
        ksession.insert(samsungTUBETV1);
        ksession.insert(sonyLEDTV1);
        ksession.insert(sonyLCDTV1);
        ksession.insert(sonyLCDTV2);
        
        //fire all rules
        ksession.fireAllRules();
        
        
        //Control
        
        //SAMSUNG
        Assert.assertEquals("",samsung_led_price.doubleValue(), samsungLEDTV1.getPrice(),0.1);
        Assert.assertEquals("",samsung_led_price.doubleValue(), samsungLEDTV2.getPrice(),0.1);

        Assert.assertEquals("",samsungLCDSpecialOffer.getPrice(), samsungLCDTV1.getPrice(),0.1);
        
        Assert.assertEquals("",samsung_tube_price.doubleValue(), samsungTUBETV1.getPrice(),0.1);
        
        //SONY
        Assert.assertEquals("",sonyLEDSpecialOffer.getPrice(), sonyLEDTV1.getPrice(),0.1);
        
        Assert.assertEquals("",sony_lcd_price.doubleValue(), sonyLCDTV1.getPrice(),0.1);
        Assert.assertEquals("",sony_lcd_price.doubleValue(), sonyLCDTV2.getPrice(),0.1);
        
        
        ksession.dispose();
    }
}
