/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.api.impl;

import java.util.HashMap;
import java.util.Map;
import org.jbpm.api.ContextInstance;

/**
 *
 * @author salaboy
 */
public class ContextInstanceImpl implements ContextInstance {
    private Map<String, Object> variables = new HashMap<String, Object>();

    public ContextInstanceImpl() {
    }

    @Override
    public Map<String, Object> getVariables() {
        return variables;
    }

    @Override
    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }
    
    
    
    
}
