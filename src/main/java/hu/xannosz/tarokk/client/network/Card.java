package hu.xannosz.tarokk.client.network;

import com.googlecode.lanterna.Symbols;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Card {
    I("t1", "I"), II("t2", "II"), III("t3", "III"),
    IV("t4", "IV"), V("t5", "V"), VI("t6", "VI"),
    VII("t7", "VII"), VIII("t8", "VIII"), IX("t9", "IX"),
    X("t10", "X"), XI("t11", "XI"), XII("t12", "XII"),
    XIII("t13", "XIII"), XIV("t14", "XIV"), XV("t15", "XV"),
    XVI("t16", "XVI"), XVII("t17", "XVII"), XVIII("t18", "XVIII"),
    XIX("t19", "XIX"), XX("t20", "XX"), XXI("t21", "XXI"),
    XXII("t22", "SKIZ"),

    DIAMOND_I("i", Symbols.DIAMOND + "A"), DIAMOND_II("i", Symbols.DIAMOND + "S"),
    DIAMOND_III("i", Symbols.DIAMOND + "H"), DIAMOND_IV("i", Symbols.DIAMOND + "D"),
    DIAMOND_V("i", Symbols.DIAMOND + "K"),

    SPADES_I("i", Symbols.SPADES + "A"), SPADES_II("i", Symbols.SPADES + "S"),
    SPADES_III("i", Symbols.SPADES + "H"), SPADES_IV("i", Symbols.SPADES + "D"),
    SPADES_V("i", Symbols.SPADES + "K"),

    CLUB_I("i", Symbols.CLUB + "A"), CLUB_II("i", Symbols.CLUB + "S"),
    CLUB_III("i", Symbols.CLUB + "H"), CLUB_IV("i", Symbols.CLUB + "D"),
    CLUB_V("i", Symbols.CLUB + "K"),

    HEART_I("i", Symbols.HEART + "A"), HEART_II("i", Symbols.HEART + "S"),
    HEART_III("i", Symbols.HEART + "H"), HEART_IV("i", Symbols.HEART + "D"),
    HEART_V("i", Symbols.HEART + "K");

    private final String id;
    private final String formattedName;

    public static Card parseCard(String id) {
        for (Card card : values()) {
            if (card.getId().equals(id)) {
                return card;
            }
        }
        return null;
    }
}
