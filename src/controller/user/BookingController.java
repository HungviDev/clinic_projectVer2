package controller.user;

import model.Booking;
import model.Doctor;
import model.Service;
import service.BookingService;

import java.util.List;

import dao.user.BookingDAO;

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