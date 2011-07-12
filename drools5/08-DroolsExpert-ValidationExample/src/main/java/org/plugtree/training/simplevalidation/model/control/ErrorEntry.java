/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.plugtree.training.simplevalidation.model.control;

import java.util.UUID;

/**
 *
 * @author salaboy
 */
public class ErrorEntry {
    
    private String id;
    private String validationId;
    private Object error;
    private String message;
    
    public ErrorEntry(String validationId, String message, Object error){ 
        this.id = UUID.randomUUID().toString();
        this.validationId = validationId;
        this.message = message;
        this.error = error;
    }

    public ErrorEntry(String message, Object error) {
        this(null, message, error);
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getValidationId() {
        return validationId;
    }
    
    
    public Object getError() {
        return error;
    }

    @Override
    public String toString() {
        return "ErrorEntry{" + "id=" + id + ", validationId=" + validationId + ", error=" + error + ", message=" + message + '}';
    }
    
    

    
}
