package models;

import controllers.AppointmentViewController;
import controllers.UpdateAppointmentViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Parent;
import utils.DBQuery;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.TimeZone;

public class Appointment {

    // Static Variables
    private static ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    private static SortedList<Appointment> appointmentSortedList = new SortedList<>(appointmentList, (o1, o2) -> {
        if(o1.getStart().isBefore(o2.getStart())){
            return -1;
        }
        else if(o1.getStart().isAfter(o2.getStart())){
            return 1;
        }
        return 0;
    });
    private static FilteredList<Appointment>  appointmentFilteredList = new FilteredList<>(appointmentSortedList, s -> true);
    // Instance variables

    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private Instant start;
    private Instant end;
    private int customerId;
    private int contactId;
    private int userId;
    private LocalTime startTime;
    private LocalTime endTime;
    private String startFormatted;
    private String endFormatted;
    private Customer customer;
    private Contact contact;

    // Constructor
    public Appointment(){}
    public Appointment(int appointmentId, String title, String description, String location, String type,
                       Instant start, Instant end, int customerId, int contactId, int userId){
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.contactId = contactId;
        this.userId = userId;

        // Time Formatting
        this.startTime = start.atZone(ZoneId.of(TimeZone.getDefault().getID())).toLocalTime();
        this.endTime = end.atZone(ZoneId.of(TimeZone.getDefault().getID())).toLocalTime();
        this.startFormatted = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm a").format(start.atZone(ZoneId.of(TimeZone.getDefault().getID())));
        this.endFormatted = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm a").format(end.atZone(ZoneId.of(TimeZone.getDefault().getID())));

        // Relation Mapping
        this.contact = Contact.findById(this.contactId);
        this.customer = Customer.findById(this.customerId);


        System.out.println("New Appointment Object: " + this);
    }

    // Instance Methods


    // Static Methods
    public static SortedList<Appointment> getAppointmentSortedList() {
        return appointmentSortedList;
    }

    public static FilteredList<Appointment> getAppointmentFilteredList() {
        return appointmentFilteredList;
    }

    public static boolean removeAppointment(Appointment appointment) {
        if (DBQuery.removeAppointment(appointment)) {
            if (appointmentList.remove(appointment)) {
                System.out.println("The appointment was successfully removed from the appointmentList");
                return true;
            }
        } else {
            System.out.println("Error ( Appointment.removeAppointment() ) : The appointment was not removed from the " +
                    "appointmentList");
            return false;
        }
        return false;
    }

    public static boolean findByCustomerId(int customerId){
        for(Appointment appointment : appointmentList){
            if(appointment.getCustomerId() == customerId)
                return true;
        }
        return false;
    }

    public static boolean exists(Customer customer){
        for(Appointment appointment : appointmentList){
            if(appointment.getCustomer() == customer){
                return true;
            }
        }
        return false;
    }
    /**
     * Takes an appointment object as an input and adds it to the appointmentList. This is required to keep the object
     * persistent and to use the object with methods that utilize the Observablelist.
     * @param   appointment The appointment object to be added to the list.
     * @return              true if the appointment object was added, false if not.
     */
    public static boolean addToList(Appointment appointment){
        return appointmentList.add(appointment);
    }

    /**
     * Prints the String representation of each appointment object in the appointmentList.
     */
    public static void printAppointmentList(){
        System.out.println("-----------------------------------Appointment List-----------------------------------");
        for(Appointment appointment : appointmentList){
            System.out.println(appointment);
        }
    }

    public static boolean refreshAppointment(Appointment appointment){
        System.out.println("Reloading Appointment.");
        int index = appointmentList.indexOf(appointment);
        Appointment refreshedAppointment = DBQuery.loadAppointment(appointment.getAppointmentId());
        appointmentList.set(index, refreshedAppointment);
        UpdateAppointmentViewController.setAppointment(refreshedAppointment);
        return true;
    }

    public static ObservableList<Appointment> getAppointmentList() {
        return appointmentList;
    }

    // GETTER ---------------------------------------------------------------------------------------------------------

    public int getCustomerId() {
        return customerId;
    }

    public String getStartFormatted() {
        return startFormatted;
    }

    public String getEndFormatted() {
        return endFormatted;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public String getType() {
        return type;
    }

    public int getUserId() {
        return userId;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public Contact getContact() {
        return contact;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Instant getEnd() {
        return end;
    }

    public Instant getStart() {
        return start;
    }

    public int getContactId() {
        return contactId;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getTitle() {
        return title;
    }


    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", type='" + type + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", customerId=" + customerId +
                ", contactId=" + contactId +
                ", userId=" + userId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", startFormatted=" + startFormatted +
                ", endFormatted=" + endFormatted +
                ", customer=" + customer.getCustomerName() +
                ", contact=" + contact.getContactName() +
                '}';
    }
}
