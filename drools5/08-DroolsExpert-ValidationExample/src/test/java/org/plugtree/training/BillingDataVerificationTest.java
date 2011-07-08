/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.plugtree.training;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static junit.framework.Assert.*;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.joda.time.DateMidnight;
import org.joda.time.base.BaseDateTime;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.plugtree.training.model.Bill;
import org.plugtree.training.model.Procedure;

/**
 *
 * @author esteban
 */
public class BillingDataVerificationTest {

    public BillingDataVerificationTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    @Test
    public void doNothing(){}
    
    @Ignore
    public void case1Fail() {

        DateMidnight admission = new DateMidnight(2010, 5, 4);
        DateMidnight discharge = new DateMidnight(2010, 5, 5);

        List<Procedure> procedures = new ArrayList<Procedure>();
        procedures.add(this.createProcedure(admission, "8080", 1));
        procedures.add(this.createProcedure(admission, "8080", 1));

        Bill bill = this.createBill(admission, discharge, "", procedures);

        StatefulKnowledgeSession ksession = this.createKSession();

        List<String> errors = new ArrayList<String>();
        ksession.setGlobal("errors", errors);

        ksession.insert(bill);

        for (Procedure procedure : bill.getProcedures()) {
            ksession.insert(procedure);
        }

        ksession.fireAllRules();

        for (String error : errors) {
            System.out.println("Error: " + error);
        }
        System.out.println("-----");
        assertEquals(2, errors.size());

        errors.clear();
        ksession.dispose();
    }

    @Ignore
    public void case1Ok() {
        DateMidnight admission = new DateMidnight(2010, 5, 4);
        DateMidnight discharge = new DateMidnight(2010, 5, 6);

        DateMidnight d4 = new DateMidnight(2010, 5, 4);
        DateMidnight d5 = new DateMidnight(2010, 5, 5);
        DateMidnight d6 = new DateMidnight(2010, 5, 6);



        List<Bill> bills = new ArrayList<Bill>();

        //same code, different dates
        List<Procedure> procedures = new ArrayList<Procedure>();
        procedures.add(this.createProcedure(d4, "8080", 1));
        procedures.add(this.createProcedure(d5, "8080", 1));
        procedures.add(this.createProcedure(d6, "8080", 1));
        bills.add(this.createBill(admission, discharge, "", procedures));

        //different codes, same dates. It also tests different Bills containing
        //Procedures with same code and same date
        procedures = new ArrayList<Procedure>();
        procedures.add(this.createProcedure(d4, "8080", 1));
        procedures.add(this.createProcedure(d5, "8081", 1));
        procedures.add(this.createProcedure(d6, "8082", 1));
        bills.add(this.createBill(admission, discharge, "", procedures));

        StatefulKnowledgeSession ksession = this.createKSession();

        List<String> errors = new ArrayList<String>();
        ksession.setGlobal("errors", errors);

        for (Bill bill : bills) {
            ksession.insert(bill);

            for (Procedure procedure : bill.getProcedures()) {
                ksession.insert(procedure);
            }
        }
        ksession.fireAllRules();

        for (String error : errors) {
            System.out.println("Error: " + error);
        }
        System.out.println("-----");

        assertEquals(0, errors.size());
        errors.clear();
        ksession.dispose();
    }

