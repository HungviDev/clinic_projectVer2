package model.admin;

public class OrderModel {

    private int id;

    private String customerName;

    private String serviceName;

    private int quantity;

    private double total;

    private String status;

    public OrderModel() {
    }

    public OrderModel(
            int id,
            String customerName,
            String serviceName,
            int quantity,
            double total,
            String status
    ) {
        this.id = id;
        this.customerName = customerName;
        this.serviceName = serviceName;
        this.quantity = quantity;
        this.total = total;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}