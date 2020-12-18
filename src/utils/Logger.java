package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;

public class Logger {

    /**
     * Creates a record of an attempted login attempt to the login_activity.txt file.
     * @param   username    The username entered.
     * @param   success     true if the login attempt was successful, otherwise false.
     *
     */
    public static void logLogin(String username, boolean success) {
        try(var writer = new BufferedWriter(new FileWriter("login_activity.txt", true))){
            DateTimeFormatter df = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.ENGLISH).withZone(ZoneId.of("UTC"));
            Instant instant = Instant.now();
            if(success) {
                writer.newLine();
                writer.append(username).append(" - ").append(df.format(instant)).append(" UTC: Login Successful");
            } else {
                writer.newLine();
                writer.append(username).append(" - ").append(df.format(instant)).append(" UTC: Login Unsuccessful");
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
