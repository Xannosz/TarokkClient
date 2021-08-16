package hu.xannosz.tarokk.client.game;

import hu.xannosz.tarokk.client.util.translator.Translator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GamePhase {
    BIDDING("bidding", Translator.INST.bidding),
    FOLDING("folding", Translator.INST.folding),
    CALLING("calling", Translator.INST.calling),
    ANNOUNCING("announcing", Translator.INST.announcing),
    GAMEPLAY("gameplay", Translator.INST.gameplay),
    END("end", Translator.INST.end);

    private final String id;
    private final String name;

    public static GamePhase getPhase(String phase) {
        for (GamePhase p : values()) {
            if (p.getId().equals(phase)) {
                return p;
            }
        }
        return null;
    }
}
