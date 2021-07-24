package hu.xannosz.tarokk.client.commandline;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.tisza.tarock.proto.MainProto;
import hu.xannosz.tarokk.client.common.AbstractClient;
import hu.xannosz.tarokk.client.common.TerminalSettings;
import hu.xannosz.tarokk.client.util.MessageTranslator;

import java.util.Map;

public class CommandLineClient extends AbstractClient {
    public CommandLineClient(TerminalSettings terminalSettings) {
        super(terminalSettings);
    }

    private Panel formatMessage(MainProto.Message message) {
        Panel panel = new Panel();
        panel.setTheme(new SimpleTheme(terminalSettings.getMessagePanelForeGround(),
                terminalSettings.getMessagePanelBackGround()));
       // panel.addComponent(new Label("" + message));
        return panel;
    }

    @Override
    protected void createActionsList(MainProto.Message message) {
        dictionary.addFunction("1", "List Lobby", () ->
                connection.sendMessage(MessageTranslator.listLobby()));
    }

    @Override
    protected Panel drawWindow(MainProto.Message message) {
        Panel messagePanel = formatMessage(message);
        Panel actionsPanel = formatActions();
        Panel mainPanel = new Panel();
        mainPanel.setLayoutManager(new GridLayout(1));
        mainPanel.addComponent(messagePanel);
        mainPanel.addComponent(actionsPanel);
        return mainPanel;
    }

    private Panel formatActions() {
        Panel panel = new Panel();
        panel.setTheme(new SimpleTheme(terminalSettings.getActionsPanelForeGround(), terminalSettings.getActionsPanelBackGround()));

        for (Map.Entry<String, String> entry : dictionary.getFunctionNames().entrySet()) {
            Panel tag = new Panel();
            tag.setLayoutManager(new GridLayout(3));
            tag.addComponent(new Label("["));
            tag.addComponent(new Label(entry.getKey()).setTheme(new SimpleTheme(terminalSettings.getKeyColor(), terminalSettings.getActionsPanelBackGround())));
            tag.addComponent(new Label("]: " + entry.getValue()));
            panel.addComponent(tag);
        }

        Panel tag = new Panel();
        tag.setLayoutManager(new GridLayout(3));
        tag.addComponent(new Label("["));
        tag.addComponent(new Label("Escape").setTheme(new SimpleTheme(terminalSettings.getKeyColor(), terminalSettings.getActionsPanelBackGround())));
        tag.addComponent(new Label("]: Quit"));
        panel.addComponent(tag);

        return panel;
    }
}
