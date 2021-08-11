package hu.xannosz.tarokk.client.util.settings;

import hu.xannosz.tarokk.client.util.Constants;
import hu.xannosz.tarokk.client.util.Util;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Getter
public class LogSettings {

    private static final String LOG_SETTINGS_FILE_NAME = "logSettings.json";
    private static final Path PATH = Paths.get(Constants.CONFIG_DIRECTORY, LOG_SETTINGS_FILE_NAME);

    public static LogSettings INSTANCE;

    static {
        try {
            INSTANCE = Util.readData(PATH, LogSettings.class);
            if (INSTANCE == null) {
                INSTANCE = new LogSettings();
                Util.writeData(PATH,INSTANCE);
            }
        } catch (Exception e) {
            INSTANCE = new LogSettings();
            Util.Log.logError("Cannot read " + LOG_SETTINGS_FILE_NAME);
            e.printStackTrace(); //TODO print to logfile
        }
    }

    private boolean messageLog = false;
    private boolean gameLog = false;
    private boolean keyLog = false;
}
