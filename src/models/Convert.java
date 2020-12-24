package models;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.time.Instant;

public interface Convert {

    /**
     * Takes a Datepicker and Combobox(String) as inputs to create a lambda function
     * @param date Datepicker
     * @param time ComboBox(String)
     * @return result of lambda function
     */
    Instant toInstant(DatePicker date, ComboBox<String> time);
}
