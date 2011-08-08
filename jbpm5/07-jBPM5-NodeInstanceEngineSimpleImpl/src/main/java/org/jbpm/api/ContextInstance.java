/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.api;

import java.util.Map;

/**
 *
 * @author salaboy
 */
public interface ContextInstance {
    public Map<String, Object> getVariables();
    public void setVariables(Map<String, Object> variables);
}
