package controller;

import dao.ServiceDAO;
import model.Service;

import java.util.List;

public class ServiceController {

    private ServiceDAO serviceDAO;

    public ServiceController() {
        serviceDAO = new ServiceDAO();
    }

    public List<Service> getAllServices() {
        return serviceDAO.getAllServices();
    }
}