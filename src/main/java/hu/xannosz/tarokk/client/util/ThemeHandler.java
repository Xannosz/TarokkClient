package hu.xannosz.tarokk.client.util;

import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.Theme;
import hu.xannosz.tarokk.client.util.settings.TerminalSettings;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ThemeHandler {
    public Theme getTheme() {
        return new SimpleTheme(TerminalSettings.INSTANCE.getForeGround(), TerminalSettings.INSTANCE.getBackGround());
    }

    public Theme getHighLightedTheme() {
        return new SimpleTheme(TerminalSettings.INSTANCE.getHighLightedForeGround(), TerminalSettings.INSTANCE.getBackGround());
    }

    public Theme getHighLightedThemeMainPanel() {
        return new SimpleTheme(TerminalSettings.INSTANCE.getHighLightedForeGround(), TerminalSettings.INSTANCE.getMainPanelBackGround());
    }

    public Theme getErrorTheme() {
        return new SimpleTheme(TerminalSettings.INSTANCE.getErrorForeGround(), TerminalSettings.INSTANCE.getBackGround());
    }

    public Theme getSubLightedThemeMainPanel() {
        return new SimpleTheme(TerminalSettings.INSTANCE.getSubLightedForeGround(), TerminalSettings.INSTANCE.getMainPanelBackGround());
    }

    public Theme getOnlineColorThemeMainPanel() {
        return new SimpleTheme(TerminalSettings.INSTANCE.getOnlineColor(), TerminalSettings.INSTANCE.getMainPanelBackGround());
    }

    public Theme getKeyThemeFooterPanel() {
        return new SimpleTheme(TerminalSettings.INSTANCE.getKeyColor(), TerminalSettings.INSTANCE.getFooterPanelBackGround());
    }

    public Theme getFooterPanelTheme() {
        return new SimpleTheme(TerminalSettings.INSTANCE.getFooterPanelForeGround(), TerminalSettings.INSTANCE.getFooterPanelBackGround());
    }
}
