package hu.xannosz.tarokk.client.util;

import lombok.Getter;

@Getter
public class LogSettings {

    public static LogSettings INSTANCE = new LogSettings();

    private boolean messageLog = false;
    private boolean gameLog = false;
    private boolean keyLog = false;
}
