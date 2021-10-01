package hu.xannosz.tarokk.client.gui;

import hu.xannosz.microtools.Json;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class Event {
    private String eventId;
    private Map<String, Object> additionalParams;

    public <T> T getAdditionalParam(String key, Class<T> clazz) {
        return Json.castObjectToSpecificClass(this.additionalParams.get(key), clazz);
    }
}
