package controller.user;

import model.TreatmentHistory;

import java.util.List;

import dao.user.TreatmentHistoryDAO;

public class TreatmentHistoryController {

    private TreatmentHistoryDAO dao;

    public TreatmentHistoryController() {
        dao = new TreatmentHistoryDAO();
    }

    public List<TreatmentHistory> getByPatientId(int patientId) {
        return dao.getByPatientId(patientId);
    }
}