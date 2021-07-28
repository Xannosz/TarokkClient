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
import hu.xannosz.tarokk.client.tui.KeyMapDictionary;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
