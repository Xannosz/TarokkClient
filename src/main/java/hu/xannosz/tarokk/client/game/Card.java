package hu.xannosz.tarokk.client.game;

import com.googlecode.lanterna.Symbols;
import hu.xannosz.tarokk.client.util.Translator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Card {
    I("t1", Translator.INST.cardTI, true, 22), II("t2", Translator.INST.cardTII, true, 21),
    III("t3", "III", true, 20), IV("t4", "IV", true, 19),
    V("t5", "V", true, 18), VI("t6", "VI", true, 17),
    VII("t7", "VII", true, 16), VIII("t8", "VIII", true, 15),
    IX("t9", "IX", true, 14), X("t10", "X", true, 13),
    XI("t11", "XI", true, 12), XII("t12", "XII", true, 11),
    XIII("t13", "XIII", true, 10), XIV("t14", "XIV", true, 9),
    XV("t15", "XV", true, 8), XVI("t16", "XVI", true, 7),
    XVII("t17", "XVII", true, 6), XVIII("t18", "XVIII", true, 5),
    XIX("t19", "XIX", true, 4), XX("t20", "XX", true, 3),
    XXI("t21", Translator.INST.cardTXXI, true, 2), XXII("t22", Translator.INST.cardTXXII, true, 1),

    HEART_I("a1", Symbols.HEART + Translator.INST.cardA, false, 31),
    HEART_II("a2", Symbols.HEART + Translator.INST.cardS, false, 32),
    HEART_III("a3", Symbols.HEART + Translator.INST.cardH, false, 33),
    HEART_IV("a4", Symbols.HEART + Translator.INST.cardD, false, 34),
    HEART_V("a5", Symbols.HEART + Translator.INST.cardK, false, 35),

    DIAMOND_I("b1", Symbols.DIAMOND + Translator.INST.cardA, false, 41),
    DIAMOND_II("b2", Symbols.DIAMOND + Translator.INST.cardS, false, 42),
    DIAMOND_III("b3", Symbols.DIAMOND + Translator.INST.cardH, false, 43),
    DIAMOND_IV("b4", Symbols.DIAMOND + Translator.INST.cardD, false, 44),
    DIAMOND_V("b5", Symbols.DIAMOND + Translator.INST.cardK, false, 45),

    SPADES_I("c1", Symbols.SPADES + Translator.INST.cardA, false, 51),
    SPADES_II("c2", Symbols.SPADES + Translator.INST.cardS, false, 52),
    SPADES_III("c3", Symbols.SPADES + Translator.INST.cardH, false, 53),
    SPADES_IV("c4", Symbols.SPADES + Translator.INST.cardD, false, 54),
    SPADES_V("c5", Symbols.SPADES + Translator.INST.cardK, false, 55),

    CLUB_I("d1", Symbols.CLUB + Translator.INST.cardA, false, 61),
    CLUB_II("d2", Symbols.CLUB + Translator.INST.cardS, false, 62),
    CLUB_III("d3", Symbols.CLUB + Translator.INST.cardH, false, 63),
    CLUB_IV("d4", Symbols.CLUB + Translator.INST.cardD, false, 64),
    CLUB_V("d5", Symbols.CLUB + Translator.INST.cardK, false, 65);

    private final String id;
    private final String formattedName;
    private final boolean isTarock;
    private final int order;

    public static Card parseCard(String id) {
        for (Card card : values()) {
            if (card.getId().equals(id)) {
                return card;
            }
        }
        return null;
    }
}
