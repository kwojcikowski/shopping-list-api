package com.example.antonapi.service.tools;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class TestFilePath {

    @ParameterizedTest(name = "{0}")
    @MethodSource("getFilePathFields")
    public void verifyFileExistence(TestedField testedField){
        try {
            assertThat(Files.exists((Path) testedField.field.get(null))).isTrue();
        } catch (IllegalAccessException e) {
            fail("Exception should not had been thrown");
        }
    }

    public static Stream<TestedField> getFilePathFields(){
        return Arrays.stream(FilePath.class.getDeclaredFields()).map(TestedField::new);
    }

    private static class TestedField {
        Field field;

        private TestedField(Field field){
            this.field = field;
        }

        @Override
        public String toString() {
            return field.getName();
        }
    }
}