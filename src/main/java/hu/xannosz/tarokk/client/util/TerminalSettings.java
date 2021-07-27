package hu.xannosz.tarokk.client.util;

import com.googlecode.lanterna.TextColor;
import lombok.Getter;

@Getter
public class TerminalSettings {
    private TextColor foreGround = new TextColor.RGB(129, 236, 13);
    private TextColor backGround = new TextColor.RGB(22, 23, 29);
    private TextColor mainPanelBackGround = new TextColor.RGB(22, 23, 29);
    private TextColor mainPanelForeGround = new TextColor.RGB(129, 236, 13);
    private TextColor errorForeGround = new TextColor.RGB(210, 0, 0);
    private TextColor highLightedForeGround = new TextColor.RGB(153, 51, 204);
    private TextColor subLightedForeGround = new TextColor.RGB(70,70,70);
    private TextColor onlineColor = new TextColor.RGB(68, 180, 204);
    private TextColor actionsPanelBackGround = new TextColor.RGB(31, 32, 41);
    private TextColor actionsPanelForeGround = new TextColor.RGB(25, 209, 216);
    private TextColor keyColor = new TextColor.RGB(255, 102, 0);


    /*
     {
            // Color Scheme: VibrantTom
            "background" : "#16171D",
            "black" : "#878787",
            "blue" : "#44B4CC",
            "brightBlack" : "#E373C8",
            "brightBlue" : "#0000FF",
            "brightCyan" : "#19D1D8",
            "brightGreen" : "#81EC0D",
            "brightPurple" : "#FF00FF",
            "brightRed" : "#FF0000",
            "brightWhite" : "#E5E5E5",
            "brightYellow" : "#FFD93D",
            "cyan" : "#19D1D8",
            "foreground" : "#FFFFFF",
            "green" : "#CCFF04",
            "name" : "VibrantTom",
            "purple" : "#9933CC",
            "red" : "#FF6600",
            "white" : "#F5F5F5",
            "yellow" : "#FFD93D"
        }
     */
}
