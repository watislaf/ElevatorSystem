package connector.protocol;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProtocolMessage implements Serializable {
    Protocol protocol = Protocol.OK;
    Byte data;
}
