package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Map;

public class TimeFunctions {
    private static final Map<String, String> shortToLongTimeMap = Map.ofEntries(
            Map.entry("8:00 AM EST", "08:00 AM"),
            Map.entry("8:15 AM EST", "08:15 AM"),
            Map.entry("8:30 AM EST", "08:30 AM"),
            Map.entry("8:45 AM EST", "08:45 AM"),
            Map.entry("9:00 AM EST", "09:00 AM"),
            Map.entry("9:15 AM EST", "09:15 AM"),
            Map.entry("9:30 AM EST", "09:30 AM"),
            Map.entry("9:45 AM EST", "09:45 AM"),
            Map.entry("10:00 AM EST", "10:00 AM"),
            Map.entry("10:15 AM EST", "10:15 AM"),
            Map.entry("10:30 AM EST", "10:30 AM"),
            Map.entry("10:45 AM EST", "10:45 AM"),
            Map.entry("11:00 AM EST", "11:00 AM"),
            Map.entry("11:15 AM EST", "11:15 AM"),
            Map.entry("11:30 AM EST", "11:30 AM"),
            Map.entry("11:45 AM EST", "11:45 AM"),
            Map.entry("12:00 PM EST", "12:00 PM"),
            Map.entry("12:15 PM EST", "12:15 PM"),
            Map.entry("12:30 PM EST", "12:30 PM"),
            Map.entry("12:45 PM EST", "12:45 PM"),
            Map.entry("1:00 PM EST", "01:00 PM"),
            Map.entry("1:15 PM EST", "01:15 PM"),
            Map.entry("1:30 PM EST", "01:30 PM"),
            Map.entry("1:45 PM EST", "01:45 PM"),
            Map.entry("2:00 PM EST", "02:00 PM"),
            Map.entry("2:15 PM EST", "02:15 PM"),
            Map.entry("2:30 PM EST", "02:30 PM"),
            Map.entry("2:45 PM EST", "02:45 PM"),
            Map.entry("3:00 PM EST", "03:00 PM"),
            Map.entry("3:15 PM EST", "03:15 PM"),
            Map.entry("3:30 PM EST", "03:30 PM"),
            Map.entry("3:45 PM EST", "03:45 PM"),
            Map.entry("4:00 PM EST", "04:00 PM"),
            Map.entry("4:15 PM EST", "04:15 PM"),
            Map.entry("4:30 PM EST", "04:30 PM"),
            Map.entry("4:45 PM EST", "04:45 PM"),
            Map.entry("5:00 PM EST", "05:00 PM"),
            Map.entry("5:15 PM EST", "05:15 PM"),
            Map.entry("5:30 PM EST", "05:30 PM"),
            Map.entry("5:45 PM EST", "05:45 PM"),
            Map.entry("6:00 PM EST", "06:00 PM"),
            Map.entry("6:15 PM EST", "06:15 PM"),
            Map.entry("6:30 PM EST", "06:30 PM"),
            Map.entry("6:45 PM EST", "06:45 PM"),
            Map.entry("7:00 PM EST", "07:00 PM"),
            Map.entry("7:15 PM EST", "07:15 PM"),
            Map.entry("7:30 PM EST", "07:30 PM"),
            Map.entry("7:45 PM EST", "07:45 PM"),
            Map.entry("8:00 PM EST", "08:00 PM"),
            Map.entry("8:15 PM EST", "08:15 PM"),
            Map.entry("8:30 PM EST", "08:30 PM"),
            Map.entry("8:45 PM EST", "08:45 PM"),
            Map.entry("9:00 PM EST", "09:00 PM"),
            Map.entry("9:15 PM EST", "09:15 PM"),
            Map.entry("9:30 PM EST", "09:30 PM"),
            Map.entry("9:45 PM EST", "09:45 PM"),
            Map.entry("10:00 PM EST", "10:00 PM")
    );

