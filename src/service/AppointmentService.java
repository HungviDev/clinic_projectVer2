package service;

import model.Appointment;

import java.time.LocalDate;
import java.util.List;

import dao.user.AppointmentDAO;

public class AppointmentService {

    private AppointmentDAO appointmentDAO;

    public AppointmentService() {
        appointmentDAO = new AppointmentDAO();
    }

    public List<LocalDate> getBookedDates(int userId) {

        return appointmentDAO.getBookedDates(userId);
    }

    public List<Appointment> getAppointmentsByDate(
            int userId,
            LocalDate date
    ) {

        return appointmentDAO.getAppointmentsByDate(
                userId,
                date
        );
    }

    public List<Appointment> getAppointmentsByUserId(int userId) {
        return appointmentDAO.getAppointmentsByUserId(userId);
    }

    // BUSINESS LOGIC

    public boolean hasAppointmentToday(int userId) {

        List<LocalDate> dates =
                appointmentDAO.getBookedDates(userId);

        return dates.contains(LocalDate.now());
    }
}