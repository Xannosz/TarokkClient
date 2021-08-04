package hu.xannosz.tarokk.client.game;

import com.googlecode.lanterna.Symbols;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Card {
    I("t1", "PAGAT",true), II("t2", "II",true),
    III("t3", "III",true), IV("t4", "IV",true),
    V("t5", "V",true), VI("t6", "VI",true),
    VII("t7", "VII",true), VIII("t8", "VIII",true),
    IX("t9", "IX",true), X("t10", "X",true),
    XI("t11", "XI",true), XII("t12", "XII",true),
    XIII("t13", "XIII",true), XIV("t14", "XIV",true),
    XV("t15", "XV",true), XVI("t16", "XVI",true),
    XVII("t17", "XVII",true), XVIII("t18", "XVIII",true),
    XIX("t19", "XIX",true), XX("t20", "XX",true),
    XXI("t21", "XXI",true), XXII("t22", "SKIZ",true),

    HEART_I("a1", Symbols.HEART + "A",false),
    HEART_II("a2", Symbols.HEART + "S",false),
    HEART_III("a3", Symbols.HEART + "H",false),
    HEART_IV("a4", Symbols.HEART + "D",false),
    HEART_V("a5", Symbols.HEART + "K",false),

    DIAMOND_I("b1", Symbols.DIAMOND + "A",false),
    DIAMOND_II("b2", Symbols.DIAMOND + "S",false),
    DIAMOND_III("b3", Symbols.DIAMOND + "H",false),
    DIAMOND_IV("b4", Symbols.DIAMOND + "D",false),
    DIAMOND_V("b5", Symbols.DIAMOND + "K",false),

    SPADES_I("c1", Symbols.SPADES + "A",false),
    SPADES_II("c2", Symbols.SPADES + "S",false),
    SPADES_III("c3", Symbols.SPADES + "H",false),
    SPADES_IV("c4", Symbols.SPADES + "D",false),
    SPADES_V("c5", Symbols.SPADES + "K",false),

    CLUB_I("d1", Symbols.CLUB + "A",false),
    CLUB_II("d2", Symbols.CLUB + "S",false),
    CLUB_III("d3", Symbols.CLUB + "H",false),
    CLUB_IV("d4", Symbols.CLUB + "D",false),
    CLUB_V("d5", Symbols.CLUB + "K",false);

    private final String id;
    private final String formattedName;
    private final boolean isTarock;

    public static Card parseCard(String id) {
        for (Card card : values()) {
            if (card.getId().equals(id)) {
                return card;
            }
        }
        return null;
    }
}
