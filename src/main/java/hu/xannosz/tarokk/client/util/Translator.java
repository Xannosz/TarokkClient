package hu.xannosz.tarokk.client.util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Translator {

    public static Translator INSTANCE;

    static {
        Path path = Paths.get(Constants.LANG_DIRECTORY, InternalData.INSTANCE.getLangId());
        try {
            INSTANCE = Util.readData(path, Translator.class);
            if (INSTANCE == null) {
                INSTANCE = new Translator();
                Util.writeData(path, INSTANCE);
            }
        } catch (Exception e) {
            INSTANCE = new Translator();
            Util.Log.logError("Cannot read " + InternalData.INSTANCE.getLangId());
            e.printStackTrace(); //TODO print to logfile
        }
    }

    private final Map<String, String> data = new HashMap<>();

    public String translate(String key) {
        if (data.containsKey(key)) {
            return data.get(key);
        }
        Util.Log.logError(key + " key missing in lang file " + InternalData.INSTANCE.getLangId());
        return key;
    }
}
