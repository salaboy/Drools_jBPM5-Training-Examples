/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.nodes.impl;

import org.jbpm.api.SequenceFlow;

/**
 *
 * @author salaboy
 */
public class SequenceFlowImpl implements SequenceFlow{
    private Long sourceId;
    private Long destinationId;

    public SequenceFlowImpl(Long sourceId, Long destinationId) {
        this.sourceId = sourceId;
        this.destinationId = destinationId;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }
    
    
    
}
