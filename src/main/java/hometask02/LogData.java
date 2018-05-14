package hometask02;

import java.util.Comparator;

public class LogData {
    private  String dateTime;
    private  String info;

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

    public void setInfo(String s) {
        this.info = s;
    }

    public static Comparator<LogData> dateTimeComparator = Comparator.comparing(LogData::getDateTime);
}
