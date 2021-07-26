package hu.xannosz.tarokk.client.util;

import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.Theme;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ThemeHandler {
    public Theme getHighLightedTheme(TerminalSettings terminalSettings) {
        return new SimpleTheme(terminalSettings.getMainPanelHighLightedForeGround(), terminalSettings.getBackGround());
    }

    public Theme getErrorTheme(TerminalSettings terminalSettings) {
        return new SimpleTheme(terminalSettings.getMainPanelErrorForeGround(), terminalSettings.getBackGround());
    }
}
