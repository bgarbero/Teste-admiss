package com.teste_admiss.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MessagesTest {

    @Test
    void constants_shouldContainExpectedValues() {
        assertAll("Messages constants",
                () -> assertEquals("The field cannot be null, empty or blank", Messages.NOT_BLANK),
                () -> assertEquals("The field cannot be zero.", Messages.NOT_NULL),
                () -> assertEquals("Resource not found: ", Messages.RESOURCE_NOT_FOUND),
                () -> assertEquals("Field must contain between {min} and {max} characters", Messages.FIELD_SIZE_MESSAGE),
                () -> assertEquals("Validation exception", Messages.VALIDATION_EXCEPTION),
                () -> assertEquals("Validation errors!! Check the errors found below", Messages.VALIDATION_MESSAGE),
                () -> assertEquals("The year must be greater than 1900.", Messages.YEAR_MIN)
        );
    }
}
