package connector.protocol;

import connector.DataClient;

public enum Protocol {
    // BOTH
    OK,
    // FROM_SERVER
    UPDATE_DATA,     // List<Creature>
    APPLICATION_SETTINGS, // AApplicationSettings
    ELEVATOR_BUTTON_CLICK, // Vector2D
    ELEVATOR_OPEN, // Integer (elevator id)
    ELEVATOR_CLOSE, // Integer (elevator id)
    CUSTOMER_GET_IN_OUT, // Integer (customer id)
    // FROM_CLIENT
    CREATE_CUSTOMER,

}
