package controller;

import model.Booking;
import model.Doctor;
import model.Service;
import service.BookingService;

import java.util.List;

public class BookingController {

    private BookingService service;

    public BookingController() {
        service = new BookingService();
    }

    public boolean submitBooking(Booking booking) {
        return service.createBooking(booking);
    }

    public List<Doctor> getDoctors() {
        return service.getDoctors();
    }

    public List<Service> getServices() {
        return service.getServices();
    }
}