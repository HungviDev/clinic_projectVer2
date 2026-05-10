package service;

import dao.user.AppointmentDetailDAO;
import model.user.AppointmentDetail;

public class AppointmentDetailService {

    private AppointmentDetailDAO dao;

    public AppointmentDetailService() {

        dao = new AppointmentDetailDAO();
    }

    public AppointmentDetail getDetail(
            int appointmentId
    ) {

        return dao.getDetailById(
                appointmentId
        );
    }

    public void cancelAppointment(
            int appointmentId
    ) {

        dao.cancelAppointment(
                appointmentId
        );
    }
}