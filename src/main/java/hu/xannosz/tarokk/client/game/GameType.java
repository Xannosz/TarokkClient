package hu.xannosz.tarokk.client.game;

import hu.xannosz.tarokk.client.util.translator.Translator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GameType {
    PASKIEVICS("paskievics", Translator.INST.paskievics),
    ILLUSZTRALT("illusztralt", Translator.INST.illustrated),
    MAGAS("magas", Translator.INST.high),
    ZEBI("zebi", Translator.INST.zebi);

    private final String id;
    private final String name;
}