    private static final Map<String, String> longToShortTimeMap = Map.ofEntries(
            Map.entry("08:00", "08:00 AM EST"),
            Map.entry("08:15", "08:15 AM EST"),
            Map.entry("08:30", "08:30 AM EST"),
            Map.entry("08:45", "08:45 AM EST"),
            Map.entry("09:00", "09:00 AM EST"),
            Map.entry("09:15", "09:15 AM EST"),
            Map.entry("09:30", "09:30 AM EST"),
            Map.entry("09:45", "09:45 AM EST"),
            Map.entry("10:00", "10:00 AM EST"),
            Map.entry("10:15", "10:15 AM EST"),
            Map.entry("10:30", "10:30 AM EST"),
            Map.entry("10:45", "10:45 AM EST"),
            Map.entry("11:00", "11:00 AM EST"),
            Map.entry("11:15", "11:15 AM EST"),
            Map.entry("11:30", "11:30 AM EST"),
            Map.entry("11:45", "11:45 AM EST"),
            Map.entry("12:00", "12:00 PM EST"),
            Map.entry("12:15", "12:15 PM EST"),
            Map.entry("12:30", "12:30 AM EST"),
            Map.entry("12:45", "12:45 AM EST"),
            Map.entry("13:00", "1:00 PM EST"),
            Map.entry("13:15", "1:15 PM EST"),
            Map.entry("13:30", "1:30 AM EST"),
            Map.entry("13:45", "1:45 AM EST"),
            Map.entry("14:00", "2:00 PM EST"),
            Map.entry("14:15", "2:15 PM EST"),
            Map.entry("14:30", "2:30 AM EST"),
            Map.entry("14:45", "2:45 AM EST"),
            Map.entry("15:00", "3:00 PM EST"),
            Map.entry("15:15", "3:15 PM EST"),
            Map.entry("15:30", "3:30 AM EST"),
            Map.entry("15:45", "3:45 AM EST"),
            Map.entry("16:00", "4:00 PM EST"),
            Map.entry("16:15", "4:15 PM EST"),
            Map.entry("16:30", "4:30 AM EST"),
            Map.entry("16:45", "4:45 AM EST"),
            Map.entry("17:00", "5:00 PM EST"),
            Map.entry("17:15", "5:15 PM EST"),
            Map.entry("17:30", "5:30 AM EST"),
            Map.entry("17:45", "5:45 AM EST"),
            Map.entry("18:00", "6:00 PM EST"),
            Map.entry("18:15", "6:15 PM EST"),
            Map.entry("18:30", "6:30 AM EST"),
            Map.entry("18:45", "6:45 AM EST"),
            Map.entry("19:00", "7:00 PM EST"),
            Map.entry("19:15", "7:15 PM EST"),
            Map.entry("19:30", "7:30 AM EST"),
            Map.entry("19:45", "7:45 AM EST"),
            Map.entry("20:00", "8:00 PM EST"),
            Map.entry("20:15", "8:15 PM EST"),
            Map.entry("20:30", "8:30 AM EST"),
            Map.entry("20:45", "8:45 AM EST"),
            Map.entry("21:00", "9:00 PM EST"),
            Map.entry("21:15", "9:15 PM EST"),
            Map.entry("21:30", "9:30 AM EST"),
            Map.entry("21:45", "9:45 AM EST"),
            Map.entry("22:00", "10:00 PM EST")
    );

    private static final ObservableList<String> times = FXCollections.observableArrayList(
            "8:00 AM EST",
            "8:15 AM EST",
            "8:30 AM EST",
            "8:45 AM EST",
            "8:00 AM EST",
            "9:15 AM EST",
            "9:30 AM EST",
            "9:45 AM EST",
            "10:00 AM EST",
            "10:15 AM EST",
            "10:30 AM EST",
            "10:45 AM EST",
            "11:00 AM EST",
            "11:15 AM EST",
            "11:30 AM EST",
            "11:45 AM EST",
            "12:00 PM EST",
            "12:15 PM EST",
            "12:30 PM EST",
            "12:45 PM EST",
            "1:00 PM EST",
            "1:15 PM EST",
            "1:30 PM EST",
            "1:45 PM EST",
            "2:00 PM EST",
            "2:15 PM EST",
            "2:30 PM EST",
            "2:45 PM EST",
            "3:00 PM EST",
            "3:15 PM EST",
            "3:30 PM EST",
            "3:45 PM EST",
            "4:00 PM EST",
            "4:15 PM EST",
            "4:30 PM EST",
            "4:45 PM EST",
            "5:00 PM EST",
            "5:15 PM EST",
            "5:30 PM EST",
            "5:45 PM EST",
            "6:00 PM EST",
            "6:15 PM EST",
            "6:30 PM EST",
            "6:45 PM EST",
            "7:00 PM EST",
            "7:15 PM EST",
            "7:30 PM EST",
            "7:45 PM EST",
            "8:00 PM EST",
            "8:15 PM EST",
            "8:30 PM EST",
            "8:45 PM EST",
            "9:00 PM EST",
            "9:15 PM EST",
            "9:30 PM EST",
            "9:45 PM EST",
            "10:00 PM EST"
    );

    public static ObservableList<String> getTimes() {
        return times;
    }

    public static String convertToShort(String time){
        for(Map.Entry<String, String> entry : shortToLongTimeMap.entrySet()){
            if(entry.getValue().contains(time))
                return entry.getKey();
        }
        return null;
    }

    public static String convertToLong(String time){
        for(Map.Entry<String, String> entry : longToShortTimeMap.entrySet()){
            if(entry.getKey().contains(time)){
                return entry.getValue();
            }
        }
        return null;
    }
}
