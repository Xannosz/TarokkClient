package hu.xannosz.tarokk.client.util;

import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import hu.xannosz.tarokk.client.android.network.MessageHandler;
import hu.xannosz.tarokk.client.android.network.ProtoConnection;
import hu.xannosz.tarokk.client.game.Card;
import hu.xannosz.tarokk.client.tui.KeyMapDictionary;
import hu.xannosz.tarokk.client.tui.TuiClient;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Util {

    public static final String LOG_FILE_PATH = "log.txt";

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

    public static Panel formatActions(TerminalSettings terminalSettings, KeyMapDictionary dictionary) {
        Panel panel = new Panel();
        panel.setTheme(ThemeHandler.getFooterPanelTheme(terminalSettings));

        for (Map.Entry<String, String> entry : dictionary.getFunctionNames().entrySet()) {
            Panel tag = new Panel();
            tag.setLayoutManager(new GridLayout(3));
            tag.addComponent(new Label("["));
            tag.addComponent(new Label(entry.getKey()).setTheme(ThemeHandler.getKeyThemeFooterPanel(terminalSettings)));
            tag.addComponent(new Label("]: " + entry.getValue()));
            panel.addComponent(tag);
        }

        return panel;
    }

    public static String getPlayerName(int playerId) {//TODO
        return "player:" + playerId;
    }

    public static String getFormattedCardName(String card) {
        return Card.parseCard(card).getFormattedName();
    }

    public static void addData(Panel data, String name, String value, TuiClient tuiClient) {
        data.addComponent(new Label(name + ":"));
        data.addComponent(new Label(value).setTheme(ThemeHandler.getHighLightedThemeMainPanel(tuiClient.getTerminalSettings())));
    }

    public static void addKey(Panel footer, String key, String name, TuiClient tuiClient) {
        footer.addComponent(new Label("["));
        footer.addComponent(new Label(key).setTheme(ThemeHandler.getKeyThemeFooterPanel(tuiClient.getTerminalSettings())));
        footer.addComponent(new Label("]: "+name));
    }

    public static void addKeyWithCardToPanel(Panel panel, String key, String cardId, TuiClient tuiClient) {
        panel.addComponent(new Label(key + ":"));
        panel.addComponent(new Label(getFormattedCardName(cardId)).setTheme(ThemeHandler.getHighLightedThemeMainPanel(tuiClient.getTerminalSettings())));
    }

    public static boolean anyNull(Object... objects) {
        for (Object obj : objects) {
            if (obj == null) {
                return true;
            }
        }
        return false;
    }

    public static void log(String color, String message) {
        System.out.println(color + message + Constants.Color.ANSI_RESET);
        try {
            if (!Files.exists(Paths.get(LOG_FILE_PATH))) {
                Files.createFile(Paths.get(LOG_FILE_PATH));
            }
            Files.write(Paths.get(LOG_FILE_PATH), (message+"\n").getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            //Unexpected
        }
    }

    public static void log(String message) {
        log("", message);
    }

    public static void error(String message) {
        log(Constants.Color.ANSI_RED, message);
    }

    public static void info(String message) {
        log(Constants.Color.ANSI_BLUE, message);
    }

    public static void debug(String message) {
        log(Constants.Color.ANSI_CYAN, message);
    }
}
