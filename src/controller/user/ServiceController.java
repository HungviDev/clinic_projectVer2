package controller.user;

import model.Service;

import java.util.List;

import dao.user.ServiceDAO;

public class ServiceController {

    private ServiceDAO serviceDAO;

    public ServiceController() {
        serviceDAO = new ServiceDAO();
    }

    public List<Service> getAllServices() {
        return serviceDAO.getAllServices();
    }
}