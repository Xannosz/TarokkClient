package hu.xannosz.tarokk.client.util;

import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.Theme;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ThemeHandler {
    public Theme getHighLightedTheme(TerminalSettings terminalSettings) {
        return new SimpleTheme(terminalSettings.getHighLightedForeGround(), terminalSettings.getBackGround());
    }

    public Theme getHighLightedThemeMainPanel(TerminalSettings terminalSettings) {
        return new SimpleTheme(terminalSettings.getHighLightedForeGround(), terminalSettings.getMainPanelBackGround());
    }

    public Theme getErrorTheme(TerminalSettings terminalSettings) {
        return new SimpleTheme(terminalSettings.getErrorForeGround(), terminalSettings.getBackGround());
    }

    public Theme getErrorThemeMainPanel(TerminalSettings terminalSettings) {
        return new SimpleTheme(terminalSettings.getErrorForeGround(), terminalSettings.getMainPanelBackGround());
    }

    public Theme getSubLightedTheme(TerminalSettings terminalSettings) {
        return new SimpleTheme(terminalSettings.getSubLightedForeGround(), terminalSettings.getBackGround());
    }

    public Theme getSubLightedThemeMainPanel(TerminalSettings terminalSettings) {
        return new SimpleTheme(terminalSettings.getSubLightedForeGround(), terminalSettings.getMainPanelBackGround());
    }

    public Theme getOnlineColorTheme(TerminalSettings terminalSettings) {
        return new SimpleTheme(terminalSettings.getOnlineColor(), terminalSettings.getBackGround());
    }

    public Theme getOnlineColorThemeMainPanel(TerminalSettings terminalSettings) {
        return new SimpleTheme(terminalSettings.getOnlineColor(), terminalSettings.getMainPanelBackGround());
    }
}