    @Ignore
    public void case2() {
        DateMidnight d4 = new DateMidnight(2010, 5, 4);
        DateMidnight d5 = new DateMidnight(2010, 5, 5);
        DateMidnight d6 = new DateMidnight(2010, 5, 6);
        DateMidnight d7 = new DateMidnight(2010, 5, 7);


        int fail = 0;
        List<Bill> bills = new ArrayList<Bill>();

        //FAIL: procedure date > discharge date
        List<Procedure> procedures = new ArrayList<Procedure>();
        procedures.add(this.createProcedure(d6, "8080", 1));
        bills.add(this.createBill(d5, d5, "", procedures));
        fail++;

        //FAIL: procedure date < admission date
        procedures = new ArrayList<Procedure>();
        procedures.add(this.createProcedure(d4, "8080", 1));
        bills.add(this.createBill(d6, d6, "", procedures));
        fail++;

        //FAIL: procedure date > discharge date && procedure date < admission date
        procedures = new ArrayList<Procedure>();
        procedures.add(this.createProcedure(d4, "8080", 1));
        procedures.add(this.createProcedure(d7, "8080", 1));
        bills.add(this.createBill(d5, d6, "", procedures));
        fail++;
        fail++;

        //OK
        procedures = new ArrayList<Procedure>();
        procedures.add(this.createProcedure(d5, "8080", 1));
        procedures.add(this.createProcedure(d6, "8080", 1));
        procedures.add(this.createProcedure(d7, "8080", 1));
        bills.add(this.createBill(d5, d7, "", procedures));


        StatefulKnowledgeSession ksession = this.createKSession();

        List<String> errors = new ArrayList<String>();
        ksession.setGlobal("errors", errors);

        for (Bill bill : bills) {
            ksession.insert(bill);

            for (Procedure procedure : bill.getProcedures()) {
                ksession.insert(procedure);
            }
        }
        ksession.fireAllRules();

        for (String error : errors) {
            System.out.println("Error: " + error);
        }
        System.out.println("-----");

        assertEquals(fail, errors.size());
        errors.clear();
        ksession.dispose();
    }

    @Ignore
    public void case3() {
        DateMidnight d4 = new DateMidnight(2010, 5, 4);
        DateMidnight d5 = new DateMidnight(2010, 5, 5);
        DateMidnight d6 = new DateMidnight(2010, 5, 6);

        int fail = 0;
        List<Bill> bills = new ArrayList<Bill>();

        //FAIL: empty procedures
        //(2 fails: bill's procedure list is empty and the number of bills != total days)
        List<Procedure> procedures = new ArrayList<Procedure>();
        bills.add(this.createBill(d4, d4, "", procedures));
        fail++;
        fail++;

        //OK
        procedures = new ArrayList<Procedure>();
        procedures.add(this.createProcedure(d4, "8080", 1));
        procedures.add(this.createProcedure(d5, "8080", 1));
        bills.add(this.createBill(d4, d5, "", procedures));


        StatefulKnowledgeSession ksession = this.createKSession();

        List<String> errors = new ArrayList<String>();
        ksession.setGlobal("errors", errors);

        for (Bill bill : bills) {
            ksession.insert(bill);

            if (bill.getProcedures() != null) {
                for (Procedure procedure : bill.getProcedures()) {
                    ksession.insert(procedure);
                }
            }
        }
        ksession.fireAllRules();

        for (String error : errors) {
            System.out.println("Error: " + error);
        }
        System.out.println("-----");
        
        assertEquals(fail, errors.size());
        errors.clear();
        ksession.dispose();
    }

