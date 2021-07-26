package hu.xannosz.tarokk.client.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GameType {
    PASKIEVICS("paskievics"),
    ILLUSZTRALT("illusztralt"),
    MAGAS("magas"),
    ZEBI("zebi");

    private final String name;
}
