package hometask02;

import java.util.Comparator;

public class LogData {
    private final String dateTime;
    private final String info;

    public LogData(String dateTime, String info) {
        this.dateTime = dateTime;
        this.info = info;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getInfo() {
        return info;
    }

    public static Comparator<LogData> dateTimeComparator = Comparator.comparing(LogData::getDateTime);
}