    @Ignore
    public void case4() {
        DateMidnight d4 = new DateMidnight(2010, 5, 4);
        DateMidnight d5 = new DateMidnight(2010, 5, 5);
        DateMidnight d6 = new DateMidnight(2010, 5, 6);

        int fail = 0;
        List<Bill> bills = new ArrayList<Bill>();

        //FAIL: 2 days only 1 "Room Charge"
        List<Procedure> procedures = new ArrayList<Procedure>();
        procedures.add(this.createProcedure(d4, "8080", 1));
        bills.add(this.createBill(d4, d5, "", procedures));
        fail++;

        //OK: 2 days only 1 "Room Charge" (Died)
        procedures = new ArrayList<Procedure>();
        procedures.add(this.createProcedure(d4, "8080", 1));
        bills.add(this.createBill(d4, d5, "Died", procedures));

        //OK
        procedures = new ArrayList<Procedure>();
        procedures.add(this.createProcedure(d4, "8080", 1));
        procedures.add(this.createProcedure(d5, "8080", 1));
        bills.add(this.createBill(d4, d5, "", procedures));

        //FAIL: 2 days only 2 "Room Charge" (Died)
        procedures = new ArrayList<Procedure>();
        procedures.add(this.createProcedure(d4, "8080", 1));
        procedures.add(this.createProcedure(d5, "8080", 1));
        bills.add(this.createBill(d4, d5, "Died", procedures));
        fail++;
        
        //FAIL: 3 days only 1 "Room Charge" (Died)
        procedures = new ArrayList<Procedure>();
        procedures.add(this.createProcedure(d4, "8080", 1));
        bills.add(this.createBill(d4, d6, "Died", procedures));
        fail++;

        //FAIL: 2 days only 3 "Room Charge" (Died)
        //(2 fails: Procedures billed outside the hospitalization period)
        procedures = new ArrayList<Procedure>();
        procedures.add(this.createProcedure(d4, "8080", 1));
        procedures.add(this.createProcedure(d5, "8080", 1));
        procedures.add(this.createProcedure(d6, "8080", 1));
        bills.add(this.createBill(d4, d5, "Died", procedures));
        fail++;
        fail++;

        //FAIL: 2 days only 3 "Room Charge"
        //(2 fails: Procedures billed outside the hospitalization period)
        procedures = new ArrayList<Procedure>();
        procedures.add(this.createProcedure(d4, "8080", 1));
        procedures.add(this.createProcedure(d5, "8080", 1));
        procedures.add(this.createProcedure(d6, "8080", 1));
        bills.add(this.createBill(d4, d5, "", procedures));
        fail++;
        fail++;        
        
        StatefulKnowledgeSession ksession = this.createKSession();

        List<String> errors = new ArrayList<String>();
        ksession.setGlobal("errors", errors);

        for (Bill bill : bills) {
            ksession.insert(bill);

            if (bill.getProcedures() != null) {
                for (Procedure procedure : bill.getProcedures()) {
                    ksession.insert(procedure);
                }
            }
        }
        ksession.fireAllRules();

        for (String error : errors) {
            System.out.println("Error: " + error);
        }
        System.out.println("-----");

        assertEquals(fail, errors.size());
        errors.clear();
        ksession.dispose();
    }

    @Ignore
    public void case5() {
        DateMidnight d4 = new DateMidnight(2010, 5, 4);
        DateMidnight d5 = new DateMidnight(2010, 5, 5);
        DateMidnight d6 = new DateMidnight(2010, 5, 6);

        int fail = 0;
        List<Bill> bills = new ArrayList<Bill>();

        //OK: 2 days only 1 "Room Charge" (Died)
        List<Procedure> procedures = new ArrayList<Procedure>();
        procedures.add(this.createProcedure(d4, "8080", 1));
        bills.add(this.createBill(d4, d5, "Died", procedures));

        //FAIL
        procedures = new ArrayList<Procedure>();
        procedures.add(this.createProcedure(d4, "8080", 1));
        procedures.add(this.createProcedure(d5, "8080", 2));
        bills.add(this.createBill(d4, d5, "", procedures));
        fail++;


        //FAIL
        //(2 fails)
        procedures = new ArrayList<Procedure>();
        procedures.add(this.createProcedure(d4, "8080", 2));
        procedures.add(this.createProcedure(d5, "8080", 2));
        bills.add(this.createBill(d4, d6, "Died", procedures));
        fail++;
        fail++;

        //Ok
        procedures = new ArrayList<Procedure>();
        procedures.add(this.createProcedure(d4, "8080", 1));
        procedures.add(this.createProcedure(d5, "8080", 1));
        procedures.add(this.createProcedure(d6, "8080", 1));
        bills.add(this.createBill(d4, d6, "", procedures));

        //Ok
        procedures = new ArrayList<Procedure>();
        procedures.add(this.createProcedure(d4, "1010", 2));
        procedures.add(this.createProcedure(d5, "1010", 3));
        procedures.add(this.createProcedure(d6, "1010", 4));
        procedures.add(this.createProcedure(d4, "8010", 1));
        procedures.add(this.createProcedure(d5, "8010", 1));
        procedures.add(this.createProcedure(d6, "8010", 1));
        bills.add(this.createBill(d4, d6, "", procedures));

        StatefulKnowledgeSession ksession = this.createKSession();

        List<String> errors = new ArrayList<String>();
        ksession.setGlobal("errors", errors);

        for (Bill bill : bills) {
            ksession.insert(bill);

            if (bill.getProcedures() != null) {
                for (Procedure procedure : bill.getProcedures()) {
                    ksession.insert(procedure);
                }
            }
        }
        ksession.fireAllRules();

        for (String error : errors) {
            System.out.println("Error: " + error);
        }
        System.out.println("-----");

        assertEquals(fail, errors.size());
        errors.clear();
        ksession.dispose();
    }

