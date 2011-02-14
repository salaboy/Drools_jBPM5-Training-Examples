package com.wordpress.salaboy.example.model;

/**
 * Created by IntelliJ IDEA.
 * User: salaboy
 * Date: 2/14/11
 * Time: 10:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class Emergency {
    private int revision = 0 ;
    private String phoneCall;
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
}
