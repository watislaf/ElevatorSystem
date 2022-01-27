package connector;

import connector.protocol.ProtocolMessage;

public interface OnSocketEvent {
    void onReceive(ProtocolMessage message);
    void onNewConnection(DataClient message);
}