    @Ignore
    public void case6() {
        DateMidnight d4 = new DateMidnight(2010, 5, 4);
        DateMidnight d5 = new DateMidnight(2010, 5, 5);
        DateMidnight d6 = new DateMidnight(2010, 5, 6);

        int fail = 0;
        List<Bill> bills = new ArrayList<Bill>();

        //OK:
        List<Procedure> procedures = new ArrayList<Procedure>();
        procedures.add(this.createProcedure(d4, "8080", 1));
        procedures.add(this.createProcedure(d5, "8080", 1));
        procedures.add(this.createProcedure(d5, "1010", 1));
        procedures.add(this.createProcedure(d5, "2020", 2));
        procedures.add(this.createProcedure(d5, "3030", 2));
        bills.add(this.createBill(d4, d5, "", procedures));

        //FAIL: missing 3030 for d5
        procedures = new ArrayList<Procedure>();
        procedures.add(this.createProcedure(d4, "8080", 1));
        procedures.add(this.createProcedure(d4, "3030", 2));
        procedures.add(this.createProcedure(d5, "8080", 1));
        procedures.add(this.createProcedure(d5, "1010", 1));
        procedures.add(this.createProcedure(d5, "2020", 2));
        bills.add(this.createBill(d4, d5, "", procedures));
        fail++;


        //OK
        procedures = new ArrayList<Procedure>();
        procedures.add(this.createProcedure(d4, "8080", 1));
        procedures.add(this.createProcedure(d5, "8080", 1));
        procedures.add(this.createProcedure(d5, "1010", 1));
        procedures.add(this.createProcedure(d5, "3030", 1));
        procedures.add(this.createProcedure(d6, "8080", 1));
        bills.add(this.createBill(d4, d6, "", procedures));

        //Ok
        procedures = new ArrayList<Procedure>();
        procedures.add(this.createProcedure(d4, "8080", 1));
        procedures.add(this.createProcedure(d4, "1010", 1));
        procedures.add(this.createProcedure(d4, "2020", 2));
        procedures.add(this.createProcedure(d4, "3030", 2));
        procedures.add(this.createProcedure(d5, "8080", 1));
        procedures.add(this.createProcedure(d5, "1010", 1));
        procedures.add(this.createProcedure(d5, "2020", 2));
        procedures.add(this.createProcedure(d5, "3030", 2));
        bills.add(this.createBill(d4, d5, "", procedures));

        StatefulKnowledgeSession ksession = this.createKSession();

        List<String> errors = new ArrayList<String>();
        ksession.setGlobal("errors", errors);

        for (Bill bill : bills) {
            ksession.insert(bill);

            if (bill.getProcedures() != null) {
                for (Procedure procedure : bill.getProcedures()) {
                    ksession.insert(procedure);
                }
            }
        }
        ksession.fireAllRules();

        for (String error : errors) {
            System.out.println("Error: " + error);
        }
        System.out.println("-----");

        assertEquals(fail, errors.size());
        errors.clear();
        ksession.dispose();
    }

