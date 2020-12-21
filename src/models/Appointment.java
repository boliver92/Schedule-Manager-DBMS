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

    // -----------------------------------------------------------------------------------------------------------------
    // Static Variables ------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------
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
    private static FilteredList<Appointment> appointmentFilteredListByDate = new FilteredList<>(appointmentSortedList, s -> true);
    private static FilteredList<Appointment>  appointmentFilteredList = new FilteredList<>(appointmentFilteredListByDate, s -> true);

    // -----------------------------------------------------------------------------------------------------------------
    // Instance Variables ----------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------

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

    // -----------------------------------------------------------------------------------------------------------------
    // Constructor -----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------
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

    // -----------------------------------------------------------------------------------------------------------------
    // INSTANCE METHODS ------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------


    // -----------------------------------------------------------------------------------------------------------------
    // STATIC METHODS --------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------

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
     * Returns true if the customer passed in is assigned to any appointment's contact in appointmentList.
     * @param customer
     * @return true if the customer is assigned to an appointment, false if not.
     */
    public static boolean exists(Customer customer){
        for(Appointment appointment : appointmentList){
            if(appointment.getCustomer() == customer){
                return true;
            }
        }
        return false;
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

    /**
     * Overrides the current appointment in the appointmentList with the most recent appointment in the database that has
     * the same appointmentId
     * @param appointment Appointment to be refreshed.
     * @return true
     */
    public static boolean refreshAppointment(Appointment appointment){
        System.out.println("Reloading Appointment.");
        int index = appointmentList.indexOf(appointment);
        Appointment refreshedAppointment = DBQuery.loadAppointment(appointment.getAppointmentId());
        appointmentList.set(index, refreshedAppointment);
        UpdateAppointmentViewController.setAppointment(refreshedAppointment);
        return true;
    }

    /**
     * Removes the appointment from the appointmentList and calls DBQuery method to remove the appointment from the
     * Database.
     * @param appointment The appointment to be removed
     * @return true if the appointment was removed, otherwise false.
     */
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

        // -------------------------------------------------------------------------------------------------------------
        // STATIC GETTERS ----------------------------------------------------------------------------------------------
        // -------------------------------------------------------------------------------------------------------------

    /**
     * Returns the filtered appointmentList
     * @return FilteredList(Appointment) appointmentFilteredList
     */
    public static FilteredList<Appointment> getAppointmentFilteredList() {
        return appointmentFilteredList;
    }

    /**
     * Returns the filtered appointmentList
     * @return FilteredList(Appointment) appointmentFilteredListByDate
     */
    public static FilteredList<Appointment> getAppointmentFilteredListByDate() {
        return appointmentFilteredListByDate;
    }


    // GETTER ---------------------------------------------------------------------------------------------------------

    /**
     * Returns the customer's ID
     * @return customerId
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Returns the formatted String start time
     * @return startFormatted
     */
    public String getStartFormatted() {
        return startFormatted;
    }

    /**
     * Returns the formatted String end time
     * @return endFormatted
     */
    public String getEndFormatted() {
        return endFormatted;
    }

    /**
     * Returns the appointment ID.
     * @return appointmentId
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * Returns the appointment Type
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the appointments User ID
     * @return userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Returns the LocalTime object startTime showing the starting time of the appointment.
     * @return startTime
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Returns the LocalTime object startTime showing the ending time of the appointment.
     * @return endTime
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Returns the Contact object assigned to the appointment.
     * @return contact
     */
    public Contact getContact() {
        return contact;
    }

    /**
     * Returns the Customer object assigned to the appointment.
     * @return customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Returns the Instant end object assigned to the appointment
     * @return end
     */
    public Instant getEnd() {
        return end;
    }

    /**
     * Returns the Instant start object assigned to the appointment
     * @return start
     */
    public Instant getStart() {
        return start;
    }

    /**
     * Returns the contact ID
     * @return contactId
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * Returns the appointment's description
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the appointment's location
     * @return location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Returns the appointment's title
     * @return title
     */
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
