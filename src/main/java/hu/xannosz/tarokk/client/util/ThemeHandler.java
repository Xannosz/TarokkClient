package hu.xannosz.tarokk.client.util;

import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.Theme;
import hu.xannosz.tarokk.client.util.settings.TerminalSettings;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ThemeHandler {
    public Theme getTheme(TerminalSettings terminalSettings) {
        return new SimpleTheme(terminalSettings.getForeGround(), terminalSettings.getBackGround());
    }

    public Theme getHighLightedTheme(TerminalSettings terminalSettings) {
        return new SimpleTheme(terminalSettings.getHighLightedForeGround(), terminalSettings.getBackGround());
    }

    public Theme getHighLightedThemeMainPanel(TerminalSettings terminalSettings) {
        return new SimpleTheme(terminalSettings.getHighLightedForeGround(), terminalSettings.getMainPanelBackGround());
    }

    public Theme getErrorTheme(TerminalSettings terminalSettings) {
        return new SimpleTheme(terminalSettings.getErrorForeGround(), terminalSettings.getBackGround());
    }

    public Theme getSubLightedThemeMainPanel(TerminalSettings terminalSettings) {
        return new SimpleTheme(terminalSettings.getSubLightedForeGround(), terminalSettings.getMainPanelBackGround());
    }

    public Theme getOnlineColorThemeMainPanel(TerminalSettings terminalSettings) {
        return new SimpleTheme(terminalSettings.getOnlineColor(), terminalSettings.getMainPanelBackGround());
    }

    public Theme getKeyThemeFooterPanel(TerminalSettings terminalSettings) {
        return new SimpleTheme(terminalSettings.getKeyColor(), terminalSettings.getFooterPanelBackGround());
    }

    public Theme getFooterPanelTheme(TerminalSettings terminalSettings) {
        return new SimpleTheme(terminalSettings.getFooterPanelForeGround(), terminalSettings.getFooterPanelBackGround());
    }
}