    @Ignore
    public void case7() {
        DateMidnight d4 = new DateMidnight(2010, 5, 4);
        DateMidnight d5 = new DateMidnight(2010, 5, 5);
        DateMidnight d6 = new DateMidnight(2010, 5, 6);

        int fail = 0;
        List<Bill> bills = new ArrayList<Bill>();

        //OK
        List<Procedure> procedures = new ArrayList<Procedure>();
        procedures.add(this.createProcedure(d4, "8080", 1));
        procedures.add(this.createProcedure(d5, "8080", 1));
        procedures.add(this.createProcedure(d5, "4040", 2));
        bills.add(this.createBill(d4, d5, "", procedures));

        //OK
        procedures = new ArrayList<Procedure>();
        procedures.add(this.createProcedure(d4, "8080", 1));
        procedures.add(this.createProcedure(d5, "8080", 1));
        procedures.add(this.createProcedure(d5, "5050", 2));
        bills.add(this.createBill(d4, d5, "", procedures));

        //FAIL
        procedures = new ArrayList<Procedure>();
        procedures.add(this.createProcedure(d4, "8080", 1));
        procedures.add(this.createProcedure(d5, "8080", 1));
        procedures.add(this.createProcedure(d5, "4040", 2));
        procedures.add(this.createProcedure(d5, "5050", 2));
        bills.add(this.createBill(d4, d5, "", procedures));
        fail++;

        //FAIL: even when 4040 and 5050 are in different days
        procedures = new ArrayList<Procedure>();
        procedures.add(this.createProcedure(d4, "8080", 1));
        procedures.add(this.createProcedure(d4, "5050", 2));
        procedures.add(this.createProcedure(d5, "8080", 1));
        procedures.add(this.createProcedure(d5, "4040", 2));
        bills.add(this.createBill(d4, d5, "", procedures));
        fail++;

        StatefulKnowledgeSession ksession = this.createKSession();

        List<String> errors = new ArrayList<String>();
        ksession.setGlobal("errors", errors);

        for (Bill bill : bills) {
            ksession.insert(bill);

            if (bill.getProcedures() != null) {
                for (Procedure procedure : bill.getProcedures()) {
                    ksession.insert(procedure);
                }
            }
        }
        ksession.fireAllRules();

        for (String error : errors) {
            System.out.println("Error: " + error);
        }
        System.out.println("-----");

        assertEquals(fail, errors.size());
        errors.clear();
        ksession.dispose();
    }

    /**
     * Compiles resources and creates a new ksession.
     * @return
     */
    private StatefulKnowledgeSession createKSession() {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(new ClassPathResource("rules/VerificationRules.drl"), ResourceType.DRL);

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

    private Bill createBill(BaseDateTime admissionDate, BaseDateTime dischargeDate, String dischargeReason, List<Procedure> procedures) {
        return this.createBill(new Date(admissionDate.getMillis()), new Date(dischargeDate.getMillis()), dischargeReason, procedures);
    }

    private Bill createBill(Date admissionDate, Date dischargeDate, String dischargeReason, List<Procedure> procedures) {
        Bill p = new Bill();
        p.setAdmissionDate(admissionDate);
        p.setDischargeDate(dischargeDate);
        p.setDischargeReason(dischargeReason);
        p.setProcedures(procedures);

        return p;
    }

    private Procedure createProcedure(BaseDateTime date, String code, double quantity) {
        return this.createProcedure(new Date(date.getMillis()), code, quantity);
    }

    private Procedure createProcedure(Date date, String code, double quantity) {
        Procedure p = new Procedure();
        p.setCode(code);
        p.setQuantity(quantity);
        p.setDate(date);

        return p;
    }
}
