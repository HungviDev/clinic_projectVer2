package controller.user;

import java.util.List;

import dao.user.TreatmentHistoryDAO;
import model.user.TreatmentHistory;

public class TreatmentHistoryController {

    private TreatmentHistoryDAO dao;

    public TreatmentHistoryController() {
        dao = new TreatmentHistoryDAO();
    }

    public List<TreatmentHistory> getByPatientId(int patientId) {
        return dao.getByPatientId(patientId);
    }
}