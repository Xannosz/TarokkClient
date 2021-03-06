package hu.xannosz.tarokk.client.util.translator;

import hu.xannosz.microtools.Json;
import hu.xannosz.tarokk.client.util.Constants;
import hu.xannosz.tarokk.client.util.InternalData;
import hu.xannosz.tarokk.client.util.Util;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Translator {

    public static Translator INST;

    static {
        Path path = Paths.get(Constants.LANG_DIRECTORY, InternalData.INSTANCE.getLangId());
        try {
            INST = Json.readData(path, Translator.class);
            if (INST == null) {
                INST = new Translator();
            }
            Json.writeData(path, INST);
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

    public String kontra = "kontra";
    public String rekontra = "rekontra";
    public String subkontra = "subkontra";
    public String hirshkontra = "hirshkontra";
    public String mordkontra = "mordkontra";
    public String fedakSari = "fedakSari";

    public String pheasant = "pheasant";
    public String centrum = "centrum";
    public String littleBird = "littleBird";
    public String bigBird = "bigBird";
    public String uhu = "uhu";
    public String ultimo = "ultimo";

    public AnnouncementNameTexts announcementNameTexts = new AnnouncementNameTexts();

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
