package hu.xannosz.tarokk.client.gui;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class Event {
    private String eventId;
    private Map<String,Object> additionalParams;
}
