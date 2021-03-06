package dk.via.shared.transfer;

import dk.via.shared.utils.UserAction;

import java.io.Serializable;

public class Request implements Serializable {
    private final UserAction type;
    private final Object object;

    public Request(UserAction type, Object object) {
        this.type = type;
        this.object = object;
    }

    public UserAction getType() {
        return type;
    }

    public Object getObject() {
        return object;
    }
}
