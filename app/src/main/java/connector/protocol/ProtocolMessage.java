package connector.protocol;

import lombok.Data;

import java.io.Serializable;

public record ProtocolMessage(Protocol protocol, Serializable data,long timeStump) implements Serializable {
}
