package org.plugtree.training.model;

import java.util.Date;

/**
 *
 * @author esteban.aliverti@gmail.com
 */
public class Procedure {
    private Date date;
    private String code;
    private double quantity;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

}
