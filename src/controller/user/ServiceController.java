package controller.user;

import java.util.List;

import dao.user.ServiceDAO;
import model.user.Service;

public class ServiceController {

    private ServiceDAO serviceDAO;

    public ServiceController() {
        serviceDAO = new ServiceDAO();
    }

    public List<Service> getAllServices() {
        return serviceDAO.getAllServices();
    }
}