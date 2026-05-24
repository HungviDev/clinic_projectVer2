package service;

import java.util.List;

import dao.user.BookingDAO;
import dao.user.DoctorDAO;
import dao.user.ServiceDAO;
import model.user.Booking;
import model.user.Doctor;
import model.user.Service;

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

    public String getNextTreatmentStage(int userId, int serviceId) {
        return bookingDAO.getNextTreatmentStageFromDB(userId, serviceId);
    }
}