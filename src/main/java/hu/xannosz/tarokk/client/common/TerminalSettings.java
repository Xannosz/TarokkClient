package hu.xannosz.tarokk.client.common;

import com.googlecode.lanterna.TextColor;
import lombok.Getter;

@Getter
public class TerminalSettings {
    private TextColor foreGround = new TextColor.RGB(129, 236, 13);
    private TextColor backGround = new TextColor.RGB(22, 23, 29);
    private TextColor messagePanelBackGround = new TextColor.RGB(22, 23, 29);
    private TextColor messagePanelForeGround = new TextColor.RGB(129, 236, 13);
    private TextColor actionsPanelBackGround = new TextColor.RGB(31, 32, 41);
    private TextColor actionsPanelForeGround = new TextColor.RGB(25, 209, 216);
    private TextColor keyColor = new TextColor.RGB(255, 102, 0);
}
