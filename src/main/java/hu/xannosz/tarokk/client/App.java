package hu.xannosz.tarokk.client;

import hu.xannosz.tarokk.client.commandline.CommandLineClient;
import hu.xannosz.tarokk.client.common.TerminalSettings;

public class App {
    public static void main(String[] args) {
        new CommandLineClient(new TerminalSettings());
    }
}
