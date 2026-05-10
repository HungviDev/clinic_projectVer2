package service;

import dao.BookingDAO;
import dao.DoctorDAO;
import dao.ServiceDAO;
import model.Booking;
import model.Doctor;
import model.Service;

import java.util.List;

public class BookingService {

    private BookingDAO bookingDAO;
    private DoctorDAO doctorDAO;
    private ServiceDAO serviceDAO;

    public BookingService() {

        bookingDAO = new BookingDAO();
        doctorDAO = new DoctorDAO();
        serviceDAO = new ServiceDAO();
    }

    public boolean createBooking(Booking booking) {

        if (booking.getDoctorId() <= 0) {
            return false;
        }

        return bookingDAO.saveBooking(booking);
    }

    public List<Doctor> getDoctors() {
        return doctorDAO.getAllDoctors();
    }

    public List<Service> getServices() {
        return serviceDAO.getAllServices();
    }
}