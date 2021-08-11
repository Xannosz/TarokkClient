package hu.xannosz.tarokk.client.util;

import lombok.Getter;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Getter
public class InternalData {

    private static final String INTERNAL_DATA_FILE_NAME = "internalData.json";
    private static final Path PATH = Paths.get(Constants.INTERNAL_DATA_DIRECTORY, INTERNAL_DATA_FILE_NAME);

    public static InternalData INSTANCE;

    static {
        try {
            INSTANCE = Util.readData(PATH, InternalData.class);
            if (INSTANCE == null) {
                INSTANCE = new InternalData();
                Util.writeData(PATH, INSTANCE);
            }
        } catch (Exception e) {
            INSTANCE = new InternalData();
            Util.Log.logError("Cannot read " + INTERNAL_DATA_FILE_NAME);
            e.printStackTrace(); //TODO print to logfile
        }
    }

    private String faceBookToken = "";
}
