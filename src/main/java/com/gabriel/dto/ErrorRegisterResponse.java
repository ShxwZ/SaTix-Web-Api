package com.gabriel.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
public class ErrorRegisterResponse implements Serializable {
    private Map<String, String> errors;

    public ErrorRegisterResponse() {
        errors = new HashMap<>();
    }

    public ErrorRegisterResponse(Map<String, String> errors) {
        this.errors = errors;
    }

    public void addError(String fieldName, String errorMessage) {
        errors.put(fieldName, errorMessage);
    }
    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
