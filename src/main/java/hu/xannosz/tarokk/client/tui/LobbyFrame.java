package hu.xannosz.tarokk.client.tui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.tisza.tarock.proto.MainProto;

import java.util.Map;

public class LobbyFrame extends Frame {

    private Panel frame = new Panel();
    private int gamePage = 0;
    private int userPage = 0;
    private int selected = 0;

    public LobbyFrame(TuiClient tuiClient) {
        super(tuiClient);
    }

    @Override
    public Component getPanel() {
        return frame;
    }

    @Override
    public void handleKeyStroke(KeyStroke keyStroke) {

    }

    @Override
    public void update() {
        frame = new Panel();
        frame.setLayoutManager(new GridLayout(2));
        TerminalSize size = tuiClient.getSize();

        if (tuiClient.getServerLiveData().getGameSessions() == null ||
                tuiClient.getServerLiveData().getUsers() == null ||
                tuiClient.getServerLiveData().getLoginResult() == null) {
            return;
        }

        Panel gamePanel = new Panel();
        gamePanel.setPreferredSize(new TerminalSize(size.getColumns() / 2, size.getRows() - 2));
        for (MainProto.GameSession game : tuiClient.getServerLiveData().getGameSessions()) {
            Panel panel = new Panel();
            panel.withBorder(Borders.singleLine(""));
            panel.setLayoutManager(new GridLayout(2));

            panel.addComponent(new Label("Type:"));
            panel.addComponent(new Label(game.getType()));

            int userId0 = game.getUserId(0);
            if (userId0 != 0) {
                panel.addComponent(new Label(tuiClient.getServerLiveData().getUsers().get(userId0).getName()));
            } else {
                panel.addComponent(new Label(""));
            }
            int userId1 = game.getUserId(1);
            if (userId1 != 0) {
                panel.addComponent(new Label(tuiClient.getServerLiveData().getUsers().get(userId1).getName()));
            } else {
                panel.addComponent(new Label(""));
            }
            int userId2 = game.getUserId(2);
            if (userId2 != 0) {
                panel.addComponent(new Label(tuiClient.getServerLiveData().getUsers().get(userId2).getName()));
            } else {
                panel.addComponent(new Label(""));
            }
            int userId3 = game.getUserId(3);
            if (userId3 != 0) {
                panel.addComponent(new Label(tuiClient.getServerLiveData().getUsers().get(userId3).getName()));
            } else {
                panel.addComponent(new Label(""));
            }

            panel.addComponent(new Label("Status:"));
            panel.addComponent(new Label("" + game.getState()));

            gamePanel.addComponent(panel.withBorder(Borders.singleLine()));
        }

        Panel userPanel = new Panel();
        userPanel.setPreferredSize(new TerminalSize(size.getColumns() / 2, size.getRows() - 2));
        for (Map.Entry<Integer, MainProto.User> user : tuiClient.getServerLiveData().getUsers().entrySet()) {
            if (!user.getValue().getBot() || user.getValue().getId() != tuiClient.getServerLiveData().getLoginResult().getUserId()) {
                Panel panel = new Panel();
                panel.addComponent(new Label(user.getValue().getName()));
                if(user.getValue().getOnline()){
                    panel.addComponent(new Label("Online"));
                }else {
                    panel.addComponent(new Label("Offline"));
                }
                if(user.getValue().getIsFriend()){
                    panel.addComponent(new Label("Friend"));
                }else {
                    panel.addComponent(new Label("Not Friend"));
                }
                userPanel.addComponent(panel);
            }
        }

        frame.addComponent(gamePanel.withBorder(Borders.doubleLine()));
        frame.addComponent(userPanel.withBorder(Borders.doubleLine()));
    }
}
