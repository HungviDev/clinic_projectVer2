package service;

import java.util.List;

import dao.user.PaymentDAO;
import model.user.Payment;

public class PaymentService {

    private PaymentDAO paymentDAO;

    public PaymentService() {
        paymentDAO = new PaymentDAO();
    }

    public List<Payment> getPaymentsByUserId(int userId) {
        return paymentDAO.getPaymentsByUserId(userId);
    }
}