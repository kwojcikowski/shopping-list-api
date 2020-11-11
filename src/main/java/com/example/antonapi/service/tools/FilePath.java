package com.example.antonapi.service.tools;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FilePath {

    public static final Path DATA_FOLDER_PATH =
            FileSystems.getDefault().getPath("src","main", "resources", "data");

    public static final Path BASE_UNITS_FILE_PATH = Paths.get(DATA_FOLDER_PATH.toString(), "baseUnits.json");
    public static final Path PREFIXES_FILE_PATH = Paths.get(DATA_FOLDER_PATH.toString(),"prefixes.json");
    public static final Path SECTIONS_FILE_PATH = Paths.get(DATA_FOLDER_PATH.toString(),"sections.json");
    public static final Path STORE_SECTIONS_FILE_PATH = Paths.get(DATA_FOLDER_PATH.toString(),"storeSections.json");
    public static final Path STORES_FILE_PATH = Paths.get(DATA_FOLDER_PATH.toString(),"stores.json");
    public static final Path UNITS_FILE_PATH = Paths.get(DATA_FOLDER_PATH.toString(),"units.json");

    public static final Path TEST_DATA_FOLDER_PATH =
            FileSystems.getDefault().getPath("src","test", "resources", "data");

    public static final Path TEST_INCORRECT_JSON_FILE_PATH =
            Paths.get(TEST_DATA_FOLDER_PATH.toString(), "incorrectJsonFile.json");
}
