package models;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.time.Instant;

public interface Convert {

    Instant toInstant(DatePicker date, ComboBox<String> time);
}
