package hu.xannosz.tarokk.client;

import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.util.settings.TerminalSettings;

public class App {
    public static void main(String[] args) {
        new TuiClient(new TerminalSettings());
    }
}
