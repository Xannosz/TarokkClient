package hu.xannosz.tarokk.client.game;

import hu.xannosz.tarokk.client.util.Translator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DoubleRoundType {
    NONE("none", Translator.INST.none),
    PECULATING("peculating", Translator.INST.peculating),
    STACKING("stacking", Translator.INST.stacking),
    MULTIPLYING("multiplying", Translator.INST.multiplying);

    private final String id;
    private final String name;
}
