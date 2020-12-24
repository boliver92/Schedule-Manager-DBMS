package models;

import javafx.scene.control.DatePicker;

import java.time.Instant;

public interface ConvertDate {

    /**
     * Takes a Datepicker and String as inputs to create a lambda function
     * @param date Datepicker
     * @param time String
     * @return result of lambda function
     */
    Instant toInstant(DatePicker date, String time);
}
