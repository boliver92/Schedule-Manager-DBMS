package models;

import controllers.UpcomingAppointmentViewController;
import controllers.UpdateAppointmentViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import utils.DBQuery;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
    private static FilteredList<Appointment> appointmentFilteredListByContact = new FilteredList<>(appointmentSortedList, s -> false);
    private static FilteredList<Appointment>  appointmentFilteredList = new FilteredList<>(appointmentFilteredListByDate, s -> true);
    private static Map<String, Integer> typeAmountMap = new HashMap<>();
    private static Map<String, Integer> monthAmountMap = new HashMap<>();
    private static Map<String, Integer> customerAmountMap = new HashMap<>();

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

        incrementMapType(type);
        incrementCustomerMap(customer.getCustomerName());
        try {
            incrementMapMonth(startFormatted);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
     * Increments the typeAmountMap to reflect the number of appointments with a matching type.
     * @param date Date of the appointment to be mapped
     */
    public static void incrementMapMonth(String date) throws ParseException {

        SimpleDateFormat month = new SimpleDateFormat("MMMMM");
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

        Date fdate = sdf.parse(date);
        String month_name = month.format(fdate);

        System.out.println(month_name);

        Appointment.monthAmountMap.compute(month_name, (key, val) -> (val == null) ? 1 : val + 1);
    }

    /**
     * Decrements the monthAmountMap to reflect the number of appointments with a matching month.
     * @param date Date of the appointment to be mapped
     */
    public static void decrementMapMonth(String date) throws ParseException {

        SimpleDateFormat month = new SimpleDateFormat("MMMMM");
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

        Date fdate = sdf.parse(date);
        String month_name = month.format(fdate);

        Appointment.monthAmountMap.compute(month_name, (key, val) -> (val != null || val > 0) ? val - 1 : 0);

        if(Appointment.monthAmountMap.get(month_name) == 0){
            Appointment.monthAmountMap.remove(month_name);
        }
    }
    /**
     * Increments the typeAmountMap to reflect the number of appointments with a matching type.
     * @param type Type of appointment to be mapped
     */
    private static void incrementMapType(String type){
        type = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
        Appointment.typeAmountMap.compute(type, (key, val) -> (val == null) ? 1 : val + 1);
    }

    /**
     * Decrements the typeAmountMap to reflect the number of appointments with a matching type.
     * @param type Type of appointment to be mapped
     */
    private static void decrementMapType(String type){
        type = type.substring(0, 1).toUpperCase() + type.substring(1);
        Appointment.typeAmountMap.compute(type, (key, val) -> (val != null || val > 0) ? val - 1 : 0);

        if(Appointment.typeAmountMap.get(type) == 0){
            Appointment.typeAmountMap.remove(type);
        }
    }

    private static void incrementCustomerMap(String customerName){
        Appointment.customerAmountMap.compute(customerName, (key, val) -> (val == null) ? 1 : val + 1);
    }

    private static void decrementCustomerMap(String customerName){
        Appointment.customerAmountMap.compute(customerName, (key, val) -> (val != null || val > 0) ? val - 1 : 0);

        if(Appointment.customerAmountMap.get(customerName) == 0){
            Appointment.customerAmountMap.remove(customerName);
        }
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
        decrementMapType(appointment.getType());
        decrementCustomerMap(appointment.getCustomer().getCustomerName());
        try {
            decrementMapMonth(appointment.getStartFormatted());
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
                decrementMapType(appointment.getType());
                decrementCustomerMap(appointment.getCustomer().getCustomerName());
                try {
                    decrementMapMonth(appointment.getStartFormatted());
                } catch (ParseException e) {
                }
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

    /**
     * Returns false if there is another appointment overlapping the start and end time
     * @param start the Start time of the appointment
     * @param end   the end time of the appointment
     * @return true if there is a conflicting appointment, false if not.
     */
    public static boolean hasConflictingTime(Instant start, Instant end){
        for(Appointment appointment : appointmentList){
            if(start.isAfter(appointment.getStart()) && start.isBefore(appointment.getEnd())){
                return true;
            }
            if(end.isAfter(appointment.getStart()) && end.isBefore(appointment.getEnd())){
                return true;
            }
        }
        return false;
    }

    public static boolean hasUpcomingAppointment(){
        Instant now = Instant.now();
        for(Appointment appointment : Appointment.appointmentList){
            if(now.until(appointment.getStart(), ChronoUnit.MINUTES) <= 15 && now.until(appointment.getStart(), ChronoUnit.MINUTES) >= 0){
                System.out.println(now.until(appointment.getStart(), ChronoUnit.MINUTES));
                UpcomingAppointmentViewController.setAppointment(appointment);
                return true;
            }
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

    /**
     * Returns the filtered appointmentList
     * @return FilteredList(Appointment) appointmentFilteredListByContact
     */
    public static FilteredList<Appointment> getAppointmentFilteredListByContact() {
        return appointmentFilteredListByContact;
    }

    public static Map<String, Integer> getTypeAmountMap() {
        return typeAmountMap;
    }

    public static Map<String, Integer> getMonthAmountMap() {
        return monthAmountMap;
    }

    public static Map<String, Integer> getCustomerAmountMap() {
        return customerAmountMap;
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
