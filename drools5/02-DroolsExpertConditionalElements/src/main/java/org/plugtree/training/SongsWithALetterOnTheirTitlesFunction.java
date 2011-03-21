/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.plugtree.training;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.drools.base.accumulators.AccumulateFunction;
import org.plugtree.training.model.Song;

/**
 *
 * @author esteban.aliverti@gmail.com
 */
public class SongsWithALetterOnTheirTitlesFunction implements AccumulateFunction{

    public static class ContextData implements Serializable{
        private List<Song> songs = new ArrayList<Song>();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    }

    public Serializable createContext() {
        return new ContextData();
    }

    public void init(Serializable context) throws Exception {
        ((ContextData)context).songs.clear();
    }

    public void accumulate(Serializable context, Object value) {
        if (((Song)value).getTitle().contains("a")){
            ((ContextData)context).songs.add((Song)value);
        }
    }

    public void reverse(Serializable context, Object value) throws Exception {
        ((ContextData)context).songs.remove((Song)value);
    }

    public Object getResult(Serializable context) throws Exception {
        return ((ContextData)context).songs;
    }

    public boolean supportsReverse() {
        return true;
    }

}
