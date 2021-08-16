package hu.xannosz.tarokk.client.util.translator;

import com.googlecode.lanterna.Symbols;
import hu.xannosz.tarokk.client.util.Util;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public class TranslatorUtil {

    private static final List<String> CONTRA_NAMES = Arrays.asList("", Translator.INST.kontra, Translator.INST.rekontra, Translator.INST.subkontra, Translator.INST.hirshkontra, Translator.INST.mordkontra, Translator.INST.fedakSari);
    private static final List<String> SUIT_NAMES = Arrays.asList("" + Symbols.HEART, "" + Symbols.DIAMOND, "" + Symbols.SPADES, "" + Symbols.CLUB);
    private static final List<String> TRICK_NAMES = Arrays.asList(Translator.INST.pheasant, "", "", "", Translator.INST.centrum, Translator.INST.littleBird, Translator.INST.bigBird, Translator.INST.uhu, Translator.INST.ultimo);

    public static String getContraName(int id) {
        if (id < 0 || id >= CONTRA_NAMES.size()) {
            return "" + id;
        }
        return CONTRA_NAMES.get(id);
    }

    public static String getSuitName(int id) {
        if (id < 0 || id >= SUIT_NAMES.size()) {
            return "" + id;
        }
        return SUIT_NAMES.get(id);
    }

    public static String getTrickName(int id) {
        if (id < 0 || id >= TRICK_NAMES.size()) {
            return "" + id;
        }
        return TRICK_NAMES.get(id);
    }

    public static String getAnnouncementNameText(String key) {
        Class<?> objectClass = Translator.INST.announcementNameTexts.getClass();
        for (Field field : objectClass.getFields()) {
            if (field.getName().equals(key)) {
                try {
                    return (String) field.get(Translator.INST.announcementNameTexts);
                } catch (Exception e) {
                    Util.Log.logError("Get announcementNameText failed.");
                    Util.Log.logError(e);
                    return key;
                }
            }
        }
        Util.Log.logError("AnnouncementNameText not exist.");
        return key;
    }
}
