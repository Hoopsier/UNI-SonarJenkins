package org.example.demo1;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class BMIResultServiceTest {

    @Test
    void databaseFailureDoesNotEscapeSaveOperation() {
        PrintStream originalError = System.err;
        try {
            System.setErr(new PrintStream(new ByteArrayOutputStream()));
            assertDoesNotThrow(() ->
                    BMIResultService.saveResult(70.0, 1.75, 22.86, "en"));
        } finally {
            System.setErr(originalError);
        }
    }
}
