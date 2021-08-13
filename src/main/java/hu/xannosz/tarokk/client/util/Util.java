package hu.xannosz.tarokk.client.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.tisza.tarock.proto.MainProto;
import hu.xannosz.tarokk.client.android.legacy.MessageHandler;
import hu.xannosz.tarokk.client.android.legacy.ProtoConnection;
import hu.xannosz.tarokk.client.game.Card;
import hu.xannosz.tarokk.client.tui.KeyMapDictionary;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.util.settings.LogSettings;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileUtils;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Function;

import static hu.xannosz.tarokk.client.util.Constants.LOG_DIRECTORY;

@UtilityClass
public class Util {
    public static ProtoConnection createProtoConnection(MessageHandler messageHandler) throws IOException {
        SSLSocket socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket();
        socket.connect(new InetSocketAddress(Constants.ADDRESS, Constants.PORT), Constants.TIME_OUT);

        Executor executor = Executors.newFixedThreadPool(Constants.THREADS);

        ProtoConnection connection = new ProtoConnection(socket, executor);
        connection.addMessageHandler(messageHandler);
        connection.start();

        return connection;
    }

    public static Screen createScreen() throws IOException {
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        return new TerminalScreen(terminal);
    }

    public static Panel formatActions(KeyMapDictionary dictionary) {
        Panel panel = new Panel();
        panel.setTheme(ThemeHandler.getFooterPanelTheme());

        for (Map.Entry<String, String> entry : dictionary.getFunctionNames().entrySet()) {
            Panel tag = new Panel();
            tag.setLayoutManager(new GridLayout(3));
            tag.addComponent(new Label("["));
            tag.addComponent(new Label(entry.getKey()).setTheme(ThemeHandler.getKeyThemeFooterPanel()));
            tag.addComponent(new Label("]: " + entry.getValue()));
            panel.addComponent(tag);
        }

        return panel;
    }

    public static String getPlayerName(int playerId, MainProto.GameSession gameData, TuiClient tuiClient) {
        if (gameData.getUserIdCount() <= playerId) {
            return "";
        }
        MainProto.User user = tuiClient.getServerLiveData().getUsers().get(gameData.getUserId(playerId));
        if (user.getBot()) {
            return "Bot " + user.getName().replace("bot", "");
        } else {
            return user.getName();
        }
    }

    public static String getFormattedCardName(String card) {
        Card cardObj = Card.parseCard(card);
        return cardObj == null ? "" : cardObj.getFormattedName();
    }

    public static void addData(Panel data, String name, String value) {
        data.addComponent(new Label(name + ":"));
        data.addComponent(new Label(value).setTheme(ThemeHandler.getHighLightedThemeMainPanel()));
    }

    public static void addKey(Panel footer, String key, String name) {
        footer.addComponent(new Label("["));
        footer.addComponent(new Label(key).setTheme(ThemeHandler.getKeyThemeFooterPanel()));
        footer.addComponent(new Label("]: " + name));
    }

    public static void addKeyWithCardToPanel(Panel panel, String key, Card card) {
        panel.addComponent(new Label(key + ":"));
        panel.addComponent(new Label(card.getFormattedName()).setTheme(ThemeHandler.getHighLightedThemeMainPanel()));
    }

    public static Component createUserPanel(int userId, TuiClient tuiClient) {
        if (userId != 0) {
            MainProto.User user = tuiClient.getServerLiveData().getUsers().get(userId);
            if (user.getBot()) {
                return new Label(" Bot").setTheme(ThemeHandler.getSubLightedThemeMainPanel());
            } else {
                Panel panel = new Panel(new GridLayout(2));
                panel.addComponent(new Label(user.getName()));
                if (user.getOnline()) {
                    panel.addComponent(new Label("" + Symbols.TRIANGLE_UP_POINTING_BLACK).setTheme(ThemeHandler.getOnlineColorThemeMainPanel()));
                } else {
                    panel.addComponent(new Label("" + Symbols.TRIANGLE_DOWN_POINTING_BLACK).setTheme(ThemeHandler.getSubLightedThemeMainPanel()));
                }
                return panel;
            }
        } else {
            return new Label("");
        }
    }

    public static MainProto.GameSession getGameData(int id, TuiClient tuiClient) {
        MainProto.GameSession result = null;
        for (MainProto.GameSession gameData : tuiClient.getServerLiveData().getGameSessions()) {
            if (id == gameData.getId()) {
                result = gameData;
            }
        }
        return result;
    }

    public static boolean anyNull(Object... objects) {
        for (Object obj : objects) {
            if (obj == null) {
                return true;
            }
        }
        return false;
    }

    public static <IN, OUT> List<OUT> map(List<IN> list, Function<IN, OUT> f) {
        List<OUT> result = new ArrayList<>();
        for (IN t : list)
            result.add(f.apply(t));
        return result;
    }

    public static <T> T readData(Path path, Class<T> clazz) throws IOException {
        path.toFile().getParentFile().mkdirs();
        path.toFile().createNewFile();
        JsonElement dataObject = JsonParser.parseString(FileUtils.readFileToString(path.toFile()));
        return new Gson().fromJson(dataObject, clazz);
    }

