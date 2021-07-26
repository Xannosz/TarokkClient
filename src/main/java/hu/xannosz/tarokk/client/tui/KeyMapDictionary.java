package hu.xannosz.tarokk.client.tui;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class KeyMapDictionary {
    @Getter
    private final Map<String, String> functionNames = new HashMap<>();
    private final Map<String, Runnable> functionSuppliers = new HashMap<>();

    public void addFunction(String key, String name, Runnable function) {
        functionNames.put(key, name);
        functionSuppliers.put(key, function);
    }

    public void runFunction(String key) {
        if (functionSuppliers.containsKey(key)) {
            functionSuppliers.get(key).run();
        }
    }

    public void clear() {
        functionNames.clear();
        functionSuppliers.clear();
    }
}
