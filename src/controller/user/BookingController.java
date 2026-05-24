package controller.user;

import service.BookingService;

import java.util.List;

import dao.user.BookingDAO;
import model.user.Booking;
import model.user.Doctor;
import model.user.Service;

public class BookingController {

    private BookingService service;
    private BookingDAO bookingDAO;

    public BookingController() {
        service = new BookingService();
        this.bookingDAO = new BookingDAO();
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

    public String getNextTreatmentStage(int userId, int serviceId) {
        return bookingDAO.getNextTreatmentStageFromDB(userId, serviceId);
    }
}