package models;

import javafx.scene.control.DatePicker;

import java.time.Instant;

public interface ConvertDate {

    Instant toInstant(DatePicker date, String time);
}
