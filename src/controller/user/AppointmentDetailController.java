package controller.user;

import model.user.AppointmentDetail;
import service.AppointmentDetailService;

public class AppointmentDetailController {

    private AppointmentDetailService service;

    public AppointmentDetailController() {

        service =
            new AppointmentDetailService();
    }

    public AppointmentDetail getDetail(
            int appointmentId
    ) {

        return service.getDetail(
                appointmentId
        );
    }

    public void cancelAppointment(
            int appointmentId
    ) {

        service.cancelAppointment(
                appointmentId
        );
    }

    public boolean canCancel(
            String status
    ) {

        return !status.equalsIgnoreCase(
                "Cancelled"
        ) &&
        !status.equalsIgnoreCase(
                "Done"
        );
    }
}