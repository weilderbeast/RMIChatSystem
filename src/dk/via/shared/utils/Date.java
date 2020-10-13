package dk.via.shared.utils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Date implements Serializable {
    private final java.util.Date date;

    public Date() {
        date = Calendar.getInstance().getTime();
    }

    public String getTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat(
                "HH:mm");
        return sdf.format(date);
    }
}
