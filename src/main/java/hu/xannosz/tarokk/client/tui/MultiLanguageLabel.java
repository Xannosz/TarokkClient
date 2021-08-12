package hu.xannosz.tarokk.client.tui;

import com.googlecode.lanterna.gui2.Label;
import hu.xannosz.tarokk.client.util.Translator;
import hu.xannosz.tarokk.client.util.Util;

import java.util.Arrays;

public class MultiLanguageLabel extends Label {
    public MultiLanguageLabel(String key) {
        super(Translator.INSTANCE.translate(key));
    }

    public MultiLanguageLabel(String formatterKey, String... dataKeys) {
        super(String.format(Translator.INSTANCE.translate(formatterKey), Util.map(Arrays.asList(dataKeys), Translator.INSTANCE::translate).toArray(new String[0])));
    }
}
