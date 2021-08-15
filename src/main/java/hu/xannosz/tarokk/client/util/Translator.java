package hu.xannosz.tarokk.client.util;

import com.google.gson.annotations.Expose;
import com.googlecode.lanterna.Symbols;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Translator {

    public static Translator INST;

    static {
        Path path = Paths.get(Constants.LANG_DIRECTORY, InternalData.INSTANCE.getLangId());
        try {
            INST = Util.readData(path, Translator.class);
            if (INST == null) {
                INST = new Translator();
            }
            Util.writeData(path, INST);
        } catch (Exception e) {
            INST = new Translator();
            Util.Log.logError("Cannot read " + InternalData.INSTANCE.getLangId());
            Util.Log.logError(e);
        }
    }

    // game session panel
    public String type = "type";
    public String status = "status";

    // user panel
    public String online = "online";
    public String offline = "offline";
    public String friend = "friend";
    public String notFriend = "notFriend";

    // game frame
    public String cards = "cards";
    public String data = "data";
    public String lobby = "lobby";
    public String hud = "hud";
    public String backToLobby = "backToLobby";

    // tui client
    public String userId = "userId";
    public String userName = "userName";
    public String loginNotSucceed = "loginNotSucceed";
    public String notLoggedIn = "notLoggedIn";
    public String quit = "quit";

    // new game frame
    public String gameType = "gameType";
    public String doubleRoundType = "doubleRoundType";

    // lobby frame
    public String joinToLobby = "joinToLobby";
    public String joinToGame = "joinToGame";
    public String joinToGameAsObserver = "joinToGameAsObserver";
    public String deleteGame = "deleteGame";

    // common
    public String arrows = "arrows";
    public String movement = "movement";
    public String createGame = "createGame";
    public String cancel = "cancel";

    // data meta panel
    public String beginnerPlayer = "beginnerPlayer";
    public String phase = "phase";
    public String playerInTurn = "playerInTurn";
    public String foldedTarock = "foldedTarock";
    public String calledTarock = "calledTarock";
    public String takenCards = "takenCards";

    // hud meta panel
    public String isCaller = "isCaller";

    // statistic meta panel
    public String callerGamePoints = "callerGamePoints";
    public String opponentGamePoints = "opponentGamePoints";
    public String sumPoints = "sumPoints";
    public String pointMultiplier = "pointMultiplier";
    public String announcement = "announcement";
    public String points = "points";
    public String callerTeam = "callerTeam";

    // start game sub frame
    public String startGame = "startGame";

    // announcing sub frame
    public String announce = "announce";
    public String sendAnnouncing = "sendAnnouncing";
    public String pass = "pass";

    // bidding sub frame
    public String hold = "hold";
    public String three = "three";
    public String two = "two";
    public String one = "one";
    public String solo = "solo";
    public String invalid = "invalid";

    // calling sub frame
    public String sendCalling = "sendCalling";

    // end sub frame
    public String incrementedPoints = "incrementedPoints";
    public String newGame = "newGame";

    // game play sub frame
    public String playCard = "playCard";

    // folding sub frame
    public String foldDone = "foldDone";
    public String foldedCards = "foldedCards";
    public String resetFolding = "resetFolding";
    public String sendFolding = "sendFolding";

    // double round type
    public String none = "none";
    public String peculating = "peculating";
    public String stacking = "stacking";
    public String multiplying = "multiplying";

    // game phase
    public String bidding = "bidding";
    public String folding = "folding";
    public String calling = "calling";
    public String announcing = "announcing";
    public String gameplay = "gameplay";
    public String end = "end";

    // game type
    public String paskievics = "paskievics";
    public String illustrated = "illustrated";
    public String high = "high";
    public String zebi = "zebi";

    // tarock cards
    public String cardTI = "cardTI";
    public String cardTII = "cardTII";
    public String cardTXXI = "cardTXXI";
    public String cardTXXII = "cardTXXII";

    // figure cards
    public String cardA = "cardA";
    public String cardS = "cardS";
    public String cardH = "cardH";
    public String cardD = "cardD";
    public String cardK = "cardK";

    // announcing
    public String silent = "silent";

    public String getContraName(int id) {
        if (id < 0 || id >= contraNames.size()) {
            return "" + id;
        }
        return contraNames.get(id);
    }

    public String getSuitName(int id) {
        if (id < 0 || id >= suitNames.size()) {
            return "" + id;
        }
        return suitNames.get(id);
    }

    public String getTrickName(int id) {
        if (id < 0 || id >= trickNames.size()) {
            return "" + id;
        }
        return trickNames.get(id);
    }

    public String getAnnouncementNameText(String key) {
        Class<?> objectClass = announcementNameTexts.getClass();
        for (Field field : objectClass.getFields()) {
            if (field.getName().equals(key)) {
                try {
                    return (String) field.get(announcementNameTexts);
                } catch (Exception e) {
                    Util.Log.logError("Get announcementNameText failed.");
                    Util.Log.logError(e);
                    return key;
                }
            }
        }
        Util.Log.logError("AnnouncementNameText not exist.");
        return key;
    }

    private String kontra = "kontra";
    private String rekontra = "rekontra";
    private String subkontra = "subkontra";
    private String hirshkontra = "hirshkontra";
    private String mordkontra = "mordkontra";
    private String fedakSari = "fedakSari";
    private final List<String> contraNames = Arrays.asList("", kontra, rekontra, subkontra, hirshkontra, mordkontra, fedakSari);

    private final List<String> suitNames = Arrays.asList("" + Symbols.HEART, "" + Symbols.DIAMOND, "" + Symbols.SPADES, "" + Symbols.CLUB);

    private String pheasant = "pheasant";
    private String centrum = "centrum";
    private String littleBird = "littleBird";
    private String bigBird = "bigBird";
    private String uhu = "uhu";
    private String ultimo = "ultimo";
    private final List<String> trickNames = Arrays.asList(pheasant, "", "", "", centrum, littleBird, bigBird, uhu, ultimo);

    private AnnouncementNameTexts announcementNameTexts = new AnnouncementNameTexts();

    public static class AnnouncementNameTexts {
        public String jatek = "jatek";
        public String hkp = "hkp";
        public String nyolctarokk = "nyolctarokk";
        public String kilenctarokk = "kilenctarokk";
        public String trull = "trull";
        public String negykiraly = "negykiraly";
        public String banda = "banda";
        public String dupla = "dupla";
        public String hosszudupla = "hosszudupla";
        public String kezbevacak = "kezbevacak";
        public String szinesites = "szinesites";
        public String volat = "volat";
        public String kiralyultimo = "kiralyultimo";
        public String ketkiralyok = "ketkiralyok";
        public String haromkiralyok = "haromkiralyok";
        public String zaroparos = "zaroparos";
        public String xxifogas = "xxifogas";
        public String parosfacan = "parosfacan";
        public String ultimo = "ultimo";
        public String kisszincsalad = "kisszincsalad";
        public String nagyszincsalad = "nagyszincsalad";
    }
}
