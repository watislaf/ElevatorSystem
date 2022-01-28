package connector.protocol;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProtocolMessage implements Serializable {
    private final Protocol protocol;
    private final Serializable data;
}
