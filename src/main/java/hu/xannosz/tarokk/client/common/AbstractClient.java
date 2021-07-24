package hu.xannosz.tarokk.client.common;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.tisza.tarock.proto.MainProto;
import hu.xannosz.tarokk.client.android.network.MessageHandler;
import hu.xannosz.tarokk.client.android.network.ProtoConnection;
import hu.xannosz.tarokk.client.util.Constants;
import hu.xannosz.tarokk.client.util.MessageTranslator;
import hu.xannosz.tarokk.client.util.Util;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractClient implements MessageHandler, WindowListener {

    private Screen screen;
    private final BasicWindow window = new BasicWindow();

    protected ProtoConnection connection;
    protected final KeyMapDictionary dictionary = new KeyMapDictionary();
    protected final TerminalSettings terminalSettings;

    public AbstractClient(TerminalSettings terminalSettings) {
        this.terminalSettings = terminalSettings;
        try {
            // start screen
            screen = Util.createScreen();
            screen.startScreen();

            // init window
            window.addWindowListener(this);
            window.setHints(Arrays.asList(Window.Hint.NO_DECORATIONS, Window.Hint.FULL_SCREEN));

            // start proto
            connection = Util.createProtoConnection(this);
            connection.sendMessage(MessageTranslator.fbLogin(Constants.FB_TOKEN));

            // init gui
            MultiWindowTextGUI gui = new MultiWindowTextGUI(screen);
            gui.setTheme(new SimpleTheme(terminalSettings.getForeGround(), terminalSettings.getBackGround()));
            gui.addWindowAndWait(window);
        } catch (Exception ex) {
            Util.error("Error client creation: " + ex);
        }
    }

    protected abstract void createActionsList(MainProto.Message message);

    protected abstract Panel drawWindow(MainProto.Message message);

    @Override
    public void onInput(Window basePane, KeyStroke keyStroke, AtomicBoolean deliverEvent) {
        Util.debug("Pressed: " + keyStroke);
        try {
            if (keyStroke.getKeyType() == KeyType.Escape) {
                connection.close();
                screen.stopScreen();
                window.close();
            }
            if (keyStroke.getKeyType() == KeyType.Character) {
                dictionary.runFunction(keyStroke.getCharacter().toString());
            }
        } catch (Exception ex) {
            Util.error("Error during execution: " + ex);
        }
    }

    @Override
    public void onUnhandledInput(Window basePane, KeyStroke keyStroke, AtomicBoolean hasBeenHandled) {
        // Not used function
    }

    @Override
    public void onResized(Window window, TerminalSize oldSize, TerminalSize newSize) {
        // Not used function
    }

    @Override
    public void onMoved(Window window, TerminalPosition oldPosition, TerminalPosition newPosition) {
        // Not used function
    }

    @Override
    public void handleMessage(MainProto.Message message) {
        Util.debug("Message: " + message);
        createActionsList(message);
        window.setComponent(drawWindow(message));
    }

    @Override
    public void connectionError(ErrorType errorType) {
        Util.error("Error: " + errorType);
    }

    @Override
    public void connectionClosed() {
        Util.info("Connection closed!");
    }
}
