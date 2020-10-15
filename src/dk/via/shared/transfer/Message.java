package dk.via.shared.transfer;

import dk.via.shared.utils.Date;

import java.io.Serializable;

public class Message implements Serializable {
    private final String messageBody;
    private final String messageSender;
    private final String messageReceiver;
    private final Date timeStamp;

    public Message(String messageSender, String messageBody, String messageReceiver) {
        this.messageBody = messageBody;
        this.messageSender = messageSender;
        this.messageReceiver = messageReceiver;
        this.timeStamp = new Date();
    }

    public String getMessageBody() {
        return messageBody;
    }

    public String getMessageSender() {
        return messageSender;
    }

    public String getMessageReceiver() {
        return messageReceiver;
    }

    public String getTimeStamp() {
        return timeStamp.getTimestamp();
    }
}
