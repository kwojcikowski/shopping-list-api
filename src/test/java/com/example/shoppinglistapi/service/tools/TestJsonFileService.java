package com.example.shoppinglistapi.service.tools;

import com.example.shoppinglistapi.model.*;
import com.google.gson.JsonIOException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

public class TestJsonFileService {

    @Test
    public void testLoadCollectionFromFile(){
        try {
            JsonFileService.loadCollectionFromFile(FilePath.SECTIONS_FILE_PATH, Section.class);
            JsonFileService.loadCollectionFromFile(FilePath.UNITS_FILE_PATH, Unit.class);
            JsonFileService.loadCollectionFromFile(FilePath.PREFIXES_FILE_PATH, Prefix.class);
            JsonFileService.loadCollectionFromFile(FilePath.STORES_FILE_PATH, Store.class);
            JsonFileService.loadCollectionFromFile(FilePath.BASE_UNITS_FILE_PATH, BaseUnit.class);
            JsonFileService.loadCollectionFromFile(FilePath.STORE_SECTIONS_FILE_PATH, StoreSection.class);
        } catch (IOException e) {
            fail("Exception should not had been thrown.");
        }
    }

    @Test
    public void testLoadCollectionFromFileThrowExceptionOnNonExistingFile(){
        Path nonExistingPath = Path.of("some", "path");
        try{
            JsonFileService.loadCollectionFromFile(Path.of("some", "path"), Section.class);
            fail("Exception should be thrown on non existing file.");
        }catch (IOException e){
            assertThat(e.getMessage()).isEqualTo("Unable to read " + nonExistingPath.toAbsolutePath() + ".");
        }
    }

    @Test
    public void testLoadCollectionFromFileThrowExceptionOnIncorrectJsonFile(){
        Path incorrectJsonFilePath = FilePath.TEST_INCORRECT_JSON_FILE_PATH;
        try{
            JsonFileService.loadCollectionFromFile(incorrectJsonFilePath, Section.class);
            fail("Exception should be thrown when unable to parse given file.");
        }catch (JsonIOException | IOException e){
            assertThat(e.getMessage())
                    .isEqualTo("Unable to parse " + incorrectJsonFilePath.toAbsolutePath() + " as json file.");
        }
    }

    @Test
    public void testLoadCollectionFromFileThrowExceptionOnMismatchedClassType(){
        Path sectionsFilePath = FilePath.SECTIONS_FILE_PATH;
        try{
            JsonFileService.loadCollectionFromFile(sectionsFilePath, Store.class);
            fail("Exception should be thrown when collection key not found.");
        }catch (JsonIOException | IOException e){
            assertThat(e.getMessage())
                    .isEqualTo("Could not find collection of key stores.");
        }
    }
}
