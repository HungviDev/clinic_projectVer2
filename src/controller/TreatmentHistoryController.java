package controller;

import dao.TreatmentHistoryDAO;
import model.TreatmentHistory;

import java.util.List;

public class TreatmentHistoryController {

    private TreatmentHistoryDAO dao;

    public TreatmentHistoryController() {
        dao = new TreatmentHistoryDAO();
    }

    public List<TreatmentHistory> getByPatientId(int patientId) {
        return dao.getByPatientId(patientId);
    }
}