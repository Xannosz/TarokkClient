package hu.xannosz.tarokk.client.game;

import hu.xannosz.tarokk.client.util.Translator;
import lombok.Getter;

@Getter
public class Announcement {

    private final String id;
    private final String name;
    private final int contraLevel;
    private final int suit;
    private final Card card;
    private final int trick;

    private Announcement(String id, String name, int suit, Card card, int trick, int contraLevel) {
        this.id = id;
        this.name = name;
        this.suit = suit;
        this.card = card;
        this.trick = trick;
        this.contraLevel = contraLevel;
    }

    public static Announcement fromID(String id) {
        if (id == null)
            throw new NullPointerException();

        String name;
        int contraLevel = 0;
        int suit = -1;
        Card card = null;
        int trick = -1;

        int pos = 0;

        name = id.substring(pos, pos = nextUppercase(id, pos));

        while (pos < id.length()) {
            char c = id.charAt(pos++);
            String substr = id.substring(pos, pos = nextUppercase(id, pos));

            switch (c) {
                case 'S':
                    suit = parseSuit(substr);
                    break;
                case 'C':
                    card = Card.parseCard(substr);
                    break;
                case 'T':
                    try {
                        trick = Integer.parseInt(substr);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("invalid trick number: " + trick);
                    }
                    break;
                case 'K':
                    contraLevel = substr.equals("s") ? -1 : Integer.parseInt(substr);
                    break;
                default:
                    throw new IllegalArgumentException("invalid announcement modifier: " + c);
            }
        }

        return new Announcement(id, name, suit, card, trick, contraLevel);
    }

    private static int parseSuit(String str) {
        if (str.length() != 1)
            throw new IllegalArgumentException("invalid suit: " + str);

        char c = str.charAt(0);

        if (c < 'a' || c > 'd')
            throw new IllegalArgumentException("invalid suit: " + str);

        return c - 'a';
    }

    private static int nextUppercase(String str, int from) {
        int i;
        for (i = from; i < str.length(); i++)
            if (Character.isUpperCase(str.charAt(i)))
                break;
        return i;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();

        if (isSilent())
            builder.append(Translator.INST.silent);
        else
            builder.append(Translator.INST.getContraName(getContraLevel()));

        if (hasSuit())
            builder.append(Translator.INST.getSuitName(getSuit()));
        if (hasCard())
            builder.append(getCard().getFormattedName());
        if (hasTrick())
            builder.append(Translator.INST.getTrickName(getTrick()));

        String nameText = Translator.INST.getAnnouncementNameText(getName());
        if (nameText == null)
            nameText = "[" + getName() + "]";
        builder.append(nameText);

        return builder.toString();
    }

    public boolean isSilent() {
        return contraLevel < 0;
    }

    public boolean hasSuit() {
        return suit >= 0;
    }

    public boolean hasCard() {
        return card != null;
    }

    public boolean hasTrick() {
        return trick >= 0;
    }
}
