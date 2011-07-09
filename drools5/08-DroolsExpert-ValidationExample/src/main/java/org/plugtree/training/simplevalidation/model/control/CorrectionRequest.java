/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.plugtree.training.simplevalidation.model.control;

/**
 *
 * @author salaboy
 */
public class CorrectionRequest {
    //The error that should be corrected
    private ErrorEntry error;

    public CorrectionRequest(ErrorEntry error) {
        this.error = error;
    }

    public ErrorEntry getError() {
        return error;
    }

    @Override
    public String toString() {
        return "CorrectionRequest{" + "error=" + error + '}';
    }
    
    
    
    
    
}
