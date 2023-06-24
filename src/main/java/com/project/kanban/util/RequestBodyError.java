package com.project.kanban.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RequestBodyError {
        public static StringBuilder returnRequiredFields(BindingResult bindingResult) {
            List<RequiredFieldSignal> requiredFieldSignals = new ArrayList<>();
            StringBuilder errorMessage = new StringBuilder();
            bindingResult.getFieldErrors().forEach(
                    fieldError -> {
                        requiredFieldSignals.add(
                                new RequiredFieldSignal(fieldError.getField(), fieldError.getDefaultMessage()));
                    }
            );
            for (RequiredFieldSignal requiredFieldSignal : requiredFieldSignals) {
                errorMessage.append("Field required: ")
                        .append(requiredFieldSignal.getField())
                        .append(", cause: ")
                        .append(requiredFieldSignal.getCause()).append(". ");
            }
            return errorMessage;
        }
}

