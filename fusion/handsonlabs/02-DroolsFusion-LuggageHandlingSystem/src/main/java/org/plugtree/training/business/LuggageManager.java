/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.plugtree.training.business;

import java.util.HashMap;
import java.util.Map;
import org.plugtree.training.model.Luggage;

/**
 *
 * @author esteban
 */
public class LuggageManager {

    private Map<String,Luggage> registeredLuggages = new HashMap<String, Luggage>();

    public Luggage registerNewLuggage(){
        Luggage l = new Luggage();
        this.registeredLuggages.put(l.getCode(), l);
        return l;
    }

    public boolean isLuggageRegistered(String code){
        return this.registeredLuggages.containsKey(code);
    }

}
