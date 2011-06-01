/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.salaboy;

import ca.odell.glazedlists.AbstractEventList;
import java.util.ArrayList;
import java.util.List;
import org.drools.runtime.rule.Row;
import org.drools.runtime.rule.ViewChangedEventListener;

/**
 *
 * @author salaboy
 */
public class MyDroolsEventList extends AbstractEventList<Row> implements ViewChangedEventListener {
    List<Row> data = new ArrayList<Row>();
     
    public Row get(int index) {
        return this.data.get( index );
    }
 
    public int size() {
        return this.data.size();
    }
 
    public void rowAdded(Row row) {
        int index = size();
        updates.beginEvent();
        updates.elementInserted(index, row);
        boolean result = data.add(row);
        updates.commitEvent();
    }
 
    public void rowRemoved(Row row) {
        int index = this.data.indexOf( row );
        updates.beginEvent();
        Row removed = data.remove( index );
        updates.elementDeleted(index, removed);       
        updates.commitEvent();
    }
 
    public void rowUpdated(Row row) {
        int index = this.data.indexOf( row );   
        updates.beginEvent();
        updates.elementUpdated(index, row, row);
        updates.commitEvent();
    }

    public void dispose() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}