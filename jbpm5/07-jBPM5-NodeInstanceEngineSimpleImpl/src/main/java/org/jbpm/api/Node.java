/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.api;

import java.util.List;
import java.util.Map;

/**
 *
 * @author salaboy
 */
public interface Node {

    public void setName(String name);

    public List<SequenceFlow> getIncomingFlows(String type);

    public List<SequenceFlow> getOutgoingFlows(String type);

    public Map<String, List<SequenceFlow>> getIncomingFlows();

    public Map<String, List<SequenceFlow>> getOutgoingFlows();

    public void addIncomingFlow(String type, SequenceFlow flow);

    public void addOutgoingFlow(String type, SequenceFlow flow);

    public void removeIncomingFlow(String type, SequenceFlow flow);

    public void removeOutgoingFlow(String type, SequenceFlow flow);
}
