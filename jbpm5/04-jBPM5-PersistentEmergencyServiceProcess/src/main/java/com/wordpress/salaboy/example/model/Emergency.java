package com.wordpress.salaboy.example.model;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: salaboy
 * Date: 2/14/11
 * Time: 10:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class Emergency implements Serializable {
    private int revision = 0 ;
    private String phoneCall;
    private String type;

    public Emergency() {
    }

    public Emergency(String phoneCall) {
        this.phoneCall = phoneCall;
    }

    public String getPhoneCall() {
        return phoneCall;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public int getRevision() {
        return revision;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Emergency{" +
                "revision=" + revision +
                ", phoneCall='" + phoneCall + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