    public static <T> void writeData(Path path, T data) throws IOException {
        path.toFile().getParentFile().mkdirs();
        path.toFile().createNewFile();
        FileUtils.writeStringToFile(path.toFile(), new GsonBuilder().setPrettyPrinting().create().toJson(data));
    }

    @UtilityClass
    public static class Log {
        private static final String MESSAGE_LOG_FILE = "message_log.txt";
        private static final String GAME_LOG_FILE = "game_log.txt";
        private static final String KEY_LOG_FILE = "key_log.txt";
        private static final String ERROR_LOG_FILE = "error_log.txt";
        private static final String COMBINED_LOG_FILE = "combined_log.txt";
        private static final int GAME_LOG_LENGTH = 30;

        private static final String DIRECTORY_INFIX = new SimpleDateFormat("yyyyy.MM.dd_hh.mm.ss").format(new Date());

        static {
            boolean logSetup = true;
            if (!Paths.get(LOG_DIRECTORY).toFile().exists()) {
                logSetup = Paths.get(LOG_DIRECTORY).toFile().mkdir();
            }
            if (!Paths.get(LOG_DIRECTORY, DIRECTORY_INFIX).toFile().exists()) {
                logSetup &= Paths.get(LOG_DIRECTORY, DIRECTORY_INFIX).toFile().mkdir();
            }

            try {
                if (!Files.exists(Paths.get(LOG_DIRECTORY, DIRECTORY_INFIX, MESSAGE_LOG_FILE))) {
                    Files.createFile(Paths.get(LOG_DIRECTORY, DIRECTORY_INFIX, MESSAGE_LOG_FILE));
                }
                if (!Files.exists(Paths.get(LOG_DIRECTORY, DIRECTORY_INFIX, GAME_LOG_FILE))) {
                    Files.createFile(Paths.get(LOG_DIRECTORY, DIRECTORY_INFIX, GAME_LOG_FILE));
                }
                if (!Files.exists(Paths.get(LOG_DIRECTORY, DIRECTORY_INFIX, KEY_LOG_FILE))) {
                    Files.createFile(Paths.get(LOG_DIRECTORY, DIRECTORY_INFIX, KEY_LOG_FILE));
                }
                if (!Files.exists(Paths.get(LOG_DIRECTORY, DIRECTORY_INFIX, ERROR_LOG_FILE))) {
                    Files.createFile(Paths.get(LOG_DIRECTORY, DIRECTORY_INFIX, ERROR_LOG_FILE));
                }
                if (!Files.exists(Paths.get(LOG_DIRECTORY, DIRECTORY_INFIX, COMBINED_LOG_FILE))) {
                    Files.createFile(Paths.get(LOG_DIRECTORY, DIRECTORY_INFIX, COMBINED_LOG_FILE));
                }
            } catch (IOException e) {
                logSetup = false;
            }

            if (!logSetup) {
                logToConsole(Constants.Color.ANSI_RED, "Logging service not started.");
            }
        }

        private static void logToConsole(String color, String message) {
            System.out.println(color + message + Constants.Color.ANSI_RESET);
        }

        private static void logToFile(String line, String fileName) {
            try {
                Files.write(Paths.get(LOG_DIRECTORY, DIRECTORY_INFIX, fileName), (line + "\n").getBytes(), StandardOpenOption.APPEND);
                Files.write(Paths.get(LOG_DIRECTORY, DIRECTORY_INFIX, COMBINED_LOG_FILE), (line + "\n").getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                //Unexpected
            }
        }

        public static void logMessage(String log) {
            if (LogSettings.INSTANCE.isMessageLog()) {
                logToConsole(Constants.Color.ANSI_GREEN, log);
            }
            logToFile(log, MESSAGE_LOG_FILE);
        }

        public static void logGame(String log) {
            if (LogSettings.INSTANCE.isGameLog()) {
                logToConsole(Constants.Color.ANSI_BLUE, log);
            }
            logToFile(log, GAME_LOG_FILE);
        }

        public static void logKey(String log) {
            if (LogSettings.INSTANCE.isKeyLog()) {
                logToConsole(Constants.Color.ANSI_CYAN, log);
            }
            logToFile(log, KEY_LOG_FILE);
        }

        public static void logError(String log) {
            logToConsole(Constants.Color.ANSI_RED, log);
            logToFile(log, ERROR_LOG_FILE);
        }

        public static void logError(Exception ex) {
            ex.printStackTrace();
            try {
                ex.printStackTrace(new PrintWriter(new FileWriter(Paths.get(LOG_DIRECTORY, DIRECTORY_INFIX, ERROR_LOG_FILE).toString(), true)));
                ex.printStackTrace(new PrintWriter(new FileWriter(Paths.get(LOG_DIRECTORY, DIRECTORY_INFIX, COMBINED_LOG_FILE).toString(), true)));
            } catch (IOException e) {
                //Unexpected
            }
            logError("\n\n");
        }

        public static void formattedGameLog(int id, String log) {
            logGame(String.format("%" + (id * GAME_LOG_LENGTH) + "s", "") + log.substring(0, GAME_LOG_LENGTH));
        }
    }
}
