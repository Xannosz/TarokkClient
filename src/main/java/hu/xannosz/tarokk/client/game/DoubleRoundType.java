package hu.xannosz.tarokk.client.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DoubleRoundType {
    NONE("none"),
    PECULATING("peculating"),
    STACKING("stacking"),
    MULTIPLYING("multiplying");

    private final String name;
}
