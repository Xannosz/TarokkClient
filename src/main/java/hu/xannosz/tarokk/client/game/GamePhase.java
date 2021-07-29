package hu.xannosz.tarokk.client.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GamePhase {
    BIDDING("bidding"),
    FOLDING("folding"),
    CALLING("calling"),
    ANNOUNCING("announcing"),
    GAMEPLAY("gameplay"),
    END("end");

    private final String name;

    public static GamePhase getPhase(String phase) {
        for (GamePhase p : values()) {
            if (p.getName().equals(phase)) {
                return p;
            }
        }
        return null;
    }
}
