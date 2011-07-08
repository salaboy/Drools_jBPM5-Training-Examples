package org.plugtree.training.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.joda.time.Period;

/**
 *
 * @author esteban.aliverti@gmail.com
 */
public class Bill {
    private Date admissionDate;
    private Date dischargeDate;
    private String dischargeReason;
    private List<Procedure> procedures = new ArrayList<Procedure>();

    public Date getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(Date admissionDate) {
        this.admissionDate = admissionDate;
    }

    public Date getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(Date dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public String getDischargeReason() {
        return dischargeReason;
    }

    public void setDischargeReason(String dischargeReason) {
        this.dischargeReason = dischargeReason;
    }

    public List<Procedure> getProcedures() {
        return procedures;
    }

    public void setProcedures(List<Procedure> procedures) {
        this.procedures = procedures;
    }

    

    public int getTotalDays(){
        if (this.admissionDate == null){
            return 0;
        }

        Date start = this.admissionDate;
        Date end = (this.dischargeDate == null)?new Date():this.dischargeDate;

        return new Period(start.getTime(), end.getTime()).getDays()+1;
    }
    

}
