package com.project.kanban.util;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

public class RequestBodyError {
    public static ResponseEntity<Object> returnRequiredFields(BindingResult bindingResult){
        List<RequiredFieldSignal> requiredFieldSignals = new ArrayList<>();
        StringBuilder errorMessage = new StringBuilder();
        bindingResult.getFieldErrors().forEach(
                fieldError -> {
                    requiredFieldSignals.add(
                            new RequiredFieldSignal(fieldError.getField(), fieldError.getDefaultMessage()));
                }
        );
        for (RequiredFieldSignal requiredFieldSignal : requiredFieldSignals){
            errorMessage.append("Field required: ").append(
                    requiredFieldSignal.getField())
                        .append(", cause: ")
                        .append(requiredFieldSignal.getCause()).append(". ");
        }
        return ResponseEntity.badRequest().body(errorMessage.toString());
    }
}
