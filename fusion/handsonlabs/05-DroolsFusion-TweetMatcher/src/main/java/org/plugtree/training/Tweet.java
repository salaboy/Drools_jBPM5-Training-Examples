/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.plugtree.training;

import java.util.Date;

/**
 *
 * @author salaboy
 */
public class Tweet {
    private long createdAt;
    private String text;

    public Tweet() {
    }

    public Tweet(long createdAt, String text) {
        this.createdAt = createdAt;
        this.text = text;
        System.out.println(this.createdAt+" - Text = "+this.text);
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
}
