package hu.xannosz.tarokk.client.terminal;

import com.googlecode.lanterna.gui2.Panel;
import com.tisza.tarock.proto.MainProto;
import hu.xannosz.tarokk.client.common.AbstractClient;
import hu.xannosz.tarokk.client.common.TerminalSettings;

public class TerminalClient extends AbstractClient {
    public TerminalClient(TerminalSettings terminalSettings) {
        super(terminalSettings);
    }

    @Override
    protected void createActionsList(MainProto.Message message) {

    }

    @Override
    protected Panel drawWindow(MainProto.Message message) {
        return null;
    }
}
